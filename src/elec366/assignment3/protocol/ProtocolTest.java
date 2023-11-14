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
import elec366.assignment3.type.ChatMessageType;

/*
 * This class is a protocol unit test that also explains the secure session establishment flow. 
 * 
 * Note that this unit test implements an older version of secure session establishment flow. 
 * The difference between the older and newer version (PacketOutSessionAck) is explained in the comments. 
 * 
 * The encryption flow is similar to Minecraft protocol encryption flow. 
 * In fact, a lot of inspiration is taken from Minecraft networking system. 
 * See source: https://wiki.vg/Protocol_Encryption
 * 
 * Note that this key exchange scheme does not achieve perfect forward security (PFS). 
 * An attacker holding the RSA private key residing in server memory can decrypt past communications. 
 * It is implemented anyways as we couldn't get Diffie–Hellman key exchange or ECDH working at the very beginning. 
 * 
 * Please refer to in-code comments. 
 * 
 */
public class ProtocolTest {

	public static void comprehensiveTest() {
		try {
		
			PipedOutputStream clientTx = new PipedOutputStream(); 
			PipedInputStream serverRx = new PipedInputStream(clientTx); 
			PipedOutputStream serverTx = new PipedOutputStream(); 
			PipedInputStream clientRx = new PipedInputStream(serverTx); 
			
			// Server codec (the codec includes a serdes!)
			PacketEncoder serverEncoder = new PacketEncoder(); 
			PacketDecoder serverDecoder = new PacketDecoder(); 
			
			// Client codec
			PacketEncoder clientEncoder = new PacketEncoder(); 
			PacketDecoder clientDecoder = new PacketDecoder(); 
			
			
			// Server side
			IAsymmetricCrypto asc = IAsymmetricCrypto.get(); 
			KeyPair serverKeypair = asc.generateKeypair(); 
			// 1. Server sends RSA public key. 
			serverEncoder.send(serverTx, new PacketOutSetPublicKey(serverKeypair.getPublic())); 
			
			// Client side
			// 2. Client receives RSA public key. 
			byte[] clientPublicKey = ((PacketOutSetPublicKey)clientDecoder.readFullPacket(clientRx)).getPublicKey(); 
			byte[] clientKey = new byte[16]; 
			byte[] clientIV = new byte[16]; 
			// 3. Client generates random bytes as keys
			SecureRandom clientSecureRandom = new SecureRandom(); 
			clientSecureRandom.nextBytes(clientKey); 
			clientSecureRandom.nextBytes(clientIV);
			IStreamCipher clientCipher = IStreamCipher.get(clientKey, clientIV); 
			// 4. Client encrypts session keys with RSA
			clientKey = asc.encrypt(clientKey, clientPublicKey); 
			clientIV = asc.encrypt(clientIV, clientPublicKey); 
			// 5. Client sends encrypted session keys
			clientEncoder.send(clientTx, new PacketInSetSessionKey(clientKey, clientIV));
			clientEncoder.attachCipher(clientCipher); // Client enables encryption right after. 
			// In the real client, both RX and TX encryption is enabled. 
			
			// Server side
			// 6. Server receives encrypted session keys
			PacketInSetSessionKey serverPacket1 = (PacketInSetSessionKey)serverDecoder.readFullPacket(serverRx); 
			// 7. Server decrypts encrypted session keys
			byte[] serverKey = asc.decrypt(serverPacket1.getKey(), serverKeypair.getPrivate()); 
			byte[] serverIV = asc.decrypt(serverPacket1.getIv(), serverKeypair.getPrivate()); 
			IStreamCipher serverCipher = IStreamCipher.get(serverKey, serverIV); 
			serverDecoder.attachCipher(serverCipher); // Server immediately enables encryption
			
			// 8. Server sends session acknowledgement (encrypted) (not implemented here, exists in new version)
			
			// Client side
			// 9. Client receives session acknowledgement (encrypted) (not implemented here, exists in new version)
			
			// --- Starting from now, secure session has established. All future communications are encrypted and secure. 
			
			
			// Normal communication follows
			clientEncoder.send(clientTx, new PacketInLogin("steve"));
			System.out.println(((PacketInLogin)serverDecoder.readFullPacket(serverRx)).toString()); 
			
			clientEncoder.send(clientTx, new PacketInQueryPlayerList());
			System.out.println(((PacketInQueryPlayerList)serverDecoder.readFullPacket(serverRx)).toString()); 
			
			serverEncoder.send(serverTx, new PacketOutPlayerList(new String[] {"alex", "steve"}));
			System.out.println(((PacketOutPlayerList)clientDecoder.readFullPacket(clientRx)).toString()); 
			
			clientEncoder.send(clientTx, new PacketInChat("i found diamonds"));
			System.out.println(((PacketInChat)serverDecoder.readFullPacket(serverRx)).toString()); 
			
			serverEncoder.send(serverTx, new PacketOutChat(ChatMessageType.CHAT_GLOBAL, "i found diamonds"));
			System.out.println(((PacketOutChat)clientDecoder.readFullPacket(clientRx)).toString()); 
			
		}
		catch(IOException | PacketDecodeException | PayloadDeserializationException ex) {
			throw new RuntimeException(ex); 
		}
		
	}
	
}
