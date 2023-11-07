package elec366.assignment3.network.codec.exception;

public class PacketHeaderInvalidException extends PacketDecodeException {
	
	private static final long serialVersionUID = -2070583494651861594L;

	public PacketHeaderInvalidException(String message) {
		super(message); 
	}
	
}
