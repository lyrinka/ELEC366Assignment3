package elec366.assignment3.server.connection;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;

import elec366.assignment3.network.codec.PacketDecoder;
import elec366.assignment3.network.codec.PacketEncoder;
import elec366.assignment3.network.codec.exception.PacketDecodeException;
import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.serdes.exception.PayloadDeserializationException;
import elec366.assignment3.server.connection.qmh.ConnectionMessage;
import elec366.assignment3.server.connection.qmh.DisconnectionMessage;
import elec366.assignment3.server.connection.qmh.EncryptionRequestMessage;
import elec366.assignment3.server.connection.qmh.PacketMessage;
import elec366.assignment3.server.connection.qmh.QueuedMessage;

public class ConnectionWorker extends Thread {
	
	private static final int POLLING_WAIT = 50; 
	private static final String TAG = "WorkerThread-"; 
	
	private final int connectionID; 
	private final Socket socket; 
	private boolean disconnected; 
	
	private final ConcurrentLinkedQueue<QueuedMessage> upstream; 
	private final ConcurrentLinkedQueue<QueuedMessage> downstream; 
	
	private final Consumer<Integer> disconnectionCallback; 
	
	public ConnectionWorker(int connectionID, Socket socket, ConcurrentLinkedQueue<QueuedMessage> upstream, Consumer<Integer> onDisconnect) {
		super(TAG + connectionID); 
		this.connectionID = connectionID; 
		this.socket = socket; 
		this.disconnected = false; 
		
		this.upstream = upstream; 
		this.downstream = new ConcurrentLinkedQueue<>(); 
		
		this.disconnectionCallback = onDisconnect; 
	}
	
	private synchronized void setDisconnected(boolean disconnected) {
		this.disconnected = disconnected; 
	}
	
	public synchronized boolean isDisconnected() {
		return this.disconnected; 
	}
	
	public void send(QueuedMessage message) {
		if(this.isDisconnected()) return; 
		this.downstream.add(message); 
	}
	
	public void fastDisconnect() {
		if(this.isDisconnected()) return; 
		this.downstream.clear(); 
		this.send(new DisconnectionMessage(this.connectionID)); 
	}
	
	@Override
	public void run() {
		try {
			this.runWrapper(); 
			this.upstream.add(new DisconnectionMessage(this.connectionID)); 
		}
		catch(IOException ex) {
			this.upstream.add(new DisconnectionMessage(this.connectionID, "The remote peer has disconnected.")); 
		}
		catch(PacketDecodeException | PayloadDeserializationException ex) {
			this.upstream.add(new DisconnectionMessage(this.connectionID, "Decoding exception: " + ex.getMessage())); 
			ex.printStackTrace(); 
		}
		catch(InterruptedException ex) {
			this.upstream.add(new DisconnectionMessage(this.connectionID, "Thread sleep interrupted.")); 
		}
		try {
			this.socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.setDisconnected(true);
		this.disconnectionCallback.accept(this.connectionID); 
	}
	
	private void runWrapper() throws IOException, PacketDecodeException, PayloadDeserializationException, InterruptedException {
		String clientAddress = this.socket.getInetAddress().toString() + ":" + this.socket.getPort(); 
		this.upstream.add(new ConnectionMessage(this.connectionID, "Inbound connection from " + clientAddress)); 
		
		OutputStream oStream = this.socket.getOutputStream(); 
		InputStream iStream = this.socket.getInputStream(); 
		
		PacketEncoder encoder = new PacketEncoder(oStream); 
		PacketDecoder decoder = new PacketDecoder(); 
		
		this.socket.setSoTimeout(POLLING_WAIT);
		
		while(true) {
			// Handle Tx
			boolean disconnect = false; 
			while(true) {
				QueuedMessage txMessage = this.downstream.poll(); 
				if(txMessage == null) break; 
				if(txMessage.getConnectionID() != this.connectionID) continue; 
				if(txMessage instanceof DisconnectionMessage) {
					disconnect = true; 
					break; 
				}
				if(txMessage instanceof EncryptionRequestMessage) {
					encoder.attachCipher(((EncryptionRequestMessage)txMessage).getEncoderCipher());
					decoder.attachCipher(((EncryptionRequestMessage)txMessage).getDecoderCipher());
				}
				if(txMessage instanceof PacketMessage) {
					encoder.send(((PacketMessage)txMessage).getPacket());
				}
			}
			if(disconnect == true) break; 
			
			// Handle Rx
			while(true) {
				int data; 
				try {
					data = iStream.read(); 
				}
				catch(SocketTimeoutException ex) {
					break; 
				}
				if(data < 0) {
					disconnect = true; 
					break; 
				}
				Packet rxPacket = decoder.accept((byte)data); 
				if(rxPacket == null) continue; 
				this.upstream.add(new PacketMessage(this.connectionID, rxPacket)); 
				break; 
			}
			if(disconnect == true) break; 
		}
	}
	
	
}
