package comp361.server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import comp361.shared.data.Game;
import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.server.GameDescriptorListPacket;

/**
 * This class handles all the {@link GameDescriptor} information on the server.
 * It provides facilities for creating game descriptors as well as for getting
 * all descriptors.
 */
public class GameDescriptorManager {

	private Map<Integer, GameDescriptor> gameDescriptors;
	private int currentId;

	public GameDescriptorManager() {
		this.gameDescriptors = new HashMap<>();
		this.currentId = 1;
	}

	/**
	 * Create a {@link GameDescriptor} based off a
	 * {@link NewGameDescriptorPacket}.
	 * 
	 * @param packet
	 *            The packet.
	 * @return The filled in descriptor.
	 */
	public GameDescriptor createDescriptor(NewGameDescriptorPacket packet) {
		GameDescriptor descriptor = new GameDescriptor(currentId++,
				packet.name, packet.password, packet.maxPlayers);
		descriptor.setSeed(System.currentTimeMillis());
		gameDescriptors.put(descriptor.getId(), descriptor);
		return descriptor;
	}

	/**
	 * Checks whether there is space for an extra player in a game.
	 * 
	 * @param id
	 *            The ID of the descriptor.
	 * @return True if there are less players in the game.
	 */
	public boolean hasSpace(int id) {
		return gameDescriptors.get(id).getPlayerCount() < gameDescriptors
				.get(id).getMaxPlayers();
	}

	/**
	 * Checks whether the game has started or not.
	 * 
	 * @param id
	 *            The id of the descriptor.
	 * @return True if the game has started.
	 */
	public boolean hasStarted(int id) {
		return gameDescriptors.get(id).isStarted();
	}

	/**
	 * Checks whether the game is ready to start (all players are ready).
	 * 
	 * @param id
	 *            The id of the descriptor.
	 * @return True if the game is ready.a
	 */
	public boolean canStart(int id) {
		return gameDescriptors.get(id).getReadyPlayers().size() == gameDescriptors
				.get(id).getMaxPlayers();
	}

	/**
	 * This adds a player to a given game descriptor
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name of the player
	 * @return True if the player was added, or false if there was no space.
	 */
	public boolean addPlayer(int id, String name) {
		if (!hasSpace(id)) {
			return false;
		}

		gameDescriptors.get(id).addPlayer(name);

		return true;
	}

	/**
	 * This removes a player from a given game descriptor
	 * 
	 * @param id
	 *            The id
	 * @param name
	 *            The name of the player
	 */
	public void removePlayer(int id, String name) {
		gameDescriptors.get(id).removePlayer(name);
		// Remove the descriptor if no players left
		if (gameDescriptors.get(id).getPlayerCount() == 0) {
			gameDescriptors.remove(id);
		}
	}

	/**
	 * Updates a player's ready status in a game.
	 * 
	 * @param id
	 * @param name
	 * @param ready
	 */
	public void setReadyStatus(int id, String name, boolean ready) {
		if (ready) {
			gameDescriptors.get(id).addReadyPlayer(name);
		} else {
			gameDescriptors.get(id).removeReadyPlayer(name);
		}
	}

	/**
	 * Update a game's seed.
	 * 
	 * @param id
	 *            The id of the game.
	 * @param seed
	 *            The seed of the game.
	 */
	public void updateSeed(int id, long seed) {
		gameDescriptors.get(id).setSeed(seed);
		gameDescriptors.get(id).clearReadyPlayers();
	}

	/**
	 * Build a packet containing all the {@link GameDescriptor}.
	 * 
	 * @return the packet.
	 */
	public GameDescriptorListPacket getGameDescriptorListPacket() {
		GameDescriptorListPacket packet = new GameDescriptorListPacket();
		packet.descriptors = new ArrayList<>(gameDescriptors.values());
		return packet;
	}

	/**
	 * Creates a new game based on the game descriptor.
	 * @param id The game descriptor id.
	 * @return The game.
	 */
	public Game createGame(int id) {
		GameDescriptor d = gameDescriptors.get(id);
				
		Game g = new Game(d.getPlayers()[0], d.getPlayers()[1], d.getSeed());
		return g;
	}
}
