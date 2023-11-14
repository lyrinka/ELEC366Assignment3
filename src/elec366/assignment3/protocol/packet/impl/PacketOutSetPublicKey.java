package elec366.assignment3.protocol.packet.impl;

import java.security.PublicKey;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;

/*
 * This class is common to server and client. 
 * 
 * This packet is sent from the server to the client
 * carrying its RSA public key for secure session establishment. 
 * 
 * This is the very first packet the server sends. 
 */
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

	@Override
	public String toString() {
		return "PacketOutSetPublicKey []";
	}
	
}
