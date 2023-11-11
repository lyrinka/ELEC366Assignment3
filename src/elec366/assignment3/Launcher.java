package elec366.assignment3;

import elec366.assignment3.client.app.gui.frontend.ClientGUI;
import elec366.assignment3.client.app.gui.frontend.IClientGUI;

public class Launcher {

	public static void main(String args[]) {
		
		IClientGUI clientGUI = new ClientGUI(); 
		
		clientGUI.onConnectionButton(() -> clientGUI.displayDialog("Debug", "Connection button clicked!"));
		clientGUI.onSendButton(() -> {
			
		});
		
		clientGUI.showUI(); 
		
	}

}
