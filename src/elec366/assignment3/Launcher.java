package elec366.assignment3;

import elec366.assignment3.client.app.gui.frontend.ClientGUI;
import elec366.assignment3.client.app.gui.frontend.IClientGUI;

public class Launcher {

	public static void main(String args[]) {
		
		IClientGUI clientGUI = new ClientGUI(); 

		clientGUI.onConnectionButton(() -> {
			clientGUI.setState(IClientGUI.State.CONNECTING); 
			new Thread(() -> {
				try {
					Thread.sleep(1000);
					clientGUI.setState(IClientGUI.State.CONNECTED); 
					Thread.sleep(5000); 
					clientGUI.setState(IClientGUI.State.DISCONNECTED); 
				} catch (InterruptedException e) {
					e.printStackTrace();
				} 
			}).start(); 
		});

		clientGUI.showUI(); 
		
	}

}
