package elec366.assignment3.protocol.packet;

/*
 * This class is common to server and client. 
 * 
 * Represents the direction of the packet. 
 * - IN  refers to server <- client. 
 * - OUT refers to server -> client. 
 * Directions are always relative to the server. 
 */
public enum PacketDirection {
	IN, OUT, 
}
