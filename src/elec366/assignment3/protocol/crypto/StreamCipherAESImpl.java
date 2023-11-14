package elec366.assignment3.protocol.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/*
 * This class is common to server and client. 
 * 
 * This class implements symmetric encryption and decryption, 
 * commonly referred as stream cipher. 
 * 
 * Objects of this class are stateful. 
 * For a Tx/Rx thread pair, two ciphers are needed. 
 */
public class StreamCipherAESImpl implements IStreamCipher {

	private final Cipher encrypt; 
	private final Cipher decrypt; 
	
	// Pre-allocate buffers for memory efficiency
	private final byte[] encryptBuffer1 = new byte[1]; 
	private final byte[] encryptBuffer2 = new byte[1]; 
	private final byte[] decryptBuffer1 = new byte[1]; 
	private final byte[] decryptBuffer2 = new byte[1]; 
	
	StreamCipherAESImpl(byte[] key, byte[] iv) {
		try {
			SecretKeySpec keyObj = new SecretKeySpec(key, "AES"); 
			IvParameterSpec ivObj = new IvParameterSpec(iv); 

			// CFB8 has a feedback length of only 8 bits, effectively deriving
			// a byte-oriented stream cipher from 16-byte AES block ciphers.
			this.encrypt = Cipher.getInstance("AES/CFB8/NoPadding");
			this.encrypt.init(Cipher.ENCRYPT_MODE, keyObj, ivObj); 
		
			this.decrypt = Cipher.getInstance("AES/CFB8/NoPadding"); 
			this.decrypt.init(Cipher.DECRYPT_MODE, keyObj, ivObj); 
		}
		catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | InvalidAlgorithmParameterException ex) {
			throw new RuntimeException(ex); 
		} 
	}

	@Override
	public byte encrypt(byte input) {
		try {
			this.encryptBuffer1[0] = input; 
			this.encrypt.update(this.encryptBuffer1, 0, 1, this.encryptBuffer2);
			return this.encryptBuffer2[0]; 
		} catch (ShortBufferException ex) {
			throw new RuntimeException(ex); 
		} 
	}

	@Override
	public byte decrypt(byte input) {
		try {
			this.decryptBuffer1[0] = input; 
			this.decrypt.update(this.decryptBuffer1, 0, 1, this.decryptBuffer2);
			return this.decryptBuffer2[0]; 
		} catch (ShortBufferException ex) {
			throw new RuntimeException(ex); 
		} 
	}
	
	

}
