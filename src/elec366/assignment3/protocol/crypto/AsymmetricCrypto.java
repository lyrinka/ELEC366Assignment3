package elec366.assignment3.protocol.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

public interface AsymmetricCrypto {
	
	public KeyPair generateKeypair(); 
	
	public byte[] decrypt(byte[] data, PrivateKey privateKey); 
	
	public byte[] encrypt(byte[] data, PublicKey publicKey); 
	
	public byte[] encrypt(byte[] data, byte[] publicKey); 
	
}
