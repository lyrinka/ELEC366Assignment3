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
	
	public void sendChatMessage(String message) {
		this.sendSecurePacket(new PacketInChat(message));
	}
	
	public void queryPlayerList() {
		this.sendSecurePacket(new PacketInQueryPlayerList());
	}

}