package elec366.assignment3.network.sdu;

import elec366.assignment3.protocol.packet.Packet;

/*
 * This class is common to server and client. 
 * 
 * This SDU is sent by the connection worker and informs the upper layers
 * that the receiving worker has successfully obtained a PDU (packet) from inbound
 * network communication stream. 
 */
public class UpstreamPacketSDU extends UpstreamSDU {

	private final Packet packet;

	public UpstreamPacketSDU(Packet packet) {
		super();
		this.packet = packet;
	}

	public Packet getPacket() {
		return this.packet;
	}

	@Override
	public String toString() {
		return "UpstreamPacketSDU [packet=" + this.packet.toString() + "]";
	}

}
