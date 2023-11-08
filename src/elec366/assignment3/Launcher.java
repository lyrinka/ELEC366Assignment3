package elec366.assignment3;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

import elec366.assignment3.network.Connection;
import elec366.assignment3.network.sdu.DownstreamDisconnectSDU;
import elec366.assignment3.network.sdu.DownstreamSDU;
import elec366.assignment3.network.sdu.UpstreamSDU;
import elec366.assignment3.server.ServerConnectionHandler;
import elec366.assignment3.util.LoggerUtil;
import elec366.assignment3.util.Pair;

public class Launcher {

	public static void main(String args[]) throws UnknownHostException, IOException, InterruptedException {
		
		Logger logger = LoggerUtil.createLogger("Logger"); 
		
		ServerConnectionHandler sch = new ServerConnectionHandler(logger, 14567); 
		sch.start();
		
		Thread.sleep(1000);
		
		Socket socket1 = new Socket("localhost", 14567); 
		
		Connection con1 = new Connection(logger, "con1", socket1, new Pair<Supplier<DownstreamSDU>, Consumer<UpstreamSDU>>(
				() -> {
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} 
					return new DownstreamDisconnectSDU();
				}, 
				(sdu) -> {
					System.out.println(sdu.toString()); 
				}
		)); 
		con1.start();
		Thread.sleep(1000);
		socket1.close();
		
	}

}
