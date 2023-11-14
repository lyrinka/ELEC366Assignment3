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
 * when the client sends a chat message. 
 */
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
