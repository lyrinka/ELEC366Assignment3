package elec366.assignment3.protocol.codec;

import elec366.assignment3.protocol.crypto.StreamCipher;

public interface Cipherable {
	
	public void removeCipher(); 
	
	public void attachCipher(StreamCipher cipher); 

	public boolean isEncrypted(); 
	
}
