package elec366.assignment3.consoleclient;

import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.SecureRandom;
import java.util.Scanner;

import elec366.assignment3.protocol.codec.PacketDecoder;
import elec366.assignment3.protocol.codec.PacketEncoder;
import elec366.assignment3.protocol.codec.exception.PacketDecodeException;
import elec366.assignment3.protocol.crypto.AsymmetricCrypto;
import elec366.assignment3.protocol.crypto.AsymmetricCryptoRSAImpl;
import elec366.assignment3.protocol.crypto.StreamCipher;
import elec366.assignment3.protocol.crypto.StreamCipherAESImpl;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.impl.PacketInChat;
import elec366.assignment3.protocol.packet.impl.PacketInLogin;
import elec366.assignment3.protocol.packet.impl.PacketInSetSessionKey;
import elec366.assignment3.protocol.packet.impl.PacketOutChat;
import elec366.assignment3.protocol.packet.impl.PacketOutSetPublicKey;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

public class ConsoleClient implements Runnable {

	private final String username;
	private final String host; 
	private final int port; 
	
	public ConsoleClient(String username) {
		this.username = username; 
		this.host = "localhost"; 
		this.port = 14569; 
	}
	
	@Override
	public void run() {
		try {
			this.println("> Connecting to " + this.host + ":" + this.port + " ...");
			Socket socket = new Socket(this.host, this.port); 
			this.runWrapper(socket); 
			socket.close();
		}
		catch(IOException | PacketDecodeException | PayloadDeserializationException | InterruptedException ex) {
			ex.printStackTrace(); 
		}
	}
	
	private void runWrapper(Socket socket) throws UnknownHostException, IOException, PacketDecodeException, PayloadDeserializationException, InterruptedException {
		InputStream iStream = socket.getInputStream(); 
		PacketDecoder dec = new PacketDecoder(); 
		PacketEncoder enc = new PacketEncoder(socket.getOutputStream()); 
		
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
		this.println("> Connected. Logging in as " + this.username + " ...");
		Thread.sleep(1000); // It takes some time for server to load session key (pending improvement)
		enc.send(new PacketInLogin(this.username));
		this.println("> Logged in.");
		
		(new Thread() {
			
			@Override
			public void run() {
				try {
					while(true) {
						Packet packet = dec.readFullPacket(iStream); 
						if(!(packet instanceof PacketOutChat)) continue; 
						PacketOutChat chatPacket = (PacketOutChat)packet; 
						ConsoleClient.this.println(chatPacket.getMessage()); 
					}
				}
				catch(PacketDecodeException | PayloadDeserializationException ex) {
					ex.printStackTrace();
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}
			}
			
		}).start();
		
		try (Scanner scanner = new Scanner(System.in)) {
			while(true) {
				String message = scanner.nextLine(); 
				enc.send(new PacketInChat(message));
			}
		}
		
	}
	
	private synchronized void println(String message) {
		System.out.println(message);
	}

	
	public static void entry(String username) {
		ConsoleClient client = new ConsoleClient(username); 
		client.run(); 
	}
	
}
