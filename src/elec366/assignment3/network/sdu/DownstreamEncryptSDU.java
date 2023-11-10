package elec366.assignment3.network.sdu;

import elec366.assignment3.protocol.crypto.StreamCipher;

public class DownstreamEncryptSDU extends DownstreamSDU {

	public static enum Mode {
		ENCRYPT_ENCODER, 
		ENCRYPT_DECODER, 
	}
	
	private final StreamCipher cipher; 
	private final Mode mode;
	
	public DownstreamEncryptSDU(StreamCipher cipher, Mode mode) {
		super();
		this.cipher = cipher;
		this.mode = mode;
	}

	public StreamCipher getCipher() {
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
