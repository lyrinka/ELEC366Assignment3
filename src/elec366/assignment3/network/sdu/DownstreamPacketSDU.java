package elec366.assignment3.network.sdu;

import elec366.assignment3.protocol.packet.Packet;

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
		return "DownstreamPacketSDU [packet=" + this.packet + "]";
	}

}
