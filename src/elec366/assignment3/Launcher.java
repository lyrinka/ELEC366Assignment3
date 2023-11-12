package elec366.assignment3;

import elec366.assignment3.client.app.gui.GraphicalClientLauncher;
import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.server.app.gui.GraphicalServerLauncher;

public class Launcher {

	public static void main(String args[]) throws InterruptedException {
		
		ConnectionInformation defaultConnectionInformation = 
				new ConnectionInformation(
						"localhost", 
						14567
		); 
		
		Runnable runnable; 
		
		if(args.length == 1) {
			runnable = new GraphicalServerLauncher(defaultConnectionInformation.getPort()); 
		}
		else {
			runnable = new GraphicalClientLauncher(defaultConnectionInformation); 
		}
		
		runnable.run();
		
	}

}
