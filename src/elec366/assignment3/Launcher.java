package elec366.assignment3;

import elec366.assignment3.client.app.gui.GraphicalClientLauncher;
import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.server.app.console.ConsoleServer;

public class Launcher {

	public static void main(String args[]) throws InterruptedException {
		
		if(args.length == 1) {
			(new ConsoleServer(14567)).start();  
		}
		else {
			(new GraphicalClientLauncher(new ConnectionInformation("localhost", 14567))).run();
		}
		
	}

}
