package comp361.shared.packets.shared;

import comp361.shared.data.GameResult;

public class GameOverPacket {
	public GameResult result;
	// An optional reason message (eg. Dominic has disconnect)
	public String message;
	// Boolean containing whether it was a disconnect
	public boolean fromDisconnect;
}
