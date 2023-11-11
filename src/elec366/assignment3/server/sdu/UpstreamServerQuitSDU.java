package elec366.assignment3.server.sdu;

import elec366.assignment3.network.sdu.BaseSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;

public class UpstreamServerQuitSDU extends UpstreamSDU {

	public UpstreamServerQuitSDU() {
		super(BaseSDU.Priority.VERY_HIGH); 
	}

}
