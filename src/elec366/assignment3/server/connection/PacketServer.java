package elec366.assignment3.server.connection;

import java.util.concurrent.LinkedBlockingQueue;
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
import elec366.assignment3.server.sdu.UpstreamServerQuitSDU;
import elec366.assignment3.util.Pair;

public abstract class PacketServer {

	private static final String TAG = "PacketServer"; 
	
	private final Logger serverLogger; 
	private final Logger networkLogger; 

	protected final int port; 
	private ServerConnectionHandler connectionHandler;
	
	public PacketServer(Logger serverLogger, Logger networkLogger, int port) {
		this.serverLogger = serverLogger; 
		this.networkLogger = networkLogger;
		this.port = port; 
	}
	
	public void start() {
		
		this.connectionHandler = new ServerConnectionHandler(this.networkLogger, this.port);
		
		Thread serverThread = new Thread(this::runServer, TAG); 
		
		serverThread.start(); 
		
		this.connectionHandler.start(); 
		
	}
	
	public void shutdown() {
		this.connectionHandler.stop(); 
	}
	
	protected void runServer() {
		
		LinkedBlockingQueue<Pair<Integer, UpstreamSDU>> upstream = this.connectionHandler.getUpstream(); 
		
		try {
			
			while(true) {
				Pair<Integer, UpstreamSDU> sduPair;
				try {
					sduPair = upstream.take();
				} catch (InterruptedException e) {
					throw new RuntimeException(e); 
				} 
				int connectionID =  sduPair.getFirst(); 
				UpstreamSDU sdu = sduPair.getSecond(); 
				
				if(sdu instanceof UpstreamServerQuitSDU) {
					break; 
				}
				if(sdu instanceof UpstreamDisconnectionSDU) {
					this.onDisconnection(connectionID);
					continue; 
				}
				if(sdu instanceof UpstreamConnectionSDU) {
					this.onConnection(connectionID);
					continue; 
				}
				if(sdu instanceof UpstreamLoggingSDU) {
					if(this.serverLogger != null)
						((UpstreamLoggingSDU)sdu).logAsInfo(this.serverLogger);
					continue; 
				}
				if(sdu instanceof UpstreamPacketSDU) {
					Packet packet = ((UpstreamPacketSDU)sdu).getPacket(); 
					if(!(packet instanceof Packet.In)) {
						if(this.serverLogger != null)
							this.serverLogger.warning("[PacketServer] " + this.getClientName(connectionID) + " sent an outbound packet. How is this possible?"); 
						this.disconnect(connectionID);
						continue; 
					}
					this.onInboundPacket(connectionID, (Packet.In)packet);
					continue; 
				}
				
			}
			
		}
		catch(Throwable e) {
			this.shutdown();
			throw e; 
		}
		
		this.onServerQuit(); 
		
	}

	public abstract void onConnection(int id); 
	
	public abstract void onDisconnection(int id); 
	
	public abstract void onInboundPacket(int id, Packet.In packet); 
	
	public abstract void onServerQuit(); 
	
	public void setDecoderEncryption(int connectionID, StreamCipher cipher) {
		this.connectionHandler.send(connectionID, new DownstreamEncryptSDU(cipher, DownstreamEncryptSDU.Mode.ENCRYPT_DECODER));
	}
	
	public void setEncoderEncryption(int connectionID, StreamCipher cipher) {
		this.connectionHandler.send(connectionID, new DownstreamEncryptSDU(cipher, DownstreamEncryptSDU.Mode.ENCRYPT_ENCODER));
	}
	
	public void sendPacket(int connectionID, Packet.Out packet) {
		this.connectionHandler.send(connectionID, new DownstreamPacketSDU(packet)); 
	}
	
	public void disconnect(int connectionID) {
		this.connectionHandler.send(connectionID, new DownstreamDisconnectSDU());
	}
	
	public Logger getLogger() {
		return this.serverLogger; 
	}
	
	protected String getClientName(int connectionID) {
		return "Client " + connectionID; 
	}

}
