package elec366.assignment3.network.sdu;

/*
 * This class is common to server and client. 
 * 
 * This SDU is send by the connection worker and informs the upper layers
 * that the connection worker has terminated connection due to an exception
 * that is not induced by PDU communication flows. 
 * 
 * This SDU is introduced to safely inform the upper layers to terminate
 * threads and exit when a fatal exception occurs. 
 */
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
