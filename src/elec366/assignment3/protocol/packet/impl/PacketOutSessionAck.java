package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;

public class PacketOutSessionAck extends Packet.Out {

	public PacketOutSessionAck() {
		super(PacketType.OUT.SESSION_ACK, new byte[0]); 
	}
	
	public PacketOutSessionAck(byte[] payload) {
		super(PacketType.OUT.SESSION_ACK, payload); 
	}

	@Override
	public String toString() {
		return "PacketOutSessionAck []";
	}

}
