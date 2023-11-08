package elec366.assignment3.network.sdu;

public class UpstreamDisconnectionSDU extends UpstreamLoggingSDU {

	public UpstreamDisconnectionSDU() {
		super("Disconnected.");
		// TODO Auto-generated constructor stub
	}

	public UpstreamDisconnectionSDU(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public UpstreamDisconnectionSDU(String reason) {
		super(reason);
		// TODO Auto-generated constructor stub
	}

	public UpstreamDisconnectionSDU(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
