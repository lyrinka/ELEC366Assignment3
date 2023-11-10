package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;

public class PacketInQueryPlayerList extends Packet.In {
	
	public PacketInQueryPlayerList() {
		super(PacketType.IN.QUERY_PLAYER_LIST, new byte[0]); 
	}
	
	public PacketInQueryPlayerList(byte[] payload) {
		super(PacketType.IN.QUERY_PLAYER_LIST, payload); 
	}

	@Override
	public String toString() {
		return "PacketInQueryPlayerList []";
	}
	
}
