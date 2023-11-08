package elec366.assignment3.server.connection.qmh;

public abstract class QueuedMessage {
	
	protected final int connectionID; 
	
	public QueuedMessage(int connectionID) {
		this.connectionID = connectionID; 
	}
	
	public int getConnectionID() {
		return this.connectionID; 
	}
	
}