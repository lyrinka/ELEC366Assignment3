package elec366.assignment3.network.packet.impl;

import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketType;

public class PacketInSetSessionKey extends Packet.In {

	public PacketInSetSessionKey(byte[] payload) {
		super(PacketType.IN.SET_SESSION_KEY, payload);
		// TODO Auto-generated constructor stub
	}

}
