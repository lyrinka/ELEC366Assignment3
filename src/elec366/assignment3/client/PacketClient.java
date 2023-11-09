package elec366.assignment3.client;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.logging.Logger;

import elec366.assignment3.network.sdu.DownstreamDisconnectSDU;
import elec366.assignment3.network.sdu.DownstreamEncryptSDU;
import elec366.assignment3.network.sdu.DownstreamPacketSDU;
import elec366.assignment3.network.sdu.UpstreamDisconnectionSDU;
import elec366.assignment3.network.sdu.UpstreamLoggingSDU;
import elec366.assignment3.network.sdu.UpstreamPacketSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;
import elec366.assignment3.protocol.crypto.StreamCipher;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.server.sdu.UpstreamConnectionSDU;

public abstract class PacketClient {
	
	private static final String TAG = "PacketClient"; 
	
	private final Logger clientLogger;
	private final Logger networkLogger;

	protected final String host;
	protected final int port;
	private ClientConnectionHandler connectionHandler; 

	public PacketClient(Logger serverLogger, Logger networkLogger, String host, int port) {
		this.networkLogger = networkLogger;
		this.clientLogger = serverLogger;
		this.host = host;
		this.port = port;
	}
	
	public void start() {
		
		this.connectionHandler = new ClientConnectionHandler(this.networkLogger, this.host, this.port); 
		
		Thread clientThread = new Thread(this::runClient, TAG); 
		
		clientThread.start(); 
		
		this.connectionHandler.start();
		
	}
	
	private void runClient() {

		PriorityBlockingQueue<UpstreamSDU> upstream = this.connectionHandler.getUpstream(); 
		
		while(true) {
			UpstreamSDU sdu;
			try {
				sdu = upstream.take();
			} catch (InterruptedException e) {
				throw new RuntimeException(e); 
			} 
			
			if(sdu instanceof UpstreamDisconnectionSDU) {
				this.onDisconnection(); 
				return; 
			}
			if(sdu instanceof UpstreamConnectionSDU) {
				this.onConnection();
				continue; 
			}
			if(sdu instanceof UpstreamLoggingSDU) {
				if(this.clientLogger != null)
					((UpstreamLoggingSDU)sdu).logAsWarning(this.clientLogger);
				continue; 
			}
			if(sdu instanceof UpstreamPacketSDU) {
				Packet packet = ((UpstreamPacketSDU)sdu).getPacket(); 
				if(!(packet instanceof Packet.Out)) {
					if(this.clientLogger != null)
						this.clientLogger.warning("[PacketClient] " + "Server sent an inbound packet. How is this possible?"); 
					this.disconnect(); 
					continue; 
				}
				this.onOutboundPacket((Packet.Out)packet); 
				continue; 
			}
		}

	}
	
	public abstract void onConnection(); 
	
	public abstract void onDisconnection(); 
	
	public abstract void onOutboundPacket(Packet.Out packet); 

	public void setDecoderEncryption(StreamCipher cipher) {
		this.connectionHandler.send(new DownstreamEncryptSDU(cipher, DownstreamEncryptSDU.Mode.ENCRYPT_DECODER));
	}
	
	public void setEncoderEncryption(StreamCipher cipher) {
		this.connectionHandler.send(new DownstreamEncryptSDU(cipher, DownstreamEncryptSDU.Mode.ENCRYPT_ENCODER));
	}
	
	public void sendPacket(Packet.In packet) {
		this.connectionHandler.send(new DownstreamPacketSDU(packet));
	}
	
	public void disconnect() {
		this.connectionHandler.send(new DownstreamDisconnectSDU());
	}
	
	public Logger getLogger() {
		return this.clientLogger; 
	}

}
