package comp361.server.data;

import comp361.shared.data.Game;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.server.SavedGameContainer;

public class SaveGameManager {
	
	public void saveGame(Game game, GameDescriptor descriptor) {
		SavedGameContainer container = new SavedGameContainer();
		container.game = game;
		container.descriptor = descriptor;
	}
}
