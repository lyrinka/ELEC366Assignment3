package elec366.assignment3.protocol.codec.exception;

/*
 * This exception is common to server and client. 
 * 
 * Packet header invalid exception is thrown when the indicated packet ID
 * is not found in the registry. This is usually seen when there is a version
 * mismatch between the server and the client, that one party has implemented
 * some newer packets that the other party does not understand. 
 */
public class PacketHeaderInvalidException extends PacketDecodeException {
	
	private static final long serialVersionUID = -2070583494651861594L;

	public PacketHeaderInvalidException(String message) {
		super(message); 
	}
	
}
