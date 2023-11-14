package elec366.assignment3.server.app.console;

import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.util.LoggerUtil;

/*
 * This class applies to only server.
 * 
 * This class launches a headless server that does not have any GUI. 
 * It is used to host our test server on Linux cloud servers. 
 */
public class ConsoleServer extends ChatServer {

	public ConsoleServer(int port, boolean verbose) {
		super(LoggerUtil.createLogger("Server"), verbose ? LoggerUtil.createLogger("Tracer") : null, port); 
	}

}
