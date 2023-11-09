package elec366.assignment3.protocol;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.security.KeyPair;
import java.security.SecureRandom;

import elec366.assignment3.protocol.codec.*;
import elec366.assignment3.protocol.codec.exception.*;
import elec366.assignment3.protocol.crypto.*;
import elec366.assignment3.protocol.packet.impl.*;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

public class ProtocolTest {

	public static void comprehensiveTest() {
		try {
		
			PipedOutputStream clientTx = new PipedOutputStream(); 
			PipedInputStream serverRx = new PipedInputStream(clientTx); 
			PipedOutputStream serverTx = new PipedOutputStream(); 
			PipedInputStream clientRx = new PipedInputStream(serverTx); 
			
			// Server codec
			PacketEncoder serverEncoder = new PacketEncoder(); 
			PacketDecoder serverDecoder = new PacketDecoder(); 
			
			// Client codec
			PacketEncoder clientEncoder = new PacketEncoder(); 
			PacketDecoder clientDecoder = new PacketDecoder(); 
			
			// Server side
			AsymmetricCrypto asc = new AsymmetricCryptoRSAImpl(); 
			KeyPair serverKeypair = asc.generateKeypair(); 
			serverEncoder.send(serverTx, new PacketOutSetPublicKey(serverKeypair.getPublic())); 
			
			// Client side
			byte[] clientPublicKey = ((PacketOutSetPublicKey)clientDecoder.readFullPacket(clientRx)).getPublicKey(); 
			byte[] clientKey = new byte[16]; 
			byte[] clientIV = new byte[16]; 
			SecureRandom clientSecureRandom = new SecureRandom(); 
			clientSecureRandom.nextBytes(clientKey); 
			clientSecureRandom.nextBytes(clientIV);
			StreamCipher clientCipher = new StreamCipherAESImpl(clientKey, clientIV); 
			clientKey = asc.encrypt(clientKey, clientPublicKey); 
			clientIV = asc.encrypt(clientIV, clientPublicKey); 
			clientEncoder.send(clientTx, new PacketInSetSessionKey(clientKey, clientIV));
			clientEncoder.attachCipher(clientCipher);
			
			// Server side
			PacketInSetSessionKey serverPacket1 = (PacketInSetSessionKey)serverDecoder.readFullPacket(serverRx); 
			byte[] serverKey = asc.decrypt(serverPacket1.getKey(), serverKeypair.getPrivate()); 
			byte[] serverIV = asc.decrypt(serverPacket1.getIv(), serverKeypair.getPrivate()); 
			StreamCipher serverCipher = new StreamCipherAESImpl(serverKey, serverIV); 
			serverDecoder.attachCipher(serverCipher);
			
			// Normal communication follows
			clientEncoder.send(clientTx, new PacketInLogin("steve"));
			System.out.println(((PacketInLogin)serverDecoder.readFullPacket(serverRx)).toString()); 
			
			clientEncoder.send(clientTx, new PacketInQueryPlayerList());
			System.out.println(((PacketInQueryPlayerList)serverDecoder.readFullPacket(serverRx)).toString()); 
			
			serverEncoder.send(serverTx, new PacketOutPlayerList(new String[] {"alex", "steve"}));
			System.out.println(((PacketOutPlayerList)clientDecoder.readFullPacket(clientRx)).toString()); 
			
			clientEncoder.send(clientTx, new PacketInChat("i found diamonds"));
			System.out.println(((PacketInChat)serverDecoder.readFullPacket(serverRx)).toString()); 
			
			serverEncoder.send(serverTx, new PacketOutChat(PacketOutChat.Type.CHAT_GLOBAL, "i found diamonds"));
			System.out.println(((PacketOutChat)clientDecoder.readFullPacket(clientRx)).toString()); 
			
		}
		catch(IOException | PacketDecodeException | PayloadDeserializationException ex) {
			throw new RuntimeException(ex); 
		}
		
	}
	
}
