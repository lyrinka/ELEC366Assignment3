package elec366.assignment3.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import elec366.assignment3.network.sdu.DownstreamDisconnectSDU;
import elec366.assignment3.network.sdu.DownstreamEncryptSDU;
import elec366.assignment3.network.sdu.DownstreamPacketSDU;
import elec366.assignment3.network.sdu.DownstreamSDU;
import elec366.assignment3.network.sdu.UpstreamDisconnectionSDU;
import elec366.assignment3.network.sdu.UpstreamPacketSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;
import elec366.assignment3.protocol.codec.PacketDecoder;
import elec366.assignment3.protocol.codec.PacketEncoder;
import elec366.assignment3.protocol.codec.exception.PacketDecodeException;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;
import elec366.assignment3.util.Pair;

public class Connection {

	private static final String RX_TAG = "ConnectionWorker-%d-RX"; 
	private static final String TX_TAG = "ConnectionWorker-%d-TX"; 
	
	private final Logger logger;
	
	private final int id; 
	private final Socket socket; 
	private final Supplier<DownstreamSDU> downstream; 
	private final Consumer<UpstreamSDU> upstream; 
	
	private final ConcurrentLinkedQueue<DownstreamEncryptSDU> encryptionRequests; 
	
	public Connection(Logger logger, int id, Socket socket, Pair<Supplier<DownstreamSDU>, Consumer<UpstreamSDU>> channel) {
		this.logger = logger; 
		
		this.id = id; 
		this.socket = socket; 
		this.downstream = channel.getFirst(); 
		this.upstream = channel.getSecond(); 
		
		this.encryptionRequests = new ConcurrentLinkedQueue<>(); 
	}
	
	public int getId() {
		return this.id; 
	}
	
	public void start() throws IOException {
		
		Thread rxThread = new Thread(() -> {
			
			UpstreamDisconnectionSDU sdu; 
			
			try {
				this.trace("[RX] Starting RX..."); 
				this.runRx();
				sdu = new UpstreamDisconnectionSDU(); 
			}
			catch (IOException e) {
				sdu = new UpstreamDisconnectionSDU("Remote peer closed connection.", e); 
			}
			catch (PacketDecodeException | PayloadDeserializationException e) {
				sdu = new UpstreamDisconnectionSDU("Packet decoding failed.", e); 
			}
			
			if(sdu.getCause() == null) {
				this.trace("[RX] " + sdu.getReason()); 
			}
			else {
				this.trace("[RX] " + sdu.getReason(), sdu.getCause()); 
			}
			
			try {
				this.socket.close(); 
			}
			catch(IOException ignored) {
				this.trace("[RX] Exception while closing socket.", ignored); 
			}
			this.upstream.accept(sdu); 
			this.trace("[RX] Thread termination."); 
			
		}, String.format(RX_TAG, this.id)); 
		
		Thread txThread = new Thread(() -> {
			
			try {
				this.trace("[TX] Starting TX..."); 
				this.runTx();
			} catch (IOException e) {
				// TODO: shall we ignore this?
				this.trace("[TX] IO exception in TX", e); 
			} catch (InterruptedException e) {
				this.trace("[TX] Interrupted", e); 
			}
			this.trace("[TX] Thread termination."); 
			
		}, String.format(TX_TAG, this.id)); 
		
		
		rxThread.start(); 
		txThread.start(); 
		
	}
	
	private void runRx() throws IOException, PacketDecodeException, PayloadDeserializationException {
		
		InputStream iStream = this.socket.getInputStream(); 
		
		PacketDecoder decoder = new PacketDecoder(); 
		
		while(true) {
			DownstreamEncryptSDU encryptionRequest = this.encryptionRequests.poll(); 
			if(encryptionRequest != null) {
				this.trace("[RX] Attached decoder cipher");
				decoder.attachCipher(encryptionRequest.getCipher());
			}
			Packet packet = null; 
			while(packet == null) {
				int data = iStream.read(); 
				if(data < 0) {
					this.trace("[RX] Input stream drained");
					return; 
				}
				packet = decoder.accept((byte)data); 
			}
			this.trace("[RX] Received packet " + packet.toString());
			this.upstream.accept(new UpstreamPacketSDU(packet)); 
		}
		
	}
	
	private void runTx() throws IOException, InterruptedException {
		
		OutputStream oStream = this.socket.getOutputStream(); 
		
		PacketEncoder encoder = new PacketEncoder(oStream); 
		
		while(true) {
			DownstreamSDU sdu = this.downstream.get(); 
			if(sdu == null) {
				this.trace("[TX] Queue fetch interrupted.");
				return; 
			}
			this.trace("[TX] Received SDU: " + sdu.toString());
			if(sdu instanceof DownstreamDisconnectSDU) return; 
			if(sdu instanceof DownstreamEncryptSDU) {
				DownstreamEncryptSDU encryptionRequest = (DownstreamEncryptSDU)sdu; 
				if(encryptionRequest.getMode() == DownstreamEncryptSDU.Mode.ENCRYPT_ENCODER) 
					encoder.attachCipher(encryptionRequest.getCipher());
				else
					this.encryptionRequests.add(encryptionRequest); 
				continue; 
			}
			if(sdu instanceof DownstreamPacketSDU) {
				encoder.send(((DownstreamPacketSDU)sdu).getPacket());
				continue; 
			}
		}
		
	}
	
	private synchronized void trace(String message) {
		if(this.logger == null) return; 
		this.logger.info(message);
	}
	
	private synchronized void trace(String message, Throwable cause) {
		if(this.logger == null) return; 
		this.logger.log(Level.WARNING, message, cause);
	}

}
