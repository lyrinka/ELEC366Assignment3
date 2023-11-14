package elec366.assignment3.protocol.packet;

import java.util.Arrays;

/*
 * This class is common to server and client. 
 * 
 * A packet is a PDU (protocol data unit) for this application. 
 * 
 * Packets are classified into OUT packets and IN packets, 
 * where OUT refers to server -> client, 
 * and IN refers to client -> server. (Directions are related to the server). 
 * Each packet type is also associated with a packet identifier (PID). 
 * The packet directions are intrinsically included in the name of the packet, 
 * while a full list of all packets, their PID and direction, can be found
 * at PacketType.java. 
 * 
 * The process of converting between Java Object representation of a Packet
 * and packet header + payload (byte array) is called serialization and deserialization. 
 * Serialization and deserialization process is implemented in the serdes package. 
 * 
 * The process of converting between the serialized / flattened representation of a Packet
 * and a stream of network bytes is called encoding and decoding. 
 * Encoding and decoding process is implemented in the codec package. 
 */
public abstract class Packet {
	
	protected final PacketDirection direction; 
	protected final PacketType type; 
	protected byte[] payload; 
	
	public Packet(PacketDirection direction, PacketType type, byte[] payload) {
		this.direction = direction; 
		this.type = type; 
		this.payload = payload; 
	}
	
	public PacketDirection getDirection() {
		return this.direction; 
	}
	
	public PacketType getType() {
		return this.type; 
	}
	
	public byte[] getPayloadCopy() {
		if(this.payload == null) return null; 
		return Arrays.copyOf(this.payload, this.payload.length); 
	}
	
	public byte[] getPayload() {
		return this.payload; 
	}
	
	public static abstract class In extends Packet {
		
		public In(PacketType type, byte[] payload) {
			super(PacketDirection.IN, type, payload);
		}

		public In(PacketType type) {
			this(type, null); 
		}
		
	}
	
	public static abstract class Out extends Packet {
		
		public Out(PacketType type, byte[] payload) {
			super(PacketDirection.OUT, type, payload);
		}
		
		public Out(PacketType type) {
			this(type, null); 
		}
		
	}
	
}
