package elec366.assignment3.protocol.packet.impl;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;

/*
 * This class is common to server and client. 
 * 
 * This packet is sent by the client to the server
 * when the client wishes to request a list of online players. 
 * 
 * The server will reply with a PacketOutPlayerList. 
 * In addition, the server automatically sends PacketOutPlayerList
 * to all connected clients whenever there is user logging in or out. 
 * 
 * This packet is unused by the client. It is included for debugging
 * and future extensions. 
 */
public class PacketInQueryPlayerList extends Packet.In {
	
	public PacketInQueryPlayerList() {
		super(PacketType.IN.QUERY_PLAYER_LIST, new byte[0]); 
	}
	
	public PacketInQueryPlayerList(byte[] payload) {
		super(PacketType.IN.QUERY_PLAYER_LIST, payload); 
	}

	@Override
	public String toString() {
		return "PacketInQueryPlayerList []";
	}
	
}
