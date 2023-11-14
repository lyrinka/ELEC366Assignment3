package elec366.assignment3.client.gameplay;

import java.util.logging.Logger;

import elec366.assignment3.client.connection.SecurePacketClient;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.impl.PacketInChat;
import elec366.assignment3.protocol.packet.impl.PacketInLogin;
import elec366.assignment3.protocol.packet.impl.PacketInQueryPlayerList;
import elec366.assignment3.protocol.packet.impl.PacketOutChat;
import elec366.assignment3.protocol.packet.impl.PacketOutPlayerList;
import elec366.assignment3.type.ChatMessageType;

/*
 * This class applies to only client.
 * 
 * The chat client is the 4th application layer, and the final layer for networking. 
 * The chat client handles player chats, command issues, connections and disconnection, and player list displays. 
 * Chat application related functionalities are all concentrated here, 
 * although most of the functionalities are handled by the server for extensibility. 
 * The chat client is simple compared to chat server.
 */
public abstract class ChatClient extends SecurePacketClient {

	private final String username; 

	public ChatClient(Logger clientLogger, Logger networkLogger, String host, int port, String username) {
		super(clientLogger, networkLogger, host, port);
		this.username = username; 
	}
	
	public String getUsername() {
		return this.username; 
	}

	@Override
	public void onSecureConnection() {
		this.sendSecurePacket(new PacketInLogin(this.username));
		this.onChatServerConnection(); 
	}

	@Override
	public void onSecureDisconnection() {
		this.onChatServerDisconnection(); 
	}
	
	@Override
	public void onSecureAbnormalDisconnection(String reason, Throwable cause) {
		this.onChatServerAbnormalDisconnection(reason, cause);
	}

	@Override
	public void onSecureOutboundPacket(Packet.Out packet) {
		if(packet instanceof PacketOutChat) {
			PacketOutChat packet0 = (PacketOutChat)packet; 
			this.onInboundChatMessage(packet0.getMessageType(), packet0.getMessage());
			return; 
		}
		if(packet instanceof PacketOutPlayerList) {
			PacketOutPlayerList packet0 = (PacketOutPlayerList)packet; 
			this.onPlayerListUpdate(packet0.getPlayerList());
			return; 
		}
	}
	
	public abstract void onChatServerConnection(); 
	
	public abstract void onInboundChatMessage(ChatMessageType type, String message); 
	
	public abstract void onPlayerListUpdate(String[] players); 
	
	public abstract void onChatServerDisconnection(); 
	
	public void onChatServerAbnormalDisconnection(String reason, Throwable cause) {
		this.onChatServerDisconnection();
	}
	
	public void sendChatMessage(String message) {
		this.sendSecurePacket(new PacketInChat(message));
	}
	
	public void queryPlayerList() {
		this.sendSecurePacket(new PacketInQueryPlayerList());
	}

}
