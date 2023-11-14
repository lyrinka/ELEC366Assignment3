package elec366.assignment3.protocol.packet.impl;

import java.util.Arrays;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;
import elec366.assignment3.protocol.serdes.PayloadDeserializer;
import elec366.assignment3.protocol.serdes.PayloadSerializer;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

/*
 * This class is common to server and client. 
 * 
 * This packet is sent by the server to the client
 * to reflect the usernames (including the recipient client)
 * of currently online users. 
 * 
 * This packet is sent by the server on demand (PacketInQueryPlayerList)
 * or when any player logins or disconnects. 
 */
public class PacketOutPlayerList extends Packet.Out {
	
	private final String[] playerList; 
	
	public PacketOutPlayerList(String[] playerList) {
		super(PacketType.OUT.PLAYER_LIST); 
		this.playerList = Arrays.copyOf(playerList, playerList.length); 
		
		PayloadSerializer enc = new PayloadSerializer(); 
		enc.writeInt(playerList.length);
		for(String player : playerList) 
			enc.writeString(player);
		this.payload = enc.getPayload();
	}
	
	public PacketOutPlayerList(byte[] payload) throws PayloadDeserializationException {
		super(PacketType.OUT.PLAYER_LIST, payload); 
		
		PayloadDeserializer dec = new PayloadDeserializer(payload, PacketOutPlayerList.class.getSimpleName()); 
		int length = dec.readInt(); 
		this.playerList = new String[length]; 
		for(int i = 0; i < length; i++) 
			this.playerList[i] = dec.readString(); 
	}
	
	public String[] getPlayerList() {
		return this.playerList; 
	}
	
	public String[] getPlayerListCopy() {
		return Arrays.copyOf(this.playerList, this.playerList.length); 
	}

	@Override
	public String toString() {
		return "PacketOutPlayerList [playerList=" + Arrays.toString(this.playerList) + "]";
	}
	
}
