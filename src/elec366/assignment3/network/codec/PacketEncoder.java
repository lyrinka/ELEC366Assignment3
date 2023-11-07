package elec366.assignment3.network.codec;

import java.io.IOException;
import java.io.OutputStream;

import elec366.assignment3.network.crypto.ByteStreamCipher;
import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketDirection;

public class PacketEncoder implements Cipherable {
	
	private final OutputStream oStream; 
	
	private ByteStreamCipher cipher; 
	
	public PacketEncoder(OutputStream oStream) {
		this.oStream = oStream; 
		this.cipher = null; 
	}
	
	@Override
	public void removeCipher() {
		this.cipher = null; 
	}

	@Override
	public void attachCipher(ByteStreamCipher cipher) {
		this.cipher = cipher; 
	}
	
	public void send(Packet packet) throws IOException {
		int header = packet.getDirection() == PacketDirection.IN ? 0x80 : 0x00; 
		header |= packet.getType().getPacketID() & 0x7F; 
		byte[] payload = packet.getPayload(); 
		int length = payload.length; 
		
		this.write(length >> 24);
		this.write(length >> 16);
		this.write(length >> 8);
		this.write(length);
		this.write(header);
		for(byte b : payload)
			this.write(b);
	}
	
	private void write(int b) throws IOException {
		this.write((byte)b);
	}
	
	private void write(byte b) throws IOException {
		if(this.cipher != null) 
			b = this.cipher.apply(b); 
		this.oStream.write(b);
	}
	
}
