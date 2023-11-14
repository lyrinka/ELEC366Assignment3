package elec366.assignment3.protocol.serdes;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
 * This class is common to server and client. 
 * 
 * The serializer writes bytes, numbers, arrays, strings, and other data structures
 * to a stream of bytes, on demand, which is later extracted as a block of bytes (payload). 
 * 
 * The serializer is unbounded - no buffer pre-allocation is required and it does not throw exceptions. 
 * 
 * The deserializer is stateful and the serializing stream is provided by ByteArrayOutputStream. 
 */
public class PayloadSerializer {
	
	private final ByteArrayOutputStream outStream; 
	
	public PayloadSerializer() {
		this.outStream = new ByteArrayOutputStream(); 
	}
	
	public byte[] getPayload() {
		return this.outStream.toByteArray(); 
	}
	
	public void writeByte(int value) {
		this.outStream.write(value & 0xFF);
	}
	
	public void writeInt(int value) {
		this.outStream.write(0xFF & (value >> 24)); 
		this.outStream.write(0xFF & (value >> 16)); 
		this.outStream.write(0xFF & (value >> 8)); 
		this.outStream.write(0xFF & (value)); 
	}
	
	public void writeBlob(byte[] blob) {
		if(blob.length == 0) return; 
		try {
			this.outStream.write(blob);
		} catch (IOException ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	public void writeBlobWithSize(byte[] blob) {
		this.writeInt(blob.length);
		this.writeBlob(blob);
	}
	
	public void writeString(String string) {
		this.writeBlobWithSize(string.getBytes());
	}
	
}
