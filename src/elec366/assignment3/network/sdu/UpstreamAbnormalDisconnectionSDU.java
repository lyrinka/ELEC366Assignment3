package elec366.assignment3.network.sdu;

public class UpstreamAbnormalDisconnectionSDU extends UpstreamDisconnectionSDU {

	public UpstreamAbnormalDisconnectionSDU() {
		super();
	}

	public UpstreamAbnormalDisconnectionSDU(String message, Throwable cause) {
		super(message, cause);
	}

	public UpstreamAbnormalDisconnectionSDU(String reason) {
		super(reason);
	}

	public UpstreamAbnormalDisconnectionSDU(Throwable cause) {
		super(cause);
	}

}
