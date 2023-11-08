package elec366.assignment3.protocol.packet;

import java.util.HashMap;

import elec366.assignment3.protocol.packet.impl.PacketInChat;
import elec366.assignment3.protocol.packet.impl.PacketInLogin;
import elec366.assignment3.protocol.packet.impl.PacketInQueryPlayerList;
import elec366.assignment3.protocol.packet.impl.PacketInSetSessionKey;
import elec366.assignment3.protocol.packet.impl.PacketOutChat;
import elec366.assignment3.protocol.packet.impl.PacketOutPlayerList;
import elec366.assignment3.protocol.packet.impl.PacketOutSetPublicKey;

public interface PacketType {
	
	enum IN implements PacketType {
		
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
	
	enum OUT implements PacketType {
		
		SET_PUBLIC_KEY	(0x00, PacketOutSetPublicKey.class), 
		CHAT			(0x02, PacketOutChat.class), 
		PLAYER_LIST		(0x03, PacketOutPlayerList.class), 
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
