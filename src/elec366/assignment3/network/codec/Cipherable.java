package elec366.assignment3.network.codec;

import elec366.assignment3.network.crypto.ByteStreamCipher;

public interface Cipherable {
	
	public void removeCipher(); 
	
	public void attachCipher(ByteStreamCipher cipher); 

}
