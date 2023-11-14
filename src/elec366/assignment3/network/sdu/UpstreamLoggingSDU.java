package elec366.assignment3.network.sdu;

import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * This class is common to server and client. 
 * 
 * This SDU and its subclasses are sent by the connection worker
 * when the worker has a message and/or an exception to inform
 * the upper layers.
 * 
 * This SDU has sub-classes. 
 */
public class UpstreamLoggingSDU extends UpstreamSDU {

	private final String message; 
	private final Throwable cause;
	
	public UpstreamLoggingSDU(String message, Throwable cause) {
		super();
		this.message = message;
		this.cause = cause;
	} 
	
	public UpstreamLoggingSDU(String reason) {
		this(reason, null); 
	}
	
	public UpstreamLoggingSDU(Throwable cause) {
		this("Exception.", cause); 
	}

	public String getReason() {
		return this.message;
	}

	public Throwable getCause() {
		return this.cause;
	}
	
	public void print() {
		System.out.println(this.message); 
		if(this.cause != null)
			this.cause.printStackTrace();
	}
	
	public void log(Level level, Logger logger) {
		if(this.cause == null) {
			logger.log(level, this.message);
		}
		else {
			logger.log(level, this.message, this.cause);
		}
	}
	
	public void logAsInfo(Logger logger) {
		this.log(Level.INFO, logger);
	}
	
	public void logAsWarning(Logger logger) {
		this.log(Level.WARNING, logger);
	}
	
	public void logAsSevere(Logger logger) {
		this.log(Level.SEVERE, logger);
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName() + " [" + this.message + (this.cause != null ? ", cause= " + this.cause : "") + "]";
	}

}
