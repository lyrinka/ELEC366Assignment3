package elec366.assignment3.protocol.codec;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import elec366.assignment3.protocol.codec.exception.PacketDecodeException;
import elec366.assignment3.protocol.codec.exception.PacketDesynchronizedException;
import elec366.assignment3.protocol.codec.exception.PacketHeaderInvalidException;
import elec366.assignment3.protocol.crypto.IStreamCipher;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.PacketType;
import elec366.assignment3.protocol.serdes.exception.PayloadDeserializationException;

public class PacketDecoder implements ICipherable {
	
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
	
	private IStreamCipher cipher; 
	
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
	public void attachCipher(IStreamCipher cipher) {
		this.cipher = cipher; 
	}
	
	@Override
	public boolean isEncrypted() {
		return this.cipher != null; 
	}
	
	public Packet accept(byte input0) throws PacketDecodeException, PayloadDeserializationException {
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
	
	private Packet processPacketAndContinue() throws PacketDecodeException, PayloadDeserializationException {
		Packet packet = this.processPacket(this.buffer0, this.buffer1); 
		this.state = State.READ_LENGTH; 
		this.index = 0; 
		this.buffer0 = 0; 
		this.buffer1 = null; 
		return packet; 
	}

	private Packet processPacket(int header, byte[] packet) throws PacketDecodeException, PayloadDeserializationException {
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
			return constructor.newInstance(packet); // This constructor may throw PayloadDeserializationException
		} 
		catch(InvocationTargetException e) {
			// This constructor may throw PayloadDeserializationException
			if(e.getCause() instanceof PayloadDeserializationException)
				throw (PayloadDeserializationException)e.getCause(); 
			else throw new RuntimeException(e); 
		}
		catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException e) {
			// There's a problem with our code, crash the application now
			throw new RuntimeException(e); 
		}
	}
	
	// Mainly used for debugging
	public Packet readFullPacket(InputStream iStream) throws PacketDecodeException, PayloadDeserializationException {
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
