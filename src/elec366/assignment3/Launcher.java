package elec366.assignment3;

import elec366.assignment3.consoleclient.ConsoleClient;
import elec366.assignment3.server.gameplay.ChatServer;

public class Launcher {

	public static void main(String args[]) {
		if(args.length == 1) {
			ConsoleClient.entry(args[0]); 
		}
		if(args.length == 0) {
			new ChatServer().start();
		}
	}

}
