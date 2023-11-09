package elec366.assignment3.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
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
import elec366.assignment3.util.Pair;

public class ClientConnectionHandler {
	
	private final Logger logger; 
	
	private final String host; 
	private final int port; 
	
	private final PriorityBlockingQueue<UpstreamSDU> upstream; 
	private DownstreamSDUSupplier downstreamSender; 
	
	public ClientConnectionHandler(Logger logger, String host, int port) {
		this.logger = logger; 
		this.host = host; 
		this.port = port; 
		
		this.upstream = new PriorityBlockingQueue<>(); 
	}
	
	public PriorityBlockingQueue<UpstreamSDU> getUpstream() {
		return this.upstream; 
	}
	
	public void start() {
		
		try {
			this.startServer();
		}
		catch(IOException ex) {
			this.logger.log(Level.SEVERE, "Unable to start client.", ex); 
		}

	}
	
	public void send(DownstreamSDU sdu) {
		this.downstreamSender.accept(sdu);
	}
	
	private void startServer() throws UnknownHostException, IOException {
		
		String clientName = "client-" + ThreadLocalRandom.current().nextInt(1000000); 
		
		Socket socket = new Socket(this.host, this.port); 
		
		UpstreamSDUConsumer ups = new UpstreamSDUConsumer(this); 
		DownstreamSDUSupplier dns = new DownstreamSDUSupplier(); 
		Connection connection = new Connection(this.logger, clientName, socket, new Pair<>(dns, ups)); 
		
		connection.start();
		this.downstreamSender = dns; 
		this.upstream.put(new UpstreamConnectionSDU());
		
	}
	
	private static class UpstreamSDUConsumer implements Consumer<UpstreamSDU> {

		private final ClientConnectionHandler handler; 
		
		public UpstreamSDUConsumer(ClientConnectionHandler handler) {
			this.handler = handler; 
		}

		@Override
		public void accept(UpstreamSDU sdu) {
			if(sdu == null) return; 
			if(sdu instanceof UpstreamDisconnectionSDU) {
				this.handler.send(new DownstreamDisconnectSDU());
			}
			this.handler.upstream.put(sdu);
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
