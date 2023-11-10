package elec366.assignment3.network.sdu;

public class UpstreamDisconnectionSDU extends UpstreamLoggingSDU {

	public UpstreamDisconnectionSDU() {
		super("Disconnected.");
	}

	public UpstreamDisconnectionSDU(String message, Throwable cause) {
		super(message, cause);
	}

	public UpstreamDisconnectionSDU(String reason) {
		super(reason);
	}

	public UpstreamDisconnectionSDU(Throwable cause) {
		super(cause);
	}

}
