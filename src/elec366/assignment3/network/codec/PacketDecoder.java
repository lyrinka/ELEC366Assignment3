package elec366.assignment3.network.codec;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import elec366.assignment3.network.codec.exception.PacketDesynchronizedException;
import elec366.assignment3.network.codec.exception.PacketDecodeException;
import elec366.assignment3.network.codec.exception.PacketHeaderInvalidException;
import elec366.assignment3.network.crypto.StreamCipher;
import elec366.assignment3.network.packet.Packet;
import elec366.assignment3.network.packet.PacketType;

public class PacketDecoder implements Cipherable {
	
	private static final int MAX_PACKET_LENGTH = 65535; 
	
	private static enum State {
		READ_LENGTH, 
		READ_HEADER, 
		READ_PAYLOAD, 
	}
	
	private State state; 
	private int index; 
	private int buffer0; 
	private byte[] buffer1; 
	
	private StreamCipher cipher; 
	
	public PacketDecoder() {
		this.state = State.READ_LENGTH; 
		this.index = 0; 
		this.buffer0 = 0; 
		this.buffer1 = null; 
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
	
	public Packet accept(byte input0) throws PacketDecodeException {
		if(this.cipher != null) input0 = this.cipher.decrypt(input0); 
		int input = Byte.toUnsignedInt(input0); 
		
		Packet packet = null; 
		switch(this.state) {
			default: 
			case READ_LENGTH: {
				if(this.index == 0 && input == 0xFF) break; 
				this.buffer0 = this.buffer0 << 8 | input; 
				if(++this.index < 4) break; 

				int length = this.buffer0; 
				if(length < 0 || length > MAX_PACKET_LENGTH) 
					throw new PacketDesynchronizedException("Invalid length received: " + length); 
				
				this.state = State.READ_HEADER; 
				this.index = 0; 
				this.buffer1 = new byte[length]; 
				break; 
			}
			case READ_HEADER: {
				this.buffer0 = input;
				if(this.buffer1.length == 0) {
					packet = this.processPacketAndContinue(); 
					break; 
				}
				
				this.state = State.READ_PAYLOAD; 
				break; 
			}
			case READ_PAYLOAD: {
				this.buffer1[this.index++] = (byte)input; 
				if(this.index < this.buffer1.length) break; 
				
				packet = this.processPacketAndContinue();  
				break; 
			}
		}
		return packet; 
	}
	
	private Packet processPacketAndContinue() throws PacketDecodeException {
		Packet packet = this.processPacket(this.buffer0, this.buffer1); 
		this.state = State.READ_LENGTH; 
		this.index = 0; 
		this.buffer0 = 0; 
		this.buffer1 = null; 
		return packet; 
	}

	private Packet processPacket(int header, byte[] packet) throws PacketDecodeException {
		PacketType type;
		
		if((header & 0x80) != 0) {
			type = PacketType.IN.getPacketByID(header & 0x7F); 
		}
		else {
			type = PacketType.OUT.getPacketByID(header & 0x7F); 
		}
		
		if(type == null)
			throw new PacketHeaderInvalidException("Invalid header received: 0x" + Integer.toHexString(header)); 
		
		Class<? extends Packet> packetClass = type.getPacketClass(); 
		try {
			Constructor<? extends Packet> constructor = packetClass.getConstructor(byte[].class);
			return constructor.newInstance(packet); 
		} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			// There's a problem with our code, crash the application now
			throw new RuntimeException(e); 
		}
	}
	
	// Mainly used for debugging
	public Packet readFullPacket(InputStream iStream) throws PacketDecodeException {
		try {
			Packet packet = null; 
			while(packet == null) {
				int data = iStream.read(); 
				if(data < 0) throw new RuntimeException("Input stream drained."); 
				packet = this.accept((byte)data); 
			}
			return packet; 
		}
		catch(IOException ex) {
			throw new RuntimeException(ex); 
		}
	}
	
}
