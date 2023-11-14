package elec366.assignment3.protocol.codec;

import elec366.assignment3.protocol.crypto.IStreamCipher;

/*
 * This interface is common to server and client. 
 * 
 * This interface denotes that a stream cipher can be attached or detached
 * from the object. 
 */
public interface ICipherable {
	
	public void removeCipher(); 
	
	public void attachCipher(IStreamCipher cipher); 

	public boolean isEncrypted(); 
	
}
