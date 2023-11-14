package elec366.assignment3.protocol.packet;

import java.util.HashMap;

import elec366.assignment3.protocol.packet.impl.PacketInChat;
import elec366.assignment3.protocol.packet.impl.PacketInLogin;
import elec366.assignment3.protocol.packet.impl.PacketInQueryPlayerList;
import elec366.assignment3.protocol.packet.impl.PacketInSetSessionKey;
import elec366.assignment3.protocol.packet.impl.PacketOutChat;
import elec366.assignment3.protocol.packet.impl.PacketOutPlayerList;
import elec366.assignment3.protocol.packet.impl.PacketOutSessionAck;
import elec366.assignment3.protocol.packet.impl.PacketOutSetPublicKey;

/*
 * This class is common to server and client. 
 * 
 * This interface denotes that the implementing enum represents a type of packet, 
 * with a defined direction, packet ID, and packet class. 
 * The direction and packet ID is used in various codec / serdes operations. 
 * The packet class is used specifically to obtain the deserializing constructor. 
 * Please see individual packet implementations for comments on constructors and fields. 
 */
public interface PacketType {
	
	// Groups all IN packets
	enum IN implements PacketType {
		
		// Internal name, 	 PID,  Packet class
		SET_SESSION_KEY		(0x00, PacketInSetSessionKey.class), 
		LOGIN				(0x01, PacketInLogin.class), 
		CHAT				(0x02, PacketInChat.class), 
		QUERY_PLAYER_LIST	(0x03, PacketInQueryPlayerList.class), 
		;
		
		private static HashMap<Integer, IN> packetMap = new HashMap<>(); 
		
		static {
			for(IN e : IN.values()) packetMap.put(e.packetID, e); 
		}
		
		public static IN getPacketByID(int packetID) {
			return packetMap.get(packetID); 
		}
		
		private final int packetID; 
		private final Class<? extends Packet> packetClass; 
		
		IN(int packetID, Class<? extends Packet> packetClass) {
			this.packetID = packetID; 
			this.packetClass = packetClass; 
		}
		
		@Override
		public PacketDirection getPacketDirection() {
			return PacketDirection.IN; 
		}
		
		@Override
		public int getPacketID() {
			return this.packetID; 
		}
		
		@Override
		public Class<? extends Packet> getPacketClass() {
			return this.packetClass; 
		}
		
	}
	
	// Groups all OUT packets
	enum OUT implements PacketType {
		
		// Internal name, 	 PID,  Packet class
		SET_PUBLIC_KEY		(0x00, PacketOutSetPublicKey.class), 
		SESSION_ACK			(0x01, PacketOutSessionAck.class), 
		CHAT				(0x02, PacketOutChat.class), 
		PLAYER_LIST			(0x03, PacketOutPlayerList.class), 
		;
		
		private static HashMap<Integer, OUT> packetMap = new HashMap<>(); 
		
		static {
			for(OUT e : OUT.values()) packetMap.put(e.packetID, e); 
		}
		
		public static OUT getPacketByID(int packetID) {
			return packetMap.get(packetID); 
		}
		
		private final int packetID; 
		private final Class<? extends Packet> packetClass; 
		
		OUT(int packetID, Class<? extends Packet> packetClass) {
			this.packetID = packetID; 
			this.packetClass = packetClass; 
		}
		
		@Override
		public PacketDirection getPacketDirection() {
			return PacketDirection.OUT; 
		}
		
		@Override
		public int getPacketID() {
			return this.packetID; 
		}
		
		@Override
		public Class<? extends Packet> getPacketClass() {
			return this.packetClass; 
		}
		
	}
	
	public PacketDirection getPacketDirection(); 
	
	public int getPacketID(); 
	
	public Class<? extends Packet> getPacketClass(); 
	
	
}
