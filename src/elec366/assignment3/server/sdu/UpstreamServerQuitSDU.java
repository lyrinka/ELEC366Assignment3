package elec366.assignment3.server.sdu;

import elec366.assignment3.network.sdu.SDU;
import elec366.assignment3.network.sdu.UpstreamSDU;

public class UpstreamServerQuitSDU extends UpstreamSDU {

	public UpstreamServerQuitSDU() {
		super(SDU.Priority.VERY_HIGH); 
	}

}
