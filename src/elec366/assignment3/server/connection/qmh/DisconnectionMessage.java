package elec366.assignment3.server.connection.qmh;

public class DisconnectionMessage extends QueuedMessage {
	
	private final String reason; 
	
	public DisconnectionMessage(int id, String reason) {
		super(id); 
		this.reason = reason; 
	}
	
	public DisconnectionMessage(int id) {
		this(id, null); 
	}
	
	public String getReason() {
		return this.reason; 
	}

	@Override
	public String toString() {
		if(this.reason == null) return "DisconnectionMessage [connectionID=" + this.connectionID + "]";
		return "DisconnectionMessage [reason=" + this.reason + ", connectionID=" + this.connectionID + "]";
	}
	
}
