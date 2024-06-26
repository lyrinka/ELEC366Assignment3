package elec366.assignment3.server.connection;

import java.security.KeyPair;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

import elec366.assignment3.protocol.crypto.IAsymmetricCrypto;
import elec366.assignment3.protocol.crypto.IStreamCipher;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.impl.PacketInSetSessionKey;
import elec366.assignment3.protocol.packet.impl.PacketOutSessionAck;
import elec366.assignment3.protocol.packet.impl.PacketOutSetPublicKey;

/*
 * This class applies to only server.
 * 
 * The secure packet server is the 3rd application layer of networking. 
 * The secure packet server handles secure session establishment flow. 
 * Once a secure session has established, control is transferred to higher layers. 
 * 
 * Higher layers extend this class and implements the abstract methods.
 */
public abstract class SecurePacketServer extends PacketServer {

	private static enum SessionState {
		CONNECTED, 
		ESTABLISHED, 
	}
	
	private final Logger logger; 
	
	private final Map<Integer, SessionState> sessionMap; 
	
	private final IAsymmetricCrypto asc; 
	private final KeyPair keypair; 
	
	public SecurePacketServer(Logger serverLogger, Logger networkLogger, int port) {
		super(serverLogger, networkLogger, port); 
		this.logger = Objects.requireNonNull(serverLogger); 
		
		this.sessionMap = new HashMap<>(); 
		
		this.logger.info("Generating keypair..");
		this.asc = IAsymmetricCrypto.get(); 
		this.keypair = this.asc.generateKeypair(); 
		this.logger.info("Keypair generated.");
	}
	
	@Override
	protected void runServer() {
		this.logger.info("Started listener on port " + this.port + ".");
		super.runServer();
	}
	
	@Override
	public void onConnection(int id) {
		if(this.sessionMap.containsKey(id)) {
			this.logger.warning(this.getClientName(id) + " connected twice. How is this possible?");
			this.disconnect(id);
			return; 
		}
		this.sessionMap.put(id, SessionState.CONNECTED); 
		this.sendPacket(id, new PacketOutSetPublicKey(this.keypair.getPublic())); 
	}

	@Override
	public void onDisconnection(int id) {
		SessionState state = this.sessionMap.remove(id); 
		if(state == null) {
			this.logger.warning(this.getClientName(id) + " disconnected without being connected. How is this possible?");
			return; 
		}
		if(state == SessionState.ESTABLISHED)
			this.onSecureDisconnection(id);
	}

	@Override
	public void onInboundPacket(int id, Packet.In packet) {
		SessionState state = this.sessionMap.get(id); 
		if(state == null) {
			this.logger.warning(this.getClientName(id) + " sent an inbound packet without being connected. How is this possible?");
			this.disconnect(id);
			return; 
		}
		switch(state) {
			default:
			case CONNECTED: {
				if(!(packet instanceof PacketInSetSessionKey)) {
					this.logger.warning(this.getClientName(id) + " sent an unexpected packet during session establishment flow.");
					this.disconnect(id);
				}
				else {
					PacketInSetSessionKey packet0 = (PacketInSetSessionKey)packet; 
					byte[] key = this.asc.decrypt(packet0.getKey(), this.keypair.getPrivate()); 
					byte[] iv  = this.asc.decrypt(packet0.getIv(),	this.keypair.getPrivate()); 
					IStreamCipher cipher1 = IStreamCipher.get(key, iv); 
					IStreamCipher cipher2 = IStreamCipher.get(key, iv); 
					this.setDecoderEncryption(id, cipher1);
					this.setEncoderEncryption(id, cipher2);
					this.sessionMap.put(id, SessionState.ESTABLISHED); 
					this.sendPacket(id, new PacketOutSessionAck());
					this.onSecureConnection(id);
				}
				break;
			}
			case ESTABLISHED: {
				this.onSecureInboundPacket(id, packet);
				break;
			}
		}
		
	}
	
	@Override
	public void onServerQuit() {
		this.logger.info("Server stopped.");
	}

	public abstract void onSecureConnection(int id); 
	
	public abstract void onSecureDisconnection(int id); 
	
	public abstract void onSecureInboundPacket(int id, Packet.In packet); 
	
	public void sendSecurePacket(int connectionID, Packet.Out packet) {
		if(this.sessionMap.get(connectionID) == SessionState.ESTABLISHED)
			this.sendPacket(connectionID, packet);
	}
	
}
