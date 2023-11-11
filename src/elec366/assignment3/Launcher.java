package elec366.assignment3;

import elec366.assignment3.server.app.gui.frontend.IServerGUI;
import elec366.assignment3.server.app.gui.frontend.ServerGUI;

public class Launcher {

	public static void main(String args[]) throws InterruptedException {
		
		IServerGUI serverGUI = new ServerGUI(); 
		serverGUI.showUI();
	}

}
