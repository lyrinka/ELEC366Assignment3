package elec366.assignment3.network.packet.impl;

import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketType;
import elec366.assignment3.network.serdes.PayloadDeserializer;
import elec366.assignment3.network.serdes.PayloadSerializer;
import elec366.assignment3.network.serdes.exception.PayloadDeserializationException;

public class PacketInChat extends Packet.In {
	
	private final String message; 
	
	public PacketInChat(String message) {
		super(PacketType.IN.CHAT); 
		this.message = message; 
		
		PayloadSerializer enc = new PayloadSerializer(); 
		enc.writeString(message);
		this.payload = enc.getPayload(); 
	}
	
	public PacketInChat(byte[] payload) throws PayloadDeserializationException {
		super(PacketType.IN.CHAT, payload); 
		
		PayloadDeserializer dec = new PayloadDeserializer(payload, PacketInChat.class.getSimpleName()); 
		this.message = dec.readString(); 
	}
	
	public String getMessage() {
		return this.message; 
	}

	@Override
	public String toString() {
		return "PacketInChat [message=" + this.message + "]";
	}
	
}
