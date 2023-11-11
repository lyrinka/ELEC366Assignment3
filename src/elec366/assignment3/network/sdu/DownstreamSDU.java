package elec366.assignment3.network.sdu;

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
