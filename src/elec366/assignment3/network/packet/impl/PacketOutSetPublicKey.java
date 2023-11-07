package elec366.assignment3.network.packet.impl;

import java.security.PublicKey;

import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketType;

public class PacketOutSetPublicKey extends Packet.Out {
	
	public PacketOutSetPublicKey(PublicKey publicKey) {
		super(PacketType.OUT.SET_PUBLIC_KEY); 
		this.payload = publicKey.getEncoded(); 
	}
	
	public PacketOutSetPublicKey(byte[] payload) {
		super(PacketType.OUT.SET_PUBLIC_KEY, payload);
	}
	
	public byte[] getPublicKey() {
		return this.getPayload(); 
	}
	
}
