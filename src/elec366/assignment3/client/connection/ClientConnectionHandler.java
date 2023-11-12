package elec366.assignment3.client.connection;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Level;
import java.util.logging.Logger;

import elec366.assignment3.network.Connection;
import elec366.assignment3.network.sdu.DownstreamDisconnectSDU;
import elec366.assignment3.network.sdu.DownstreamSDU;
import elec366.assignment3.network.sdu.UpstreamAbnormalDisconnectionSDU;
import elec366.assignment3.network.sdu.UpstreamDisconnectionSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;
import elec366.assignment3.server.sdu.UpstreamConnectionSDU;
import elec366.assignment3.util.Pair;

public class ClientConnectionHandler {
	
	private final Logger logger; 
	
	private final String host; 
	private final int port; 
	
	private final LinkedBlockingQueue<UpstreamSDU> upstream; 
	private DownstreamSDUSupplier downstreamSender; 
	
	public ClientConnectionHandler(Logger logger, String host, int port) {
		this.logger = logger; 
		this.host = host; 
		this.port = port; 
		
		this.upstream = new LinkedBlockingQueue<>(); 
	}
	
	public LinkedBlockingQueue<UpstreamSDU> getUpstream() {
		return this.upstream; 
	}
	
	public void start() {
		
		try {
			this.startServer();
		}
		catch(IOException ex) {
			if(this.logger != null)
				this.logger.log(Level.SEVERE, "Unable to start client. Socket connection failed.", ex); 
			this.upstream.add(new UpstreamAbnormalDisconnectionSDU("Connection failed."));
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
		try {
			this.upstream.put(new UpstreamConnectionSDU());
		} catch (InterruptedException e) {
			throw new RuntimeException(e); 
		}
		
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
			try {
				this.handler.upstream.put(sdu);
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
