package elec366.assignment3.client.app.console;

import java.util.Arrays;
import java.util.Scanner;

import elec366.assignment3.client.gameplay.ChatClient;
import elec366.assignment3.type.ChatMessageType;
import elec366.assignment3.util.LoggerUtil;

public class ConsoleClient extends ChatClient {

	private Thread consoleInputThread;

	public ConsoleClient(String username) {
		super(LoggerUtil.createLogger("Client"), LoggerUtil.createLogger("Tracer"), "localhost", 14567, username);
	}

	@Override
	public void onChatServerConnection() {
		System.out.println("> Connected");
		this.startConsoleInputThread();
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
		// TODO: terminate scanner thread properly?
//		if (this.consoleInputThread != null)
//			this.consoleInputThread.interrupt();
	}

	private void startConsoleInputThread() {
		this.consoleInputThread = new Thread(this::runConsoleInputThread);
		this.consoleInputThread.start();
	}

	private void runConsoleInputThread() {
		try (Scanner scanner = new Scanner(System.in)) {
			while (true) {
				String message = scanner.nextLine();
				if (message.equals("/exit"))
					break;
				this.sendChatMessage(message);
			}
		}
		this.disconnect();
	}
	
}
