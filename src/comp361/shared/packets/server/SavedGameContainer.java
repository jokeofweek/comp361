package comp361.shared.packets.server;

import java.util.Date;

import comp361.shared.data.Game;
import comp361.shared.data.GameDescriptor;

public class SavedGameContainer {
	public GameDescriptor descriptor;
	public Game game;
	public String fileName;
	public Date saveDate;
}
