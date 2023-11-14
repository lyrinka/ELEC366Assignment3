package elec366.assignment3.protocol.serdes.exception;

import java.util.Arrays;

/*
 * This exception is common to server and client. 
 * 
 * Payload deserialization exception is the parent to all deserialization-related exceptions.
 * Note that deserialization is different compared to decoding. 
 * The exception carries additional fields containing the content of the
 * exception-inducing packet for further debugging. 
 */
public class PayloadDeserializationException extends Exception {
	
	private static final long serialVersionUID = 9202965290980350916L;

	private final byte[] payload; 
	
	public PayloadDeserializationException(String message, String packetName, byte[] payload) {
		super(message + String.format("{packet=%s,content=[%s],length=%d}", packetName == null ? "<unnamed>" : packetName, convertToString(payload), payload.length)); 
		this.payload = Arrays.copyOf(payload, payload.length); 
	}
	
	public byte[] getPayload() {
		return this.payload; 
	}
	
	public static String convertToString(byte[] payload) {
		StringBuilder sb = new StringBuilder(); 
		for(byte b : payload) {
			String bs = Integer.toString(Byte.toUnsignedInt(b), 16).toUpperCase(); 
			if(bs.length() == 1) sb.append("0"); 
			sb.append(bs); 
			sb.append(", "); 
		}
		sb.delete(sb.length() - 2, sb.length()); 
		return sb.toString(); 
	}
	
}
