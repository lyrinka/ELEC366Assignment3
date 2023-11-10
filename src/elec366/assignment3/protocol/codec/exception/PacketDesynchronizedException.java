package elec366.assignment3.protocol.codec.exception;

public class PacketDesynchronizedException extends PacketDecodeException {

	private static final long serialVersionUID = 5724150162353687051L;

	public PacketDesynchronizedException(String message) {
		super(message); 
	}
	
}
