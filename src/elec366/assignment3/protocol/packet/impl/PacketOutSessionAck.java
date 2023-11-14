package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;

/*
 * This class is common to server and client. 
 * 
 * This packet is sent by the server to the client
 * when the server has received and applied encryption ciphers
 * to its encoder and decoders. 
 * This packet is the first packet to be sent encrypted and 
 * concludes the secure session flow. 
 */
public class PacketOutSessionAck extends Packet.Out {

	public PacketOutSessionAck() {
		super(PacketType.OUT.SESSION_ACK, new byte[0]); 
	}
	
	public PacketOutSessionAck(byte[] payload) {
		super(PacketType.OUT.SESSION_ACK, payload); 
	}

	@Override
	public String toString() {
		return "PacketOutSessionAck []";
	}

}
