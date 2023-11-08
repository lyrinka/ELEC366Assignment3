package elec366.assignment3.serverold.connection;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketDirection;
import elec366.assignment3.serverold.connection.qmh.ConnectionMessage;
import elec366.assignment3.serverold.connection.qmh.DisconnectionMessage;
import elec366.assignment3.serverold.connection.qmh.LoggingMessage;
import elec366.assignment3.serverold.connection.qmh.PacketMessage;
import elec366.assignment3.serverold.connection.qmh.QueuedMessage;

public abstract class ConnectionServer extends Thread {
	
	private static final String TAG = "ServerThread"; 
	private static final int POLLING_WAIT = 50; 
	
	private final Logger logger; 
	
	private final ConnectionHandler listener; 
	
	public ConnectionServer(int port) {
		super(TAG); 
		this.logger = ConnectionServer.createLogger(); 
		this.listener = new ConnectionHandler(port); 
	}
	
	private static Logger createLogger() {
		Logger logger = Logger.getLogger(TAG); 
		logger.setUseParentHandlers(false);
		logger.addHandler(new ConsoleHandler() {
			public ConsoleHandler delegate() {
				this.setFormatter(new Formatter() {
					private Date dt = new Date(); 
					private SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss"); 
					@Override
					public String format(LogRecord record) {
						StringBuilder sb = new StringBuilder(); 
						this.dt.setTime(record.getMillis()); 
						sb.append("[").append(this.sdf.format(this.dt)).append("] ["); 
						sb.append(record.getLevel()).append("] ").append(record.getMessage()).append("\n"); 
						return sb.toString(); 
					}
				});
				return this; 
			}
		}.delegate());
		return logger;
	}
	
	public Logger getLogger() {
		return this.logger; 
	}
	
	public void send(QueuedMessage message) {
		this.listener.send(message); 
	}
	
	public void sendPacket(int connectionID, Packet.Out packet) {
		this.send(new PacketMessage(connectionID, packet));
	}
	
	public void disconnect(int connectionID) {
		this.listener.disconnect(connectionID);
	}
	
	@Override
	public void run() {
		this.listener.start();
		ConcurrentLinkedQueue<QueuedMessage> upstream = this.listener.getUpstream(); 
		while(true) {
			QueuedMessage msg = upstream.poll(); 
			if(msg == null) {
				try {
					Thread.sleep(POLLING_WAIT);
				} catch (InterruptedException e) {
					return; 
				}
				continue; 
			}
			int id = msg.getConnectionID(); 
			if(msg instanceof ConnectionMessage) {
				this.onConnection(id, ((ConnectionMessage)msg).getReason());
			}
			if(msg instanceof DisconnectionMessage) {
				this.onDisconnection(id, ((DisconnectionMessage)msg).getReason());
			}
			if(msg instanceof LoggingMessage) {
				((LoggingMessage)msg).print(this.logger);
			}
			if(msg instanceof PacketMessage) {
				Packet packet = ((PacketMessage)msg).getPacket(); 
				if(packet.getDirection() == PacketDirection.IN)
					this.onInboundPacket(id, (Packet.In)packet);
				else {
					this.logger.warning("Received an outbound packet from client " + id + ". How is this possible?");
					this.logger.warning(packet.toString()); 
					this.disconnect(id);
				}
			}
		}
	}
	
	protected abstract void onConnection(int connectionID, String reason); 
	
	protected abstract void onDisconnection(int connectionID, String reason); 
	
	protected abstract void onInboundPacket(int connectionID, Packet.In packet); 
	
}
