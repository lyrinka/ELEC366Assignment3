package elec366.assignment3.network.serdes.exception;

public class PayloadSizeException extends PayloadDeserializationException {

	private static final long serialVersionUID = -1783013125772855636L;
	
	public PayloadSizeException(String packetName, byte[] payload, int index) {
		super(String.format("Packet is shorter than I expected, reading " + index + ". "), packetName, payload); 
	}
	
}
