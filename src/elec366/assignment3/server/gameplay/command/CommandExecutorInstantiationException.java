package elec366.assignment3.server.gameplay.command;

/*
 * This exception applies to only server.
 * 
 * This exception is thrown when an internal Java reflection problem occurs
 * during the construction of a command executor object, mostly due to code problems. 
 */
public class CommandExecutorInstantiationException extends Exception {

	private static final long serialVersionUID = 4599850079180192893L;

	public CommandExecutorInstantiationException(Throwable cause) {
		super(cause);
	}
	
}
