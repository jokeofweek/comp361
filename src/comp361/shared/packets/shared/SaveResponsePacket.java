package comp361.shared.packets.shared;

import comp361.shared.data.Game;

public class SaveResponsePacket {
	public boolean accepted;
	public Game game;
	public boolean isP1Turn;
}
