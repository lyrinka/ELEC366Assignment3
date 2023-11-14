package elec366.assignment3.protocol.codec.exception;

/*
 * This exception is common to server and client. 
 * 
 * Packet desynchronized exception is thrown when the packet length field extracted
 * from the inbound stream is invalid. 
 * 
 * When session key is wrong with encryption enabled, this exception
 * is frequently seen.
 */
public class PacketDesynchronizedException extends PacketDecodeException {

	private static final long serialVersionUID = 5724150162353687051L;

	public PacketDesynchronizedException(String message) {
		super(message); 
	}
	
}
