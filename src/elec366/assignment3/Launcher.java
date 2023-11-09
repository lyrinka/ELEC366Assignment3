package elec366.assignment3;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.logging.Logger;

import elec366.assignment3.client.PacketClient;
import elec366.assignment3.protocol.packet.Packet.In;
import elec366.assignment3.protocol.packet.Packet.Out;
import elec366.assignment3.server.PacketServer;
import elec366.assignment3.util.LoggerUtil;

public class Launcher {

	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {
		
		Logger clientNetworkLogger = 	LoggerUtil.createLogger("ClientNetwork"); 
		Logger clientLogger = 			LoggerUtil.createLogger("Client"); 
		Logger serverNetworkLogger = 	LoggerUtil.createLogger("ServerNetwork"); 
		Logger serverLogger = 			LoggerUtil.createLogger("Server"); 
		
		PacketServer server = new PacketServer(serverLogger, serverNetworkLogger, 14567) {

			@Override
			public void onConnection(int id) {
				// TODO Auto-generated method stub
				this.getLogger().info("[S] Connected " + id); 
			}

			@Override
			public void onDisconnection(int id) {
				// TODO Auto-generated method stub
				this.getLogger().info("[S] Disconnected " + id); 
			}

			@Override
			public void onInboundPacket(int id, In packet) {
				// TODO Auto-generated method stub
				this.getLogger().info("[S] Inbound " + packet.toString()); 
			}
			
		}; 
		
		server.start();
		
		Thread.sleep(1000);
		
		PacketClient client1 = new PacketClient(clientLogger, clientNetworkLogger, "localhost", 14567) {

			@Override
			public void onConnection() {
				// TODO Auto-generated method stub
				this.getLogger().info("[C] Connected"); 
			}
			
			@Override
			public void onDisconnection() {
				this.getLogger().info("[C] Disconnected");
			}

			@Override
			public void onInboundPacket(Out packet) {
				// TODO Auto-generated method stub
				this.getLogger().info("[C] Inbound " + packet.toString()); 
			}
			
		}; 
		
		client1.start();
		
		Thread.sleep(5000);
		
		client1.disconnect();
		
	}

}
