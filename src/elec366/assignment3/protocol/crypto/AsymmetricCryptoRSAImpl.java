package elec366.assignment3.protocol.crypto;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/*
 * This class is common to server and client. 
 * 
 * This class implements asymmetric encryption and decryption methods. 
 * This class provides keypair generation, encryption and decryption facilities. 
 * 
 * Objects of this class are stateless. 
 */
public class AsymmetricCryptoRSAImpl implements IAsymmetricCrypto {
	
	AsymmetricCryptoRSAImpl() {
		
	}
	
	@Override
	public KeyPair generateKeypair() {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA"); 
			kpg.initialize(2048); // 2048 bit RSA keypair
			return kpg.generateKeyPair(); 
		} 
		catch(NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	@Override
	public byte[] decrypt(byte[] data, PrivateKey privateKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA"); 
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return cipher.doFinal(data); 
		}
		catch(NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException ex) {
			throw new RuntimeException(ex); 
		}
		catch(BadPaddingException ignored) {
			// Decryption failed
			return null; 
		}
	}
	
	@Override
	public byte[] encrypt(byte[] data, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA"); 
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(data); 
		}
		catch(NoSuchAlgorithmException | InvalidKeyException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException ex) {
			throw new RuntimeException(ex); 
		}
	}
	
	@Override
	public byte[] encrypt(byte[] data, byte[] publicKey) { 
		try {
			return encrypt(data, KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKey)));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException ex) {
			throw new RuntimeException(ex); 
		} 
	}
	
	
}
