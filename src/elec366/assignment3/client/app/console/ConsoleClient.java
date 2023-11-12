package elec366.assignment3.client.app.console;

import java.util.Arrays;
import java.util.Scanner;

import elec366.assignment3.client.app.type.ConnectionInformation;
import elec366.assignment3.client.gameplay.ChatClient;
import elec366.assignment3.type.ChatMessageType;
import elec366.assignment3.util.LoggerUtil;

public class ConsoleClient extends ChatClient {

	private final ConsoleInputThread consoleInputThread;

	public ConsoleClient(ConnectionInformation conn, boolean verbose, String username) {
		super(LoggerUtil.createLogger("Client"), verbose ? LoggerUtil.createLogger("Tracer") : null, conn.getHost(), conn.getPort(), username);
		this.consoleInputThread = new ConsoleInputThread(this); 
	}

	@Override
	public void onChatServerConnection() {
		System.out.println("> Connected");
		this.consoleInputThread.start();
	}

	@Override
	public void onInboundChatMessage(ChatMessageType type, String message) {
		System.out.println(message);
	}

	@Override
	public void onPlayerListUpdate(String[] players) {
		System.out.println("> Received updated playerlist: " + Arrays.toString(players));
	}

	@Override
	public void onChatServerDisconnection() {
		System.out.println("> Disconnected");
		this.consoleInputThread.exit();
	}
	
	@Override
	public void sendChatMessage(String message) {
		if(message.equals("/logout")) {
			this.disconnect(); 
			return; 
		}
		super.sendChatMessage(message);
	}

	private static class ConsoleInputThread extends Thread {
		
		private final ConsoleClient client; 
		
		private boolean exit; 
		
		public ConsoleInputThread(ConsoleClient client) {
			this.client = client; 
			this.exit = false; 
		}
		
		@Override
		public void run() {
			
			try (Scanner scanner = new Scanner(System.in)) {
				while (true) {
					if(!scanner.hasNextLine()) {
						try {
							if(this.exit) break; 
							Thread.sleep(50);
						} catch (InterruptedException e) {
							break; 
						}
						continue; 
					}
					String message = scanner.nextLine();
					this.client.sendChatMessage(message);
				}
			}
			this.client.disconnect();
		}
		
		public void exit() {
			this.exit = true; 
		}
		
	}
	
}
