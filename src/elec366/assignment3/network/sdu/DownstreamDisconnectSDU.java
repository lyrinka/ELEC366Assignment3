package elec366.assignment3.network.sdu;

/*
 * This class is common to server and client. 
 * 
 * This SDU is sent by the upper layers and informs the connection worker
 * to immediately release the network connection and terminate. 
 */
public class DownstreamDisconnectSDU extends DownstreamSDU {

	public DownstreamDisconnectSDU() {
		super(BaseSDU.Priority.VERY_HIGH); 
	}

	@Override
	public String toString() {
		return "DownstreamDisconnectSDU []";
	}

}
