package elec366.assignment3.network.sdu;

import elec366.assignment3.protocol.packet.Packet;

/*
 * This class is common to server and client. 
 * 
 * This SDU is sent by the upper layers and informs the connection worker
 * to immediately serialize and encode a packet
 * and transmit the byte streams over the underlying network connection. 
 */
public class DownstreamPacketSDU extends DownstreamSDU {

	private final Packet packet; 
	
	public DownstreamPacketSDU(Packet packet) {
		super(); 
		this.packet = packet; 
	}

	public Packet getPacket() {
		return this.packet;
	}

	@Override
	public String toString() {
		return "DownstreamPacketSDU [packet=" + this.packet.toString() + "]";
	}

}
