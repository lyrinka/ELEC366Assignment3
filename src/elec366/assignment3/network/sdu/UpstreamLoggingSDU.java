package elec366.assignment3.network.sdu;

import java.util.logging.Level;
import java.util.logging.Logger;

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
	
	public UpstreamLoggingSDU() {
		this("A problem occured."); 
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
		return "UpstreamLoggingSDU [" + this.message + ", " + (this.cause != null ? "cause=" + this.cause : "") + "]";
	}

}
