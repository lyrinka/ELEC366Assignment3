package elec366.assignment3.server.app.console;

import elec366.assignment3.server.gameplay.ChatServer;
import elec366.assignment3.util.LoggerUtil;

public class ConsoleServer extends ChatServer {

	public ConsoleServer() {
		super(LoggerUtil.createLogger("Server"), LoggerUtil.createLogger("Tracer"), 14567); 
	}

}
