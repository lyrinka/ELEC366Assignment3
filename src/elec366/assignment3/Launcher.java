package elec366.assignment3;

import java.util.function.Consumer;

import elec366.assignment3.client.app.console.ConsoleClient;
import elec366.assignment3.client.app.gui.GraphicalClientLauncher;
import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.server.ServerSettings;
import elec366.assignment3.server.app.console.ConsoleServer;
import elec366.assignment3.server.app.gui.GraphicalServerLauncher;
import elec366.assignment3.util.ArrayIterator;

public class Launcher {

	public static void main(String args[]) throws InterruptedException {
		
		final ConnectionInformation conn = 
				new ConnectionInformation(
						"localhost", 
						14567
		); 
		
		ArrayIterator<String> it = new ArrayIterator<>(args); 
		
		Consumer<Boolean> entry = null; 
		boolean verbose = false; 
		
		while(it.hasNext()) {
			String arg = it.next(); 
			switch(arg) {
				case "-server": 
				case "-s": {
					entry = v -> new GraphicalServerLauncher(conn.getPort(), v).run(); 
					break; 
				}
				case "-client": 
				case "-c": {
					entry = v -> new GraphicalClientLauncher(conn, v).run(); 
					break; 
				}
				case "-console-server": 
				case "-S": {
					entry = v -> new ConsoleServer(conn.getPort(), v).start(); 
					break; 
				}
				case "-console-client": 
				case "-C": {
					String username = it.next(); 
					if(username == null) return; 
					entry = v -> new ConsoleClient(conn, v, username).start();
					break; 
				}
				case "-verbose": 
				case "-V": {
					verbose = true; 
					break; 
				}
				case "-host": 
				case "-H": {
					String host = it.next(); 
					if(host == null) return; 
					conn.setHost(host);
					break; 
				}
				case "-port": 
				case "-P": {
					String port0 = it.next(); 
					if(port0 == null) return; 
					int port; 
					try {
						port = Integer.parseInt(port0); 
					}
					catch(NumberFormatException ignored) {
						return; 
					}
					conn.setPort(port);
					break; 
				}
				case "-stop-pass": 
				case "-SP": {
					String pass = it.next(); 
					if(pass == null) return; 
					ServerSettings.STOP_PASSWORD = pass; 
					break; 
				}
			}
		}
		
		if(entry == null) entry = v -> new GraphicalClientLauncher(conn, v).run(); 
		entry.accept(verbose);
		
	}

}
