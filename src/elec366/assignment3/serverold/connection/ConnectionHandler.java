package elec366.assignment3.serverold.connection;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import elec366.assignment3.serverold.connection.qmh.QueuedMessage;

public class ConnectionHandler extends Thread {
	
	private static final String TAG = "ListenerThread"; 
	
	private final int port; 
	
	private final ConcurrentHashMap<Integer, ConnectionWorker> workerMap; 
	
	private final ConcurrentLinkedQueue<QueuedMessage> upstream; 
	
	private ServerSocket listener; 
	
	public ConnectionHandler(int port) {
		super(TAG); 
		this.port = port; 
		this.workerMap = new ConcurrentHashMap<>(); 
		this.upstream = new ConcurrentLinkedQueue<>(); 
	}
	
	public void send(QueuedMessage message) {
		ConnectionWorker worker = this.workerMap.get(message.getConnectionID()); 
		if(worker == null) return; 
		worker.send(message);
	}
	
	public void disconnect(int connectionID) {
		ConnectionWorker worker = this.workerMap.get(connectionID); 
		if(worker == null) return; 
		worker.fastDisconnect(); 
	}
	
	public ConcurrentLinkedQueue<QueuedMessage> getUpstream() {
		return this.upstream; 
	}

	@Override
	public void run() {
		try {
			this.runWrapper();
			this.listener.close();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				this.listener.close();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		}
	}
	
	private void runWrapper() throws IOException {
		this.listener = new ServerSocket(this.port); 
		int connectionID = 0; 
		while(true) {
			Socket socket = this.listener.accept(); 
			ConnectionWorker worker = new ConnectionWorker(connectionID, socket, this.upstream, this::disconnectionHandler); 
			this.workerMap.put(connectionID++, worker); 
			worker.start(); 
		}
	}
	
	private void disconnectionHandler(int connectionID) {
		ConnectionWorker worker = this.workerMap.remove(connectionID); 
		if(worker == null) return; 
		try {
			worker.join();
		} catch (InterruptedException e) {
			
		}
	}
	
}
