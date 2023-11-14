package elec366.assignment3.protocol.codec.exception;

/*
 * This exception is common to server and client. 
 * 
 * Packet decode exception is the parent to all decoding-related exceptions.
 * Note that decoding is different compared to deserialization. 
 */
public class PacketDecodeException extends Exception {

	private static final long serialVersionUID = 430750636536802219L;
	
	public PacketDecodeException(String message) {
		super(message); 
	}
	
}
