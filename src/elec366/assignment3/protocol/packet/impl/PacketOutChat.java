package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;
import elec366.assignment3.protocol.serdes.PayloadDeserializer;
import elec366.assignment3.protocol.serdes.PayloadSerializer;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;
import elec366.assignment3.type.ChatMessageType;

public class PacketOutChat extends Packet.Out {
	
	private final ChatMessageType type;
	private final String message; 
	
	public PacketOutChat(ChatMessageType type, String message) {
		super(PacketType.OUT.CHAT); 
		this.type = type; 
		this.message = message; 
		
		PayloadSerializer enc = new PayloadSerializer(); 
		enc.writeByte(type.getFlag());
		enc.writeString(message);
		this.payload = enc.getPayload(); 
	}
	
	public PacketOutChat(byte[] payload) throws PayloadDeserializationException {
		super(PacketType.OUT.CHAT, payload); 
		
		PayloadDeserializer dec = new PayloadDeserializer(payload, PacketOutChat.class.getSimpleName()); 
		this.type = ChatMessageType.getByFlag(dec.readByte()); 
		this.message = dec.readString(); 
	}
	
	public ChatMessageType getMessageType() {
		return this.type; 
	}
	
	public String getMessage() {
		return this.message; 
	}

	@Override
	public String toString() {
		return "PacketOutChat [type=" + this.type + ", message=" + this.message + "]";
	}
	
}
