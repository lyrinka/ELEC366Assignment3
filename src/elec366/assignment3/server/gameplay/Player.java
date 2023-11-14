package elec366.assignment3.server.gameplay;

import java.util.Objects;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.impl.PacketOutChat;
import elec366.assignment3.protocol.packet.impl.PacketOutPlayerList;
import elec366.assignment3.type.ChatMessageType;

/*
 * This class applies to only server.
 * 
 * Represents an online player (chat server user). 
 * The client has to be
 * - connected, 
 * - established a secure session, 
 * - logged in with a valid username
 * to be registered in the player registry of MultiplayerServer class. 
 * 
 * Player names are case-insensitive. 
 * 
 * The class gets its name "Player" from Minecraft, 
 * although the server does not provide any game content. 
 */
public class Player {

	private final MultiplayerServer server;
	private final int connectionID; 
	private final String username;
	private final String usernameLower; 
	
	public Player(MultiplayerServer server, int connectionID, String username) {
		this.server = server;
		this.connectionID = connectionID;
		this.username = username;
		this.usernameLower = this.username.toLowerCase(); 
	}

	public MultiplayerServer getServer() {
		return this.server;
	}

	public int getConnectionID() {
		return this.connectionID;
	}

	public String getName() {
		return this.username;
	}
	
	public void disconnect() {
		this.server.disconnect(this.connectionID);
	}
	
	public void sendPacket(Packet.Out packet) {
		this.server.sendSecurePacket(this.connectionID, packet);
	}
	
	public void sendMessage(ChatMessageType type, String message) {
		this.sendPacket(new PacketOutChat(type, message));
	}
	
	public void sendServerMessage(String message) {
		this.sendMessage(ChatMessageType.SYSMSG_SERVER, message);
	}
	
	public void updatePlayerlist() {
		this.sendPacket(
			new PacketOutPlayerList(
				this.server.getOnlinePlayerStream()
					.map(Player::getName)
					.toArray(String[]::new)
			)
		);
	}

	@Override
	public int hashCode() {
		return Objects.hash(usernameLower);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return this.usernameLower.equals(other.usernameLower); 
	}

}
