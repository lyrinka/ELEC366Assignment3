package elec366.assignment3.protocol.crypto;

/*
 * This interface is common to server and client. 
 * 
 * This interface denotes the implementing object provides
 * byte-oriented symmetric encryption and decryption methods. 
 * 
 * The get method returns a default implementation of this interface, 
 * with the specified shared secret and shared initialization vector. 
 */
public interface IStreamCipher {
	
	public static IStreamCipher get(byte[] key, byte[] iv) {
		return new StreamCipherAESImpl(key, iv); 
	}
	
	public byte encrypt(byte input); 
	
	public byte decrypt(byte input); 
	
}
