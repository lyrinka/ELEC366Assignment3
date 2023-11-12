package elec366.assignment3.client.app.type;

public class ConnectionInformation {

	private String host; 
	private int port;
	
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

	public void setHost(String host) {
		this.host = host;
	}

	public void setPort(int port) {
		this.port = port;
	}
	
}
