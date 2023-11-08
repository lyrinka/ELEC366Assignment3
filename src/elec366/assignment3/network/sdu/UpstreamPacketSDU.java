package elec366.assignment3.network.sdu;

import elec366.assignment3.protocol.packet.Packet;

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
		return "UpstreamPacketSDU [packet=" + this.packet + "]";
	}

}
