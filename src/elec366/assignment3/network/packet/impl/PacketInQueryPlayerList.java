package elec366.assignment3.network.packet.impl;

import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketType;

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
