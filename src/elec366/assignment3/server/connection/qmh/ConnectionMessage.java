package elec366.assignment3.server.connection.qmh;

public class ConnectionMessage extends QueuedMessage {
	
	private final String reason; 
	
	public ConnectionMessage(int id, String reason) {
		super(id); 
		this.reason = reason; 
	}
	
	public ConnectionMessage(int id) {
		this(id, null); 
	}
	
	public String getReason() {
		return this.reason; 
	}

	@Override
	public String toString() {
		if(this.reason == null) return "ConnectionMessage [connectionID=" + this.connectionID + "]";
		return "ConnectionMessage [reason=" + this.reason + ", connectionID=" + this.connectionID + "]";
	}
	
}
