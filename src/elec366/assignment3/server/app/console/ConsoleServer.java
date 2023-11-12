package elec366.assignment3.server.app.console;

import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.util.LoggerUtil;

public class ConsoleServer extends ChatServer {

	public ConsoleServer(int port, boolean verbose) {
		super(LoggerUtil.createLogger("Server"), verbose ? LoggerUtil.createLogger("Tracer") : null, port); 
	}

}
