package elec366.assignment3.server.sdu;

import elec366.assignment3.network.sdu.BaseSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;

public class UpstreamConnectionSDU extends UpstreamSDU {

	public UpstreamConnectionSDU() {
		super(BaseSDU.Priority.HIGH); 
	}

}
