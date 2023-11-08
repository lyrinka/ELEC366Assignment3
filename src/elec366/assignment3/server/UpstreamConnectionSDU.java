package elec366.assignment3.server;

import elec366.assignment3.network.sdu.SDU;
import elec366.assignment3.network.sdu.UpstreamSDU;

public class UpstreamConnectionSDU extends UpstreamSDU {

	public UpstreamConnectionSDU() {
		super(SDU.Priority.HIGH); 
	}

}
