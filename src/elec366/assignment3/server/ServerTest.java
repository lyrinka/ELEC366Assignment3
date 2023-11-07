package elec366.assignment3.server;

import elec366.assignment3.server.gameplay.PacketServer;
import elec366.assignment3.server.gameplay.Player;

public class ServerTest {

	public static void comprehensiveTest() {
		
		(new PacketServer() {
			
			public PacketServer emulatedConsturctor() {
				return this; 
			}
			
			@Override
			protected void onPlayerUsernameConflict(Player player) {
				this.getLogger().info(player.getUsername() + " has disconnected: a player with the same username is already online!"); 
				player.sendMessage(R.USERNAME_CONFLICT);
			}

			@Override
			protected void onPlayerLogin(Player player) {
				this.getLogger().info(player.getUsername() + " has connected.");
				player.sendMessage("Welcome!");
			}

			@Override
			protected void onPlayerChat(Player player, String message) {
				this.getLogger().info(player.getUsername() + " sent chat: " + message);
				player.sendMessage("You have sent: " + message);
			}

			@Override
			protected void onPlayerQuit(Player player) {
				this.getLogger().info(player.getUsername() + " has disconnected.");
			}
			
		}).emulatedConsturctor().start(); 
		
	}

}
