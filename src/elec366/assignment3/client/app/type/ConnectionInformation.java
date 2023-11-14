package elec366.assignment3.client.app.type;

/*
 * This class applies to only client.
 * 
 * Connection information groups the host and port of a TCP connection target.
 */
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
