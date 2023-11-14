package elec366.assignment3.server.sdu;

import elec366.assignment3.network.sdu.BaseSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;

/*
 * This class applies to only server.
 * 
 * A server-specific add-on SDU. 
 * This SDU is sent to upper layers when the server decides to quit, usually due to ane exception.
 * The upper layers can then terminate their threads, such as GUI thread.
 */
public class UpstreamServerQuitSDU extends UpstreamSDU {

	public UpstreamServerQuitSDU() {
		super(BaseSDU.Priority.VERY_HIGH); 
	}

}
