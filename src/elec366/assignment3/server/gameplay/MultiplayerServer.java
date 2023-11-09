package elec366.assignment3.server.gameplay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.Packet.In;
import elec366.assignment3.protocol.packet.impl.PacketInChat;
import elec366.assignment3.protocol.packet.impl.PacketInLogin;
import elec366.assignment3.protocol.packet.impl.PacketInQueryPlayerList;
import elec366.assignment3.protocol.packet.impl.PacketOutPlayerList;
import elec366.assignment3.server.connection.SecurePacketServer;

public abstract class MultiplayerServer extends SecurePacketServer {
	
	private final Map<Integer, Optional<Player>> playerMap; 
	
	public MultiplayerServer(Logger serverLogger, Logger networkLogger, int port) {
		super(serverLogger, networkLogger, port);
		this.playerMap = new HashMap<>(); 
	}

	@Override
	public void onSecureConnection(int id) {
		this.playerMap.put(id, Optional.empty()); 
	}

	@Override
	public void onSecureDisconnection(int id) {
		Optional<Player> oPlayer = this.playerMap.get(id); 
		if(oPlayer == null) {
			this.disconnect(id);
			return; 
		}
		if(!oPlayer.isPresent()) return; 
		Player player = oPlayer.get(); 
		this.onPlayerQuit(player); 
		this.broadcastPlayerListChange(); 
		this.playerMap.remove(id); 
	}

	@Override
	public void onSecureInboundPacket(int id, In packet) {
		Optional<Player> oPlayer = this.playerMap.get(id); 
		if(oPlayer == null) {
			this.disconnect(id);
			return; 
		}
		if(!oPlayer.isPresent()) {
			if(!(packet instanceof PacketInLogin)) return; 
			String username = ((PacketInLogin)packet).getUsername(); 
			Player temporaryPlayer = new Player(this, id, username); 
			if(this.playerMap.values()
				.stream()
				.filter(Optional::isPresent)
				.map(Optional::get)
				.anyMatch(p -> temporaryPlayer.equals(p))) {
				this.onPlayerNameConflict(temporaryPlayer); 
				temporaryPlayer.disconnect();
			}
			else {
				boolean result = this.onPlayerPreLogin(temporaryPlayer); 
				if(!result) {
					temporaryPlayer.disconnect(); 
					return; 
				}
				this.playerMap.put(id, Optional.of(temporaryPlayer)); 
				this.broadcastPlayerListChange(); 
				this.onPlayerLogin(temporaryPlayer); 
			}
		}
		else {
			if(packet instanceof PacketInLogin) return; 
			Player player = oPlayer.get(); 
			this.onPlayerPacket(player, packet); 
		}
	}
	
	private void broadcastPlayerListChange() {
		PacketOutPlayerList updatePacket = new PacketOutPlayerList(
				this.playerMap.values()
					.stream()
					.filter(Optional::isPresent)
					.map(Optional::get)
					.map(Player::getName)
					.toArray(String[]::new)
		);
		this.playerMap.values()
			.stream()
			.filter(Optional::isPresent)
			.map(Optional::get)
			.forEach(p -> p.sendPacket(updatePacket));
	}
	
	protected void onPlayerPacket(Player player, Packet.In packet) {
		if(packet instanceof PacketInQueryPlayerList) {
			player.updatePlayerlist();
			return; 
		}
		if(packet instanceof PacketInChat) {
			this.onPlayerChat(player, ((PacketInChat)packet).getMessage()); 
			return; 
		}
	}
	
	public abstract void onPlayerNameConflict(Player player); 
	
	public abstract boolean onPlayerPreLogin(Player player); 
	
	public abstract void onPlayerLogin(Player player); 
	
	public abstract void onPlayerQuit(Player player); 
	
	public abstract void onPlayerChat(Player player, String message); 
	
	public Stream<Player> getOnlinePlayerStream() {
		return this.playerMap.values()
				.stream()
				.filter(Optional::isPresent)
				.map(Optional::get); 
	}
	
	public List<Player> getOnlinePlayers() {
		return this.getOnlinePlayerStream().collect(Collectors.toList()); 
	}
	
	public int getOnlinePlayerCount() {
		return (int) this.getOnlinePlayerStream().count(); 
	}

}