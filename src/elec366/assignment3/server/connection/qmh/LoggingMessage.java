package elec366.assignment3.server.connection.qmh;

import java.util.logging.Logger;

public class LoggingMessage extends QueuedMessage {
	
	public static enum Severity {
		INFO, 
		WARN, 
		FATAL, 
	}
	
	private final Severity severity; 
	private final String message; 
	
	public LoggingMessage(int id, Severity severity, String message) {
		super(id); 
		this.severity = severity; 
		this.message = message; 
	}
	
	public LoggingMessage(int id, String message) {
		this(id, Severity.INFO, message); 
	}
	
	public Severity getSeverity() {
		return this.severity; 
	}
	
	public String getMessage() {
		return this.message; 
	}
	
	public void print(Logger logger) {
		switch(this.severity) {
			default:
			case INFO:
				logger.info(this.message);
				break;
			case WARN:
				logger.warning(this.message);
				break;
			case FATAL:
				logger.severe(this.message);
				break;
		}
	}

	@Override
	public String toString() {
		return "LoggingMessage [severity=" + this.severity + ", message=" + this.message + ", connectionID="
				+ this.connectionID + "]";
	}
	
}
