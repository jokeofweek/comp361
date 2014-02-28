package comp361.shared.packets.server;

import java.util.ArrayList;

import comp361.shared.data.GameDescriptor;

/**
 * This packet is sent from the server to the client when the client first
 * logins or registers, and contains a full list of all game descriptors.
 */
public class GameDescriptorListPacket {
	public ArrayList<GameDescriptor> descriptors;	
}
