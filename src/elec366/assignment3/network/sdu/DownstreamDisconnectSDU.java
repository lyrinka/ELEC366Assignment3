package elec366.assignment3.network.sdu;

public class DownstreamDisconnectSDU extends DownstreamSDU {

	public DownstreamDisconnectSDU() {
		super(SDU.Priority.VERY_HIGH); 
	}

	@Override
	public String toString() {
		return "DownstreamDisconnectSDU []";
	}

}
