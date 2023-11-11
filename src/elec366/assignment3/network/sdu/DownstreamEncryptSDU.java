package elec366.assignment3.network.sdu;

import elec366.assignment3.protocol.crypto.IStreamCipher;

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
