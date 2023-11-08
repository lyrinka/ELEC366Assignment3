package elec366.assignment3.serverold.gameplay;

import java.util.Objects;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.impl.PacketOutChat;
import elec366.assignment3.serverold.connection.ConnectionServer;

public class Player {

	private final ConnectionServer server; 
	private final int connectionID; 
	private final String name; 
	
	public Player(ConnectionServer server, int connectionID, String name) {
		this.server = server; 
		this.connectionID = connectionID; 
		this.name = name; 
	}

	public ConnectionServer getServer() {
		return this.server;
	}

	public int getConnectionID() {
		return this.connectionID;
	}

	public String getName() {
		return this.name;
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
		this.sendMessage(PacketOutChat.Type.SYSMSG_SERVER, message); 
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
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
		return this.name.equalsIgnoreCase(other.name); 
	}

}
