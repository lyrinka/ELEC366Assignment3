package elec366.assignment3.network.sdu;

/*
 * This class is common to server and client. 
 * 
 * This class abstracts all upstream SDUs sent by the connection worker. 
 */
public class UpstreamSDU extends BaseSDU {

	public UpstreamSDU() {
		super();
	}

	public UpstreamSDU(int priority) {
		super(priority);
	}

	public UpstreamSDU(BaseSDU.Priority priority) {
		super(priority);
	}

}
