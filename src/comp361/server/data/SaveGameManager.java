package comp361.server.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import comp361.server.GameServer;
import comp361.shared.Constants;
import comp361.shared.data.Game;
import comp361.shared.packets.server.SavedGameContainer;

public class SaveGameManager {
	
	private static final String SAVE_GAME_PATH = Constants.SERVER_DATA_PATH + "saves/";
	
	private GameServer server;
	
	public SaveGameManager(GameServer server) {
		this.server = server;
	}
	
	public void saveGame(Game game, int gameDescriptorId) {
		SavedGameContainer container = new SavedGameContainer();
		container.game = game;
		container.descriptor = server.getGameDescriptorManager().getGameDescriptor(gameDescriptorId);
		
		// Create the file
		String time = "" + System.currentTimeMillis();
		String nonce = "" + new Random().nextInt(10000);
		String fileName = time + "_" + game.getP1() + "_" + game.getP2() + "_" + nonce + ".game";
		container.fileName = fileName;
		
		Kryo k = new Kryo();
		Output out = null;
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(SAVE_GAME_PATH + fileName);
			out = new Output(fos);
			k.writeObject(out, container);
		} catch (FileNotFoundException e) {
			server.getLogger().error("Error saving game to " + fileName + ": " + e.toString());
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
	}
	
	/**
	 * This fetches all of a player's saved games and returns a list of their
	 * saved game containers. 
	 * @param playerName The name of the player
	 * @param keepGame True if the game should be kept, else false means it gets
	 * 	set to null.
	 * @return a list of the saved games.
	 */
	public List<SavedGameContainer> getSavedGames(String playerName, boolean keepGame) {
		ArrayList<SavedGameContainer> containers = new ArrayList<SavedGameContainer>();
		for (File f : new File(SAVE_GAME_PATH).listFiles()) {
			if (f.getName().contains(playerName +"_")) {
				SavedGameContainer container = loadGameContainer(f.getName());
				if (container != null) {
					if (!keepGame) {
						container.game = null;
					}
					containers.add(container);
				}
			}
		}
		return containers;
	}
	
	/**
	 * This loads a game container from a file.
	 * @param fileName The file to load.
	 * @return The saved game in the file.
	 */
	private SavedGameContainer loadGameContainer(String fileName) {
		Kryo k = new Kryo();
		Input in = null;
		FileInputStream fin = null;
		SavedGameContainer container = null;		
		try {
			fin = new FileInputStream(SAVE_GAME_PATH + fileName);
			in = new Input(fin);
			container = k.readObject(in, SavedGameContainer.class);
		} catch (FileNotFoundException e) {
			server.getLogger().error("Error loading game from " + fileName + ": " + e.toString());
			e.printStackTrace();
		} finally {
			if (in != null) {
				in.close();
			}
		}
		return container;
	}
	
	/**
	 * Fetches the container for a given file.
	 * @param fileName The filename of the save game.
	 * @return The container containing all the save information.
	 */
	public SavedGameContainer loadGame(String fileName) {
		return loadGameContainer(fileName);
	}
}
