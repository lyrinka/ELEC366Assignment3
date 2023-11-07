package elec366.assignment3.server.gameplay;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.KeyPair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import elec366.assignment3.network.crypto.AsymmetricCrypto;
import elec366.assignment3.network.crypto.AsymmetricCryptoRSAImpl;
import elec366.assignment3.network.crypto.StreamCipher;
import elec366.assignment3.network.crypto.StreamCipherAESImpl;
import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.Packet.In;
import elec366.assignment3.network.packet.impl.PacketInChat;
import elec366.assignment3.network.packet.impl.PacketInLogin;
import elec366.assignment3.network.packet.impl.PacketInQueryPlayerList;
import elec366.assignment3.network.packet.impl.PacketInSetSessionKey;
import elec366.assignment3.network.packet.impl.PacketOutPlayerList;
import elec366.assignment3.network.packet.impl.PacketOutSetPublicKey;
import elec366.assignment3.server.connection.ConnectionServer;
import elec366.assignment3.server.connection.qmh.EncryptionRequestMessage;

public abstract class PacketServer extends ConnectionServer {

	@Retention(RetentionPolicy.RUNTIME)
	private static @interface PacketHandler {
		
	}
	
	private static enum State {
		INITIAL, 
		ENCRYPTED, 
		LOGGED_IN, 
	}
	
	private final Map<Integer, State> clientMap; 
	private final Map<Integer, Player> playerMap; 
	
	private final Map<Class<? extends Packet.In>, Method> handlerMap; 
	
	private final AsymmetricCrypto asc; 
	
	private final KeyPair keypair; 
	
	@SuppressWarnings("unchecked")
	public PacketServer() {
		this.getLogger().info("Loading...");
		this.clientMap = new HashMap<>(); 
		this.playerMap = new HashMap<>(); 
		this.handlerMap = new HashMap<>(); 
		Arrays.stream(PacketServer.class.getDeclaredMethods())
			.filter(m -> m.isAnnotationPresent(PacketHandler.class))
			.filter(m -> m.getReturnType().toString().equals("void"))
			.filter(m -> m.getParameterCount() == 2)
			.filter(m -> m.getParameterTypes()[0].toString().equals("int"))
			.filter(m -> Packet.In.class.isAssignableFrom(m.getParameterTypes()[1]))
			.forEach(m -> {
				m.setAccessible(true); 
				this.handlerMap.put((Class<? extends In>)m.getParameterTypes()[1], m); 
			});
		this.getLogger().info("Generating keypair...");
		this.asc = new AsymmetricCryptoRSAImpl(); 
		this.keypair = this.asc.generateKeypair(); 
		this.getLogger().info("Loading complete."); 
	}
	
	@Override
	public void start() {
		this.getLogger().info("Starting server...");
		super.start();
		this.getLogger().info("Done!"); 
	}

	@Override
	protected void onInboundPacket(int connectionID, Packet.In packet) {
		State state = this.clientMap.get(connectionID); 
		if(state == null) {
			this.getLogger().warning("Client " + connectionID + " is not in registry but sent inbound packet. How is this possible?");
			this.getLogger().warning(packet.toString()); 
			this.disconnect(connectionID);
			return; 
		}
		Method method = this.handlerMap.get(packet.getClass()); 
		if(method == null) {
			this.getLogger().warning("Could not find dispatcher for: " + packet.toString() + " (from client " + connectionID + ")");
			return; 
		}
		try {
			method.invoke(this, connectionID, packet);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		} 
	}
	
	@Override
	protected void onConnection(int connectionID, String reason) {
		this.getLogger().fine("Client " + connectionID + " has connected.");
		this.clientMap.put(connectionID, State.INITIAL); 
		this.sendPacket(connectionID, new PacketOutSetPublicKey(this.keypair.getPublic()));
	}

	@Override
	protected void onDisconnection(int connectionID, String reason) {
		this.getLogger().fine("Client " + connectionID + " has disconnected.");
		this.clientMap.remove(connectionID); 
		Player player = this.playerMap.remove(connectionID); 
		if(player == null) return; 
		this.onPlayerQuit(player); 
	}

	@PacketHandler
	private void onPacketInSetSessionKey(int id, PacketInSetSessionKey packet) {
		if(this.clientMap.get(id) != State.INITIAL) {
			this.getLogger().warning("Client " + id + " sent encryption keys in the wrong state. How is this possible?");
			this.getLogger().warning(packet.toString());
			this.disconnect(id);
			return; 
		}
		byte[] encryptedKey = packet.getKey(); 
		byte[] encryptedIv = packet.getIv(); 
		byte[] key = this.asc.decrypt(encryptedKey, this.keypair.getPrivate());
		byte[] iv = this.asc.decrypt(encryptedIv, this.keypair.getPrivate()); 
		StreamCipher encodingCipher = new StreamCipherAESImpl(key, iv); 
		StreamCipher decodingCipher = new StreamCipherAESImpl(key, iv); 
		this.send(new EncryptionRequestMessage(id, encodingCipher, decodingCipher));
		this.clientMap.put(id, State.ENCRYPTED); 
	}
	
	@PacketHandler
	private void onPacketInLogin(int id, PacketInLogin packet) {
		if(this.clientMap.get(id) != State.ENCRYPTED) {
			this.getLogger().warning("Client " + id + " sent login request in the wrong state. How is this possible?");
			this.getLogger().warning(packet.toString());
			this.disconnect(id);
			return; 
		}
		Player player = new Player(this, id, packet.getUsername()); 
		if(this.playerMap.containsValue(player)) {
			this.onPlayerUsernameConflict(player);
			player.disconnect();
			return; 
		}
		this.clientMap.put(id, State.LOGGED_IN); 
		this.playerMap.put(id, player); 
		this.onPlayerLogin(player);
	}
	
	@PacketHandler
	private void onPacketInQueryPlayerList(int id, PacketInQueryPlayerList packet) {
		if(this.clientMap.get(id) != State.ENCRYPTED || this.clientMap.get(id) != State.LOGGED_IN) {
			this.getLogger().warning("Client " + id + " sent player list query in the wrong state. How is this possible?");
			this.getLogger().warning(packet.toString());
			this.disconnect(id);
			return; 
		}
		this.sendPacket(id, new PacketOutPlayerList(this.playerMap.values().stream().map(p -> p.getUsername()).toArray(String[]::new)));
	}
	
	@PacketHandler
	private void onPacketInChat(int id, PacketInChat packet) {
		Player player = this.playerMap.get(id); 
		if(player == null) {
			this.getLogger().warning("Client " + id + " sent chat without logging in. How is this possible?"); 
			this.getLogger().warning(packet.toString()); 
			this.disconnect(id);
			return; 
		}
		this.onPlayerChat(player, packet.getMessage()); 
	}
	
	protected abstract void onPlayerUsernameConflict(Player player); 
	
	protected abstract void onPlayerLogin(Player player); 
	
	protected abstract void onPlayerChat(Player player, String message); 
	
	protected abstract void onPlayerQuit(Player player); 
	
	public List<Player> getOnlinePlayers() {
		return new ArrayList<>(this.playerMap.values()); 
	}

}
