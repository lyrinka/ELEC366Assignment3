package elec366.assignment3.protocol.codec.exception;

/*
 * This exception is common to server and client.
 * 
 * Packet decryption failed exception is thrown from ciphers to indicate
 * that a wrong session key is supplied.
 *  
 * This exception is, however, unused in the application. 
 */
public class PacketDecryptFailedException extends PacketDecodeException {

	private static final long serialVersionUID = -7893080694917485042L;

	public PacketDecryptFailedException(String message) {
		super(message);
	}

}
