package elec366.assignment3;

import java.util.function.Consumer;

import elec366.assignment3.client.app.console.ConsoleClient;
import elec366.assignment3.client.app.gui.GraphicalClientLauncher;
import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.server.ServerSettings;
import elec366.assignment3.server.app.console.ConsoleServer;
import elec366.assignment3.server.app.gui.GraphicalServerLauncher;
import elec366.assignment3.util.ArrayIterator;

/*
 * This is the application launcher. 
 * Methods of launching:
 * 
 * - Double clicking the jar file launches the client UI.
 * - Launching directly from Eclipse launches the client UI. 
 * - Setting defaultLaunchServer to TRUE, launching directly from Eclipse launches the server UI. 
 * 
 * Command line options:
 * -server, -s: 			launches the server GUI. 
 * -client, -c: 			launches the client GUI. 
 * -console-server, -S: 	launches a headless server. 
 * -console-client, -C: 	launches a headless client.
 * -verbose, -V:			enable additional logs.
 * -host, -H <host>:		sets the default host for client
 * -port, -P <port>:		sets the port for server, or the default port for client
 * -stop-password, -SP <pass>: sets the stop password. without the stop password, a client cannot stop the server with the /stop command.
 * 
 * 
 * Navigation:
 * - GUI implementation: 				elec366.assignment3.client.app.gui.*
 * 						 				elec366.assignment3.server.app.gui.*
 * 
 * - Connection handling and threading:	elec366.assignment3.network.Connection.java
 * 										elec366.assignment3.client.connection.*
 * 										elec366.assignment3.client.gameplay.*
 * 										elec366.assignment3.server.connection.*
 * 										elec366.assignment3.server.gameplay.*
 * 
 * - Packet handling:					elec366.assignment3.protocol.*
 * 
 * 
 * Asides from GUI bonus implementations, this application features secure messaging by RSA and AES encryption.
 * 
 */
public class Launcher {

	private static final boolean defaultLaunchServer = false; 
	
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
		
		if(entry == null) {
			if(!defaultLaunchServer)
				entry = v -> new GraphicalClientLauncher(conn, v).run(); 
			else 
				entry = v -> new GraphicalServerLauncher(conn.getPort(), v).run(); 
		}
		
		entry.accept(verbose);
		
	}

}
