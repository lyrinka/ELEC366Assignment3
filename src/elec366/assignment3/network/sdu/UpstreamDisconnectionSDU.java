package elec366.assignment3.network.sdu;

/*
 * This class is common to server and client. 
 * 
 * This SDU is sent by the connection worker and informs the upper layers
 * that the connection worker has disconnected, or was disconnected by the peer, 
 * due to a downstream disconnection SDU, 
 * or a PDU communication error (codec / serdes), 
 * or that the remote peer closes the connection. 
 */
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
