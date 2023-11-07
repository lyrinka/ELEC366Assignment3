package elec366.assignment3.network.packet;

import java.util.Arrays;

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
