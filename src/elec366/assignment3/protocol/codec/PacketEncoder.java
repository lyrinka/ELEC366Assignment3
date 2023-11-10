package elec366.assignment3.protocol.codec;

import java.io.IOException;
import java.io.OutputStream;

import elec366.assignment3.protocol.crypto.StreamCipher;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketDirection;

public class PacketEncoder implements Cipherable {
	
	private StreamCipher cipher; 
	
	public PacketEncoder() {
		this.cipher = null; 
	}
	
	@Override
	public void removeCipher() {
		this.cipher = null; 
	}

	@Override
	public void attachCipher(StreamCipher cipher) {
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
