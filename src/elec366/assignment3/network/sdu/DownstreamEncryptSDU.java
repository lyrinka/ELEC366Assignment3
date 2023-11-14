package elec366.assignment3.network.sdu;

import elec366.assignment3.protocol.crypto.IStreamCipher;

/*
 * This class is common to server and client. 
 * 
 * This SDU is sent by the upper layers and informs the connection worker
 * to immediately apply the given stream cipher to its codecs, 
 * either encoder or decoder. 
 * To fully enable encryption, two encryption SDUs are to be sent, 
 * one for encoding and one for decoding. 
 */
public class DownstreamEncryptSDU extends DownstreamSDU {

	public static enum Mode {
		ENCRYPT_ENCODER, 
		ENCRYPT_DECODER, 
	}
	
	private final IStreamCipher cipher; 
	private final Mode mode;
	
	public DownstreamEncryptSDU(IStreamCipher cipher, Mode mode) {
		super();
		this.cipher = cipher;
		this.mode = mode;
	}

	public IStreamCipher getCipher() {
		return this.cipher;
	}

	public Mode getMode() {
		return this.mode;
	}

	@Override
	public String toString() {
		return "DownstreamEncryptSDU [mode=" + this.mode + "]";
	}

}
