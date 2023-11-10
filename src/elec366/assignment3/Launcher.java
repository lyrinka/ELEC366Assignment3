package elec366.assignment3;

import elec366.assignment3.client.app.console.ConsoleClient;
import elec366.assignment3.server.app.console.ConsoleServer;

public class Launcher {

	public static void main(String args[]) {
		if (args.length == 0) {
			ConsoleServer server = new ConsoleServer();
			server.start();
		} else if (args.length == 1) {
			ConsoleClient client = new ConsoleClient(args[0]);
			client.start();
		}
	}

}
