package elec366.assignment3.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import elec366.assignment3.network.Connection;
import elec366.assignment3.network.sdu.DownstreamSDU;
import elec366.assignment3.network.sdu.UpstreamDisconnectionSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;
import elec366.assignment3.util.Pair;

public class ServerConnectionHandler {

	private static final String TAG = "ConnectionListener"; 
	
	private final Logger logger; 
	
	private final int port; 
	
	private final ConcurrentMap<Integer, DownstreamSDUSupplier> clientMap; 
	
	private final PriorityBlockingQueue<Pair<Integer, UpstreamSDU>> upstream; 
	
	public ServerConnectionHandler(Logger logger, int port) {
		this.logger = logger; 
		
		this.port = port; 
		this.clientMap = new ConcurrentHashMap<>(); 
		this.upstream = new PriorityBlockingQueue<Pair<Integer, UpstreamSDU>>(11, (a, b) -> a.getSecond().compareTo(b.getSecond())); 
	}
	
	public PriorityBlockingQueue<Pair<Integer, UpstreamSDU>> getUpstream() {
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
				this.logger.log(Level.SEVERE, "Unable to start server.", ex);
			}
			
		}, TAG); 
		
		serverThread.start(); 
		
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
		
		try (ServerSocket listener = new ServerSocket(this.port)) {
			
			int connectionID = 0; 
			
			while(true) {
				
				Socket socket = listener.accept(); 
				
				UpstreamSDUConsumer ups = new UpstreamSDUConsumer(this, connectionID); 
				DownstreamSDUSupplier dns = new DownstreamSDUSupplier(); 
				Connection connection = new Connection(this.logger, connectionID, socket, new Pair<>(dns, ups)); 
				
				connection.start();
				this.clientMap.put(connectionID, dns); 
				this.upstream.put(new Pair<>(connectionID, new UpstreamConnectionSDU()));
				
				connectionID++; 
				
			}
			
		}
		
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
			if(sdu instanceof UpstreamDisconnectionSDU) 
				this.handler.clientMap.remove(this.connectionID); 
			this.handler.upstream.put(new Pair<>(this.connectionID, sdu));
		}
		
	}
	
	private static class DownstreamSDUSupplier implements Consumer<DownstreamSDU>, Supplier<DownstreamSDU> {
		
		private final PriorityBlockingQueue<DownstreamSDU> downstream; 
		
		public DownstreamSDUSupplier() {
			this.downstream = new PriorityBlockingQueue<>(); 
		}
		
		@Override
		public void accept(DownstreamSDU sdu) {
			if(sdu == null) return; 
			this.downstream.put(sdu);
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
