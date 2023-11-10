package elec366.assignment3.protocol.serdes;

import java.util.Arrays;

import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;
import elec366.assignment3.protocol.serdes.exception.PayloadSizeException;

public class PayloadDeserializer {
	
	private final String packetName; 
	private final byte[] payload; 
	private int index; 
	
	public PayloadDeserializer(byte[] payload, String packetName) {
		this.packetName = packetName; 
		this.payload = payload; 
		this.index = 0; 
	}
	
	public PayloadDeserializer(byte[] payload, int requiredLength, String packetName) throws PayloadDeserializationException {
		this(payload, packetName); 
		if(payload.length != requiredLength) 
			this.throwSizeException(); 
	}
	
	public int readByte() throws PayloadDeserializationException {
		try {
			return Byte.toUnsignedInt(payload[this.index++]); 
		}
		catch(ArrayIndexOutOfBoundsException ignored) {
			this.throwSizeException(); 
			return 0; 
		}
	}
	
	public int readInt() throws PayloadDeserializationException {
		try {
			int value = 0; 
			value |= this.readByte() << 24; 
			value |= this.readByte() << 16;
			value |= this.readByte() << 8;
			value |= this.readByte();
			return value; 
		}
		catch(ArrayIndexOutOfBoundsException ignored) {
			this.throwSizeException(); 
			return 0; 
		}
	}
	
	public byte[] readBlob(int length) throws PayloadDeserializationException {
		if(this.payload.length < this.index + length) {
			this.throwSizeException(); 
			return null;
		}
		byte[] value = Arrays.copyOfRange(this.payload, this.index, this.index + length); 
		this.index += length; 
		return value; 
	}
	
	public byte[] readBlob() throws PayloadDeserializationException {
		return this.readBlob(this.readInt()); 
	}
	
	public String readString() throws PayloadDeserializationException {
		return new String(this.readBlob()); 
	}
	
	private void throwSizeException() throws PayloadSizeException {
		throw new PayloadSizeException(this.packetName, this.payload, this.index); 
	}
	
}
