package elec366.assignment3.network.codec;

import elec366.assignment3.network.crypto.StreamCipher;

public interface Cipherable {
	
	public void removeCipher(); 
	
	public void attachCipher(StreamCipher cipher); 

	public boolean isEncrypted(); 
	
}
