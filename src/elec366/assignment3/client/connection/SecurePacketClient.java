package elec366.assignment3.client.connection;

import java.security.SecureRandom;
import java.util.logging.Logger;

import elec366.assignment3.protocol.crypto.IAsymmetricCrypto;
import elec366.assignment3.protocol.crypto.IStreamCipher;
import elec366.assignment3.protocol.packet.Packet;
import elec366.assignment3.protocol.packet.impl.PacketInSetSessionKey;
import elec366.assignment3.protocol.packet.impl.PacketOutSessionAck;
import elec366.assignment3.protocol.packet.impl.PacketOutSetPublicKey;

public abstract class SecurePacketClient extends PacketClient {

	private static enum SessionState {
		DISCONNECTED, 
		CONNECTED, 
		ENCRYPTED, 
		ESTABLISHED, 
	}
	
	private final Logger logger; 
	
	private final IAsymmetricCrypto asc; 
	
	private SessionState sessionState; 
	
	
	public SecurePacketClient(Logger clientLogger, Logger networkLogger, String host, int port) {
		super(clientLogger, networkLogger, host, port); 
		this.logger = clientLogger; 
		this.asc = IAsymmetricCrypto.get(); 
		this.sessionState = SessionState.DISCONNECTED; 
	}

	@Override
	public void onConnection() {
		this.sessionState = SessionState.CONNECTED; 
	}

	@Override
	public void onDisconnection() {
		this.onSecureDisconnection(); 
		this.sessionState = SessionState.DISCONNECTED; 
	}
	
	@Override
	public void onAbnormalDisconnection(String reason, Throwable cause) {
		this.onSecureAbnormalDisconnection(reason, cause);
		this.sessionState = SessionState.DISCONNECTED; 
	}

	@Override
	public void onOutboundPacket(Packet.Out packet) {
		switch(this.sessionState) {
			default:
			case DISCONNECTED: 
				return; 
			case CONNECTED: {
				if(!(packet instanceof PacketOutSetPublicKey)) {
					this.logger.warning("Server sent an unexpected packet during session establishment flow. (1)");
					this.disconnect();
				}
				else {
					PacketOutSetPublicKey packet0 = (PacketOutSetPublicKey)packet; 
					SecureRandom sr = new SecureRandom(); 
					byte[] key = new byte[16]; 
					byte[] iv  = new byte[16]; 
					sr.nextBytes(key);
					sr.nextBytes(iv); 
					IStreamCipher cipher1 = IStreamCipher.get(key, iv); 
					IStreamCipher cipher2 = IStreamCipher.get(key, iv); 
					this.sendPacket(new PacketInSetSessionKey(
						this.asc.encrypt(key, packet0.getPublicKey()), 
						this.asc.encrypt(iv,  packet0.getPublicKey())
					)); 
					this.setDecoderEncryption(cipher1);
					this.setEncoderEncryption(cipher2);
					this.sessionState = SessionState.ENCRYPTED; 
					
				}
				break;
			}
			case ENCRYPTED: {
				if(!(packet instanceof PacketOutSessionAck)) {
					this.logger.warning("Server sent an unexpected packet during session establishment flow. (2)");
					this.disconnect();
				}
				this.sessionState = SessionState.ESTABLISHED; 
				this.onSecureConnection(); 
				break; 
			}
			case ESTABLISHED: {
				this.onSecureOutboundPacket(packet); 
				break;
			}
		}
	}
	
	public abstract void onSecureConnection(); 
	
	public abstract void onSecureDisconnection(); 
	
	public abstract void onSecureAbnormalDisconnection(String reason, Throwable cause); 
	
	public abstract void onSecureOutboundPacket(Packet.Out packet); 
	
	public void sendSecurePacket(Packet.In packet) {
		if(this.sessionState == SessionState.ESTABLISHED)
			this.sendPacket(packet);
	}

}
