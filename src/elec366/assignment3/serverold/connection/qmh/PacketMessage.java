package elec366.assignment3.serverold.connection.qmh;

import elec366.assignment3.protocol.packet.Packet;

public class PacketMessage extends QueuedMessage {

	private final Packet packet; 
	
	public PacketMessage(int id, Packet packet) {
		super(id); 
		this.packet = packet; 
	}
	
	public Packet getPacket() {
		return this.packet; 
	}

	@Override
	public String toString() {
		return "PacketMessage [packet=" + this.packet + ", connectionID=" + this.connectionID + "]";
	}
	
}
