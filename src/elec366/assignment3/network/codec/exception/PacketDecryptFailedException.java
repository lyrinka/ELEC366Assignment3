package elec366.assignment3.network.codec.exception;

public class PacketDecryptFailedException extends PacketDecodeException {

	private static final long serialVersionUID = -7893080694917485042L;

	public PacketDecryptFailedException(String message) {
		super(message);
	}

}
