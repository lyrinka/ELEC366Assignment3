package elec366.assignment3.protocol.crypto;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;

/*
 * This interface is common to server and client. 
 * 
 * This interface denotes the implementing object provides
 * asymmetric encryption and decryption methods. 
 * 
 * The get method returns a default implementation of this interface. 
 */
public interface IAsymmetricCrypto {
	
	public static IAsymmetricCrypto get() {
		return new AsymmetricCryptoRSAImpl(); 
	}
	
	public KeyPair generateKeypair(); 
	
	public byte[] decrypt(byte[] data, PrivateKey privateKey); 
	
	public byte[] encrypt(byte[] data, PublicKey publicKey); 
	
	public byte[] encrypt(byte[] data, byte[] publicKey); 
	
}
