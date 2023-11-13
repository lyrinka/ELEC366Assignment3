package elec366.assignment3.server.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import elec366.assignment3.network.Connection;
import elec366.assignment3.network.sdu.DownstreamDisconnectSDU;
import elec366.assignment3.network.sdu.DownstreamSDU;
import elec366.assignment3.network.sdu.UpstreamDisconnectionSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;
import elec366.assignment3.server.sdu.UpstreamConnectionSDU;
import elec366.assignment3.server.sdu.UpstreamServerQuitSDU;
import elec366.assignment3.util.Flag;
import elec366.assignment3.util.Pair;

public class ServerConnectionHandler {

	private static final String TAG = "ServerConnectionHandler"; 
	
	private final Logger logger; 
	
	private final int port; 
	
	private final ConcurrentMap<Integer, DownstreamSDUSupplier> clientMap; 
	
	private final LinkedBlockingQueue<Pair<Integer, UpstreamSDU>> upstream; 
	
	private Runnable closureCallback; 
	
	public ServerConnectionHandler(Logger logger, int port) {
		this.logger = logger; 
		
		this.port = port; 
		this.clientMap = new ConcurrentHashMap<>(); 
		this.upstream = new LinkedBlockingQueue<Pair<Integer, UpstreamSDU>>(); 
	}
	
	public LinkedBlockingQueue<Pair<Integer, UpstreamSDU>> getUpstream() {
		return this.upstream; 
	}
	
	public int getClientCount() {
		return this.clientMap.size(); 
	}
	
	public ArrayList<Integer> getClientList() {
		return new ArrayList<Integer>(this.clientMap.keySet()); 
	}
	
	public void start() {
		
		Thread serverThread = new Thread(() -> {
			
			try {
				this.runServer();
			}
			catch(IOException ex) {
				if(this.logger != null)
					this.logger.log(Level.SEVERE, "Unable to start server.", ex);
				this.stopUpstream(); 
			}
			
		}, TAG); 
		
		serverThread.start(); 
		
	}
	
	public void stop() {
		if(this.closureCallback != null)
			this.closureCallback.run(); 
	}
	
	public void send(int connectionID, DownstreamSDU sdu) {
		DownstreamSDUSupplier dns = this.clientMap.get(connectionID); 
		if(dns == null) return; 
		dns.accept(sdu);
	}
	
	public void send(Pair<Integer, DownstreamSDU> pair) {
		this.send(pair.getFirst(), pair.getSecond());
	}
	
	private void runServer() throws IOException {
		
		Flag closureFlag = new Flag(); 
		
		try (ServerSocket listener = new ServerSocket(this.port)) {
			
			this.closureCallback = () -> {
				try {
					listener.close();
					closureFlag.setFlag(true);
				} catch (IOException ignored) {
					
				} 
			}; 
			
			if(this.logger != null)
				this.logger.info("[ServerConnection] Started listener on port " + this.port + ".");
			
			int connectionID = 0; 
			
			while(true) {
				
				Socket socket; 
				try {
					socket = listener.accept(); 
				}
				catch(IOException ex) {
					if(ex instanceof SocketException && closureFlag.getFlag())
						break; 
					throw ex; 
				}
				
				UpstreamSDUConsumer ups = new UpstreamSDUConsumer(this, connectionID); 
				DownstreamSDUSupplier dns = new DownstreamSDUSupplier(); 
				Connection connection = new Connection(this.logger, Integer.toString(connectionID), socket, new Pair<>(dns, ups)); 
				
				connection.start();
				this.clientMap.put(connectionID, dns); 
				try {
					this.upstream.put(new Pair<>(connectionID, new UpstreamConnectionSDU()));
				} catch (InterruptedException e) {
					throw new RuntimeException(e); 
				}
				
				connectionID++; 
				
			}
			
			listener.close();
			
			this.clientMap.values().forEach(c -> c.accept(new DownstreamDisconnectSDU()));
			
			if(this.logger != null)
				this.logger.info("[ServerConnection] Server listener closed.");
			
			this.stopUpstream(); 
			
		}
		
	}
	
	private void stopUpstream() {
		this.upstream.add(new Pair<>(-1, new UpstreamServerQuitSDU())); 
	}
	
	private static class UpstreamSDUConsumer implements Consumer<UpstreamSDU> {

		private final ServerConnectionHandler handler; 
		private final int connectionID; 
		
		public UpstreamSDUConsumer(ServerConnectionHandler handler, int connectionID) {
			this.handler = handler; 
			this.connectionID = connectionID;
		}

		@Override
		public void accept(UpstreamSDU sdu) {
			if(sdu == null) return; 
			if(sdu instanceof UpstreamDisconnectionSDU) {
				this.handler.send(new Pair<>(this.connectionID, new DownstreamDisconnectSDU()));
				this.handler.clientMap.remove(this.connectionID); 
			}
			try {
				this.handler.upstream.put(new Pair<>(this.connectionID, sdu));
			} catch (InterruptedException e) {
				throw new RuntimeException(e); 
			}
		}
		
	}
	
	private static class DownstreamSDUSupplier implements Consumer<DownstreamSDU>, Supplier<DownstreamSDU> {
		
		private final LinkedBlockingQueue<DownstreamSDU> downstream; 
		
		public DownstreamSDUSupplier() {
			this.downstream = new LinkedBlockingQueue<>(); 
		}
		
		@Override
		public void accept(DownstreamSDU sdu) {
			if(sdu == null) return; 
			try {
				this.downstream.put(sdu);
			} catch (InterruptedException e) {
				throw new RuntimeException(e); 
			}
		}
		
		@Override
		public DownstreamSDU get() {
			try {
				return this.downstream.take();
			} catch (InterruptedException e) {
				return null; 
			} 
		}
		
	}

}
