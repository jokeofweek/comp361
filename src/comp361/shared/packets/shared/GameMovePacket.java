package comp361.shared.packets.shared;

import java.awt.Point;

import comp361.shared.data.MoveType;

public class GameMovePacket {

	public int ship;
	public MoveType moveType;
	// An optional point which can be used by the move, eg.
	// where we are moving to or where we fired the cannon.
	public Point contextPoint;
	
}
