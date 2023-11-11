package elec366.assignment3.network.sdu;

public class DownstreamDisconnectSDU extends DownstreamSDU {

	public DownstreamDisconnectSDU() {
		super(BaseSDU.Priority.VERY_HIGH); 
	}

	@Override
	public String toString() {
		return "DownstreamDisconnectSDU []";
	}

}
