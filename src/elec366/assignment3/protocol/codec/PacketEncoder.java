package elec366.assignment3.protocol.codec;

import java.io.IOException;
import java.io.OutputStream;

import elec366.assignment3.protocol.crypto.IStreamCipher;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketDirection;

/*
 * This class is common to server and client. 
 * 
 * A packet encoder converts a packet to a stream of bytes. 
 * The packet encoder is stateless - it does not have internal states. 
 * (The exception is the internal cryptography cipher, if present.)
 * The encoder processes one packet at a time. 
 * The serialized packet payload length is first written to the output stream as four bytes, 
 * the one-byte packet header is then written. 
 * Finally, the serialized packet payload is written. 
 * The encoding is performed in network order (big endian). 
 * 
 * The encoder supports ciphering - a cipher implementing the IStreamCipher interface
 * can be attached to the encoder any time. 

 */
public class PacketEncoder implements ICipherable {
	
	private IStreamCipher cipher; 
	
	public PacketEncoder() {
		this.cipher = null; 
	}
	
	@Override
	public void removeCipher() {
		this.cipher = null; 
	}

	@Override
	public void attachCipher(IStreamCipher cipher) {
		this.cipher = cipher; 
	}
	
	@Override
	public boolean isEncrypted() {
		return this.cipher != null; 
	}
	
	public void send(OutputStream oStream, Packet packet) throws IOException {
		int header = packet.getDirection() == PacketDirection.IN ? 0x80 : 0x00; 
		header |= packet.getType().getPacketID() & 0x7F; 
		byte[] payload = packet.getPayload(); 
		int length = payload.length; 
		
		this.write(oStream, length >> 24);
		this.write(oStream, length >> 16);
		this.write(oStream, length >> 8);
		this.write(oStream, length);
		this.write(oStream, header);
		for(byte b : payload)
			this.write(oStream, b);
	}
	
	private void write(OutputStream oStream, int b) throws IOException {
		this.write(oStream, (byte)b);
	}
	
	private void write(OutputStream oStream, byte b) throws IOException {
		if(this.cipher != null) 
			b = this.cipher.encrypt(b); 
		oStream.write(b);
	}
	
}
