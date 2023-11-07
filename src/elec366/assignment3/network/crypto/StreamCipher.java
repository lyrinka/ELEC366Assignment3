package elec366.assignment3.network.crypto;

public interface StreamCipher {
	
	public byte encrypt(byte input); 
	
	public byte decrypt(byte input); 
	
}
