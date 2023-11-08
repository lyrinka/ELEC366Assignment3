package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;
import elec366.assignment3.protocol.serdes.PayloadDeserializer;
import elec366.assignment3.protocol.serdes.PayloadSerializer;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

public class PacketInLogin extends Packet.In {

	private final String username; 
	
	public PacketInLogin(String username) {
		super(PacketType.IN.LOGIN); 
		this.username = username; 
		
		PayloadSerializer enc = new PayloadSerializer(); 
		enc.writeString(username);
		this.payload = enc.getPayload(); 
	}
	
	public PacketInLogin(byte[] payload) throws PayloadDeserializationException {
		super(PacketType.IN.LOGIN, payload);
		
		PayloadDeserializer dec = new PayloadDeserializer(payload, PacketInLogin.class.getSimpleName()); 
		this.username = dec.readString(); 
	}
	
	public String getUsername() {
		return this.username; 
	}

	@Override
	public String toString() {
		return "PacketInLogin [username=" + this.username + "]";
	}

}
