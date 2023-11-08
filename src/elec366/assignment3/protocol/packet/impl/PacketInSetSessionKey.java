package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;
import elec366.assignment3.protocol.serdes.PayloadDeserializer;
import elec366.assignment3.protocol.serdes.PayloadSerializer;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

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

}
