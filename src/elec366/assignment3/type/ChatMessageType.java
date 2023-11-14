package elec366.assignment3.type;

import java.util.HashMap;
import java.util.Map;

/*
 * This class is common to server and client. 
 * 
 * This enum represents the type of a message sent from the server to the client. 
 * Please see: PacketOutChat.
 */
public enum ChatMessageType {

	DEFAULT			(0x0), 
	SYSMSG_CLIENT	(0x1), 
	SYSMSG_SERVER	(0x2), 
	CHAT_GLOBAL		(0x3), 
	CHAT_PRIVATE	(0x4),
	; 
	
	private final int flag; 
	
	ChatMessageType(int flag) {
		this.flag = flag; 
	}
	
	public int getFlag() {
		return this.flag; 
	}
	
	private static Map<Integer, ChatMessageType> map; 
	
	static {
		ChatMessageType.map = new HashMap<>(); 
		for(ChatMessageType type : ChatMessageType.values()) 
			ChatMessageType.map.put(type.getFlag(), type); 
	}
	
	public static ChatMessageType getByFlag(int flag) {
		ChatMessageType type = ChatMessageType.map.get(flag); 
		if(type == null) return ChatMessageType.DEFAULT; 
		return type; 
	}
	
}
