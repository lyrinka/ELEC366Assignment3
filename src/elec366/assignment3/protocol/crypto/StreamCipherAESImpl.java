package elec366.assignment3.protocol.crypto;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class StreamCipherAESImpl implements StreamCipher {

	private final Cipher encrypt; 
	private final Cipher decrypt; 
	
	private final byte[] encryptBuffer1 = new byte[1]; 
	private final byte[] encryptBuffer2 = new byte[1]; 
	private final byte[] decryptBuffer1 = new byte[1]; 
	private final byte[] decryptBuffer2 = new byte[1]; 
	
	StreamCipherAESImpl(byte[] key, byte[] iv) {
		try {
			SecretKeySpec keyObj = new SecretKeySpec(key, "AES"); 
			IvParameterSpec ivObj = new IvParameterSpec(iv); 

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
