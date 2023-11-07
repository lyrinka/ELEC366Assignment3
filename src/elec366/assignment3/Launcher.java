package elec366.assignment3;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import elec366.assignment3.network.codec.PacketDecoder;
import elec366.assignment3.network.codec.PacketEncoder;
import elec366.assignment3.network.codec.exception.PacketDecodeException;
import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.impl.*;
import elec366.assignment3.network.serdes.exception.PayloadDeserializationException;

public class Launcher {

	public static void main(String[] args) throws IOException, PacketDecodeException {
			
		ByteArrayOutputStream oStream = new ByteArrayOutputStream(); 
		
		PacketEncoder enc = new PacketEncoder(oStream); 
		
		enc.send(new PacketInLogin("username"));
		enc.send(new PacketInChat("Chat message sent to server 1."));
		enc.send(new PacketInChat("Chat message sent to server 2."));
		enc.send(new PacketInQueryPlayerList());
		enc.send(new PacketOutPlayerList(new String[] {"mary", "joe", "peter"}));
		enc.send(new PacketOutChat(PacketOutChat.Type.CHAT_PRIVATE, "someone: private chats to you"));
		
		
		byte[] array = oStream.toByteArray(); 
		System.out.println("Total bytes: " + array.length); 
		System.out.println(PayloadDeserializationException.convertToString(array)); 
		
		PacketDecoder dec = new PacketDecoder(); 
		
		for(byte b : array) {
			Packet packet = dec.accept(b); 
			if(packet == null) continue; 
			System.out.println(packet.toString()); 
		}
		
		
	}

}
