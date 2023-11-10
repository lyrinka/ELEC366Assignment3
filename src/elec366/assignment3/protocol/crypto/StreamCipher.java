package elec366.assignment3.protocol.crypto;

public interface StreamCipher {
	
	public static StreamCipher get(byte[] key, byte[] iv) {
		return new StreamCipherAESImpl(key, iv); 
	}
	
	public byte encrypt(byte input); 
	
	public byte decrypt(byte input); 
	
}
