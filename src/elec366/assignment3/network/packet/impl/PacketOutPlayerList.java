package elec366.assignment3.network.packet.impl;

import java.util.Arrays;

import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketType;
import elec366.assignment3.network.serdes.PayloadDeserializer;
import elec366.assignment3.network.serdes.PayloadSerializer;
import elec366.assignment3.network.serdes.exception.PayloadDeserializationException;

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
