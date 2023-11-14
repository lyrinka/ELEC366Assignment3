package elec366.assignment3.network.sdu;

/*
 * This class is common to server and client. 
 * 
 * This class abstracts all downstream SDUs sent by the upper layers. 
 */
public abstract class DownstreamSDU extends BaseSDU {

	public DownstreamSDU() {
		super();
	}

	public DownstreamSDU(int priority) {
		super(priority);
	}

	public DownstreamSDU(Priority priority) {
		super(priority);
	}
	
}
