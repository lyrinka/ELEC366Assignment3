package elec366.assignment3.network;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
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

	private static final String RX_TAG = "ConnectionWorker-%s-RX"; 
	private static final String TX_TAG = "ConnectionWorker-%s-TX"; 
	
	private final Logger logger;
	
	private final String name; 
	private final Socket socket; 
	private final Supplier<DownstreamSDU> downstream; 
	private final Consumer<UpstreamSDU> upstream; 
	
	private PacketEncoder encoder; 
	private PacketDecoder decoder; 
	
	public Connection(Logger logger, String name, Socket socket, Pair<Supplier<DownstreamSDU>, Consumer<UpstreamSDU>> channel) {
		this.logger = logger; 
		
		this.name = name; 
		this.socket = socket; 
		this.downstream = channel.getFirst(); 
		this.upstream = channel.getSecond(); 
	}
	
	public String getName() {
		return this.name;  
	}
	
	public void start() {
		
		this.encoder = new PacketEncoder(); 
		this.decoder = new PacketDecoder(); 
		
		Thread rxThread = new Thread(() -> {
			
			UpstreamDisconnectionSDU sdu; 
			
			try {
				this.traceRx("Starting RX..."); 
				this.runRx();
				sdu = new UpstreamDisconnectionSDU(); 
			}
			catch (IOException e) {
				sdu = new UpstreamDisconnectionSDU(); 
			}
			catch (PacketDecodeException | PayloadDeserializationException e) {
				sdu = new UpstreamDisconnectionSDU("Packet decoding failed.", e); 
			}
			
			if(sdu.getCause() == null) {
				this.traceRx(sdu.getReason()); 
			}
			else {
				this.traceRx(sdu.getReason(), sdu.getCause()); 
			}
			
			try {
				this.socket.close(); 
			}
			catch(IOException ignored) {
				this.traceRx("Exception while closing socket.", ignored); 
			}
			this.sendSDU(sdu); 
			this.traceRx("Thread termination."); 
			
		}, String.format(RX_TAG, this.name)); 
		
		Thread txThread = new Thread(() -> {
			
			try {
				this.traceTx("Starting TX..."); 
				this.runTx();
			} catch (IOException e) {
				// TODO: shall we ignore this?
				this.traceTx("IO exception in TX", e); 
			} catch (InterruptedException e) {
				this.traceTx("Interrupted", e); 
			}
			
			try {
				this.socket.close(); 
			}
			catch(IOException ignored) {
				this.traceRx("Exception while closing socket.", ignored); 
			}
			this.traceTx("Thread termination."); 
			
		}, String.format(TX_TAG, this.name)); 
		
		
		rxThread.start(); 
		txThread.start(); 
		
	}
	
	private void runRx() throws IOException, PacketDecodeException, PayloadDeserializationException {
		
		InputStream iStream = this.socket.getInputStream(); 
		
		while(true) {
			Packet packet = null; 
			while(packet == null) {
				int data = iStream.read(); 
				if(data < 0) {
					this.traceRx("Input stream drained (connection closed).");
					return; 
				}
				packet = this.decoder.accept((byte)data); 
			}
			this.traceRx("[Network] Received packet " + packet.toString());
			this.sendSDU(new UpstreamPacketSDU(packet)); 
		}
		
	}
	
	private void runTx() throws IOException, InterruptedException {
		
		OutputStream oStream = this.socket.getOutputStream(); 
		
		while(true) {
			DownstreamSDU sdu = this.receiveSDU(); 
			if(sdu == null) {
				this.traceTx("Queue fetch interrupted.");
				return; 
			}
			
			if(sdu instanceof DownstreamDisconnectSDU) return; 
			if(sdu instanceof DownstreamEncryptSDU) {
				DownstreamEncryptSDU encryptionRequest = (DownstreamEncryptSDU)sdu; 
				if(encryptionRequest.getMode() == DownstreamEncryptSDU.Mode.ENCRYPT_ENCODER) 
					this.encoder.attachCipher(encryptionRequest.getCipher());
				else
					// TODO: resolve synchronization issues here
					this.decoder.attachCipher(encryptionRequest.getCipher()); 
				continue; 
			}
			if(sdu instanceof DownstreamPacketSDU) {
				Packet packet = ((DownstreamPacketSDU)sdu).getPacket(); 
				this.encoder.send(oStream, packet);
				this.traceTx("[Network] Sent packet " + packet.toString());
				continue; 
			}
		}
		
	}
	
	private void sendSDU(UpstreamSDU sdu) {
		this.traceRx("[SDU] Worker sent SDU: " + sdu.toString());
		this.upstream.accept(sdu);
	}
	
	private DownstreamSDU receiveSDU() {
		DownstreamSDU sdu = this.downstream.get(); 
		if(sdu != null)
			this.traceTx("[SDU] Worker received SDU: " + sdu.toString());
		return sdu; 
	}
	
	private synchronized void traceRaw(String message) {
		if(this.logger == null) return; 
		this.logger.info(message);
	}
	
	private synchronized void traceRaw(String message, Throwable cause) {
		if(this.logger == null) return; 
		this.logger.log(Level.WARNING, message, cause);
		// TODO: do this properly
		cause.printStackTrace();
	}
	
	private void traceRx(String message) {
		this.traceRaw("[RX-" + this.name + "] " + message);
	}
	
	private void traceRx(String message, Throwable cause) {
		this.traceRaw("[RX-" + this.name + "] " + message, cause);
	}
	
	private void traceTx(String message) {
		this.traceRaw("[TX-" + this.name + "] " + message);
	}
	
	private void traceTx(String message, Throwable cause) {
		this.traceRaw("[TX-" + this.name + "] " + message, cause);
	}

}
