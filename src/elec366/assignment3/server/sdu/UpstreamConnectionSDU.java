package elec366.assignment3.server.sdu;

import elec366.assignment3.network.sdu.BaseSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;

/*
 * This class applies to only server.
 * 
 * A server-specific add-on SDU. 
 * This SDU is sent to upper layers when a new client establishes connection and a connection worker is instantiated.
 * The client connection ID is sent in conjugate with this SDU in a Pair<Integer, SDU>. 
 */
public class UpstreamConnectionSDU extends UpstreamSDU {

	public UpstreamConnectionSDU() {
		super(BaseSDU.Priority.HIGH); 
	}

}
