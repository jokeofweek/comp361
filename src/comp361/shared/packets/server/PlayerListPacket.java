package comp361.shared.packets.server;

import java.util.ArrayList;

import comp361.shared.data.Player;

/**
 * This packet is sent from the server to the client when the client first
 * logins or registers, and contains a full list of all players. Afterwards,
 * {@link PlayerUpdatePacket} should be used to send changes.
 */
public class PlayerListPacket {
	public ArrayList<Player> players;	
}
