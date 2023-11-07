package elec366.assignment3;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;

import elec366.assignment3.network.codec.PacketDecoder;
import elec366.assignment3.network.codec.PacketEncoder;
import elec366.assignment3.network.codec.exception.PacketDecodeException;
import elec366.assignment3.network.crypto.AsymmetricCrypto;
import elec366.assignment3.network.crypto.AsymmetricCryptoRSAImpl;
import elec366.assignment3.network.crypto.StreamCipher;
import elec366.assignment3.network.crypto.StreamCipherAESImpl;
import elec366.assignment3.network.packet.impl.PacketInLogin;
import elec366.assignment3.network.packet.impl.PacketInSetSessionKey;
import elec366.assignment3.network.packet.impl.PacketOutSetPublicKey;
import elec366.assignment3.network.serdes.exception.PayloadDeserializationException;
import elec366.assignment3.server.ServerTest;

public class Launcher {

	public static void main(String args[]) throws InterruptedException, UnknownHostException, IOException, PacketDecodeException, PayloadDeserializationException {
		
		ServerTest.comprehensiveTest();
		
		Thread.sleep(1000);
		
		Socket socket = new Socket("localhost", 14569); 
		
		InputStream iStream = socket.getInputStream(); 
		PacketDecoder dec = new PacketDecoder(); 
		PacketEncoder enc = new PacketEncoder(socket.getOutputStream()); 
		
		// Encryption flow
		byte[] publicKey = ((PacketOutSetPublicKey)dec.readFullPacket(iStream)).getPublicKey(); 
		AsymmetricCrypto asc = new AsymmetricCryptoRSAImpl(); 
		byte[] key = new byte[16]; 
		byte[] iv = new byte[16]; 
		SecureRandom sr = new SecureRandom(); 
		sr.nextBytes(key);
		sr.nextBytes(iv); 
		StreamCipher cip1 = new StreamCipherAESImpl(key, iv); 
		StreamCipher cip2 = new StreamCipherAESImpl(key, iv); 
		key = asc.encrypt(key, publicKey); 
		iv = asc.encrypt(iv, publicKey); 
		enc.send(new PacketInSetSessionKey(key, iv));
		enc.attachCipher(cip1);
		dec.attachCipher(cip2);
		System.out.println("Encryption flow done"); 
		
		// Login
		Thread.sleep(100);
		enc.send(new PacketInLogin("username"));
		
		// Receive messages
		System.out.println(dec.readFullPacket(iStream).toString()); 
		
		// Disconnect
		Thread.sleep(1000);
		socket.close(); 
		
	}

}
