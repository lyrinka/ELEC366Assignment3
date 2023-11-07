package elec366.assignment3.network.packet.impl;

import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketType;

public class PacketOutSetPublicKey extends Packet.Out {

	public PacketOutSetPublicKey(byte[] payload) {
		super(PacketType.OUT.SET_PUBLIC_KEY, payload);
		// TODO Auto-generated constructor stub
	}
	
}
