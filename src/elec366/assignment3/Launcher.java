package elec366.assignment3;

import elec366.assignment3.client.app.gui.frontend.ClientGUI;
import elec366.assignment3.client.app.gui.frontend.IClientGUI;
import elec366.assignment3.client.app.gui.frontend.IClientGUI.State;

public class Launcher {

	public static void main(String args[]) throws InterruptedException {
		
		IClientGUI clientGUI = new ClientGUI(); 

		clientGUI.showUI(); 
		clientGUI.setState(State.CONNECTED);
		
		clientGUI.setOnlinePlayers(new String[] {"aaa", "bbb", "ccc", "ddd"}); 
		
		Thread.sleep(2000);
		
		clientGUI.setOnlinePlayers(new String[] {"ccc", "ddd", "eee", "fff"}); 
		clientGUI.appendChat("updated");
		
		Thread.sleep(2000);
		
		clientGUI.clearChat();
		clientGUI.setOnlinePlayers(new String[0]);
		
	}

}
