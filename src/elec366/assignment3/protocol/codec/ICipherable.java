package elec366.assignment3.protocol.codec;

import elec366.assignment3.protocol.crypto.IStreamCipher;

public interface ICipherable {
	
	public void removeCipher(); 
	
	public void attachCipher(IStreamCipher cipher); 

	public boolean isEncrypted(); 
	
}
