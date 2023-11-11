package elec366.assignment3.client.app.type;

public class ConnectionInformation {

	private final String host; 
	private final int port;
	
	public ConnectionInformation(String host, int port) {
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return this.host;
	}

	public int getPort() {
		return this.port;
	} 
	
}
