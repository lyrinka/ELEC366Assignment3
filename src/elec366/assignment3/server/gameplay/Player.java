package elec366.assignment3.server.gameplay;

import java.util.Objects;

import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.impl.PacketOutChat;
import elec366.assignment3.server.connection.ConnectionServer;

public class Player {

	private final ConnectionServer server; 
	private final int connectionID; 
	private final String username; 
	
	public Player(ConnectionServer server, int connectionID, String username) {
		this.server = server; 
		this.connectionID = connectionID; 
		this.username = username; 
	}

	public ConnectionServer getServer() {
		return this.server;
	}

	public int getConnectionID() {
		return this.connectionID;
	}

	public String getUsername() {
		return this.username;
	}
	
	public void disconnect() {
		this.server.disconnect(this.connectionID);
	}
	
	public void sendPacket(Packet.Out packet) {
		this.server.sendPacket(this.connectionID, packet);
	}
	
	public void sendMessage(PacketOutChat.Type type, String message) {
		this.sendPacket(new PacketOutChat(type, message)); 
	}
	
	public void sendMessage(String message) {
		this.sendMessage(PacketOutChat.Type.DEFAULT, message); 
	}

	@Override
	public int hashCode() {
		return Objects.hash(username);
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
		return Objects.equals(this.username, other.username);
	}

}
