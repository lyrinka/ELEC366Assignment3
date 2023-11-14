package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;
import elec366.assignment3.protocol.serdes.PayloadDeserializer;
import elec366.assignment3.protocol.serdes.PayloadSerializer;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

/*
 * This class is common to server and client. 
 * 
 * This packet is sent by the client to the server
 * during secure session establishment flow. 
 * 
 * The client generates two secure random byte arrays, 
 * each 16 bytes long, as the symmetric encryption key
 * and initialization vector. The two secure parameters
 * are then encrypted with the server's RSA public key. 
 * The encrypted session keys are sent via this packet. 
 */
public class PacketInSetSessionKey extends Packet.In {
	
	private final byte[] key; 
	private final byte[] iv; 
	
	public PacketInSetSessionKey(byte[] key, byte[] iv) {
		super(PacketType.IN.SET_SESSION_KEY); 
		this.key = key; 
		this.iv = iv; 
		
		PayloadSerializer enc = new PayloadSerializer(); 
		enc.writeBlobWithSize(key);
		enc.writeBlobWithSize(iv);
		this.payload = enc.getPayload(); 
	}
	
	public PacketInSetSessionKey(byte[] payload) throws PayloadDeserializationException {
		super(PacketType.IN.SET_SESSION_KEY, payload);
		
		PayloadDeserializer dec = new PayloadDeserializer(payload, PacketInSetSessionKey.class.getSimpleName()); 
		this.key = dec.readBlob(); 
		this.iv = dec.readBlob(); 
	}
	
	public byte[] getKey() {
		return this.key; 
	}
	
	public byte[] getIv() {
		return this.iv; 
	}

	@Override
	public String toString() {
		return "PacketInSetSessionKey []";
	}
}
