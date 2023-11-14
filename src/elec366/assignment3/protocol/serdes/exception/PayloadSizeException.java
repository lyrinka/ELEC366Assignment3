package elec366.assignment3.protocol.serdes.exception;

/*
 * This exception is common to server and client. 
 * 
 * Payload size exception is thrown when the deserializer expects additional
 * bytes but the payload is already drained.
 */
public class PayloadSizeException extends PayloadDeserializationException {

	private static final long serialVersionUID = -1783013125772855636L;
	
	public PayloadSizeException(String packetName, byte[] payload, int index) {
		super(String.format("Packet is shorter than I expected, reading " + index + ". "), packetName, payload); 
	}
	
}
