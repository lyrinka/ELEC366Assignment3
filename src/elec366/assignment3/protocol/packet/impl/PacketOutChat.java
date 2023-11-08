package elec366.assignment3.protocol.packet.impl;

import java.util.HashMap;
import java.util.Map;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;
import elec366.assignment3.protocol.serdes.PayloadDeserializer;
import elec366.assignment3.protocol.serdes.PayloadSerializer;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

public class PacketOutChat extends Packet.Out {
	
	public static enum Type {
		DEFAULT			(0x0), 
		SYSMSG_CLIENT	(0x1), 
		SYSMSG_SERVER	(0x2), 
		CHAT_GLOBAL		(0x3), 
		CHAT_PRIVATE	(0x4),
		; 
		
		private final int flag; 
		
		Type(int flag) {
			this.flag = flag; 
		}
		
		public int getFlag() {
			return this.flag; 
		}
		
		private static Map<Integer, Type> map; 
		
		static {
			Type.map = new HashMap<>(); 
			for(Type type : Type.values()) 
				Type.map.put(type.getFlag(), type); 
		}
		
		public static Type getByFlag(int flag) {
			Type type = Type.map.get(flag); 
			if(type == null) return Type.DEFAULT; 
			return type; 
		}
		
	}
	
	private final Type type;
	private final String message; 
	
	public PacketOutChat(Type type, String message) {
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
		this.type = Type.getByFlag(dec.readByte()); 
		this.message = dec.readString(); 
	}
	
	public Type getMessageType() {
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
