package elec366.assignment3;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import elec366.assignment3.client.connection.SecurePacketClient;
import elec366.assignment3.protocol.packet.Packet; 
import elec366.assignment3.protocol.packet.impl.PacketInLogin;
import elec366.assignment3.server.gameplay.MultiplayerServer;
import elec366.assignment3.server.gameplay.Player;
import elec366.assignment3.util.LoggerUtil;

public class Launcher {

	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {
		
		Logger clientNetworkLogger = 	LoggerUtil.createLogger("ClientNetwork"); 
		Logger clientLogger = 			LoggerUtil.createLogger("Client"); 
		Logger serverNetworkLogger = 	LoggerUtil.createLogger("ServerNetwork"); 
		Logger serverLogger = 			LoggerUtil.createLogger("Server"); 
		
		MultiplayerServer server = new MultiplayerServer(serverLogger, serverNetworkLogger, 14567) {

			@Override
			public void onPlayerNameConflict(Player player) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public boolean onPlayerPreLogin(Player player) {
				// TODO Auto-generated method stub
				return true;
			}

			@Override
			public void onPlayerLogin(Player player) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPlayerQuit(Player player) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPlayerChat(Player player, String message) {
				// TODO Auto-generated method stub
				
			}

		}; 
		
		server.start();
		
		Thread.sleep(2000);
		
		SecurePacketClient client1 = new SecurePacketClient(clientLogger, clientNetworkLogger, "localhost", 14567) {

			@Override
			public void onSecureConnection() {
				this.sendSecurePacket(new PacketInLogin("user"));
				
			}

			@Override
			public void onSecureDisconnection() {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onSecureOutboundPacket(Packet.Out packet) {
				// TODO Auto-generated method stub
				
			}
			
		}; 
		
		client1.start();
		
		Thread.sleep(5000);
		
		client1.disconnect();
		
	}

}
