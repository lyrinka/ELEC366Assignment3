package elec366.assignment3.server.connection.qmh;

import java.util.function.Supplier;

import elec366.assignment3.network.crypto.StreamCipher;

public class EncryptionRequestMessage extends QueuedMessage {

	private final StreamCipher encoderCipher; 
	private final StreamCipher decoderCipher; 
	
	public EncryptionRequestMessage(int id, StreamCipher encoderCipher, StreamCipher decoderCipher) {
		super(id); 
		this.encoderCipher = encoderCipher; 
		this.decoderCipher = decoderCipher; 
	}
	
	public EncryptionRequestMessage(int id, Supplier<StreamCipher> cipherSupplier) {
		this(id, cipherSupplier.get(), cipherSupplier.get()); 
	}

	public StreamCipher getEncoderCipher() {
		return this.encoderCipher;
	}

	public StreamCipher getDecoderCipher() {
		return this.decoderCipher;
	}

	@Override
	public String toString() {
		return "EncryptionRequestMessage [connectionID=" + this.connectionID + "]";
	}
	
}
