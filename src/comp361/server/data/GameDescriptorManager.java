package comp361.server.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import comp361.server.GameServer;
import comp361.server.session.Session;
import comp361.server.session.SessionType;
import comp361.shared.data.Game;
import comp361.shared.data.GameDescriptor;
import comp361.shared.data.GameResult;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.server.GameDescriptorListPacket;
import comp361.shared.packets.server.GameDescriptorRemovedPacket;
import comp361.shared.packets.server.UpdatePlayerStatisticsPacket;
import comp361.shared.packets.shared.GameOverPacket;

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
				packet.name, packet.password, packet.maxPlayers, packet.shipInventory);
		descriptor.setSeed(System.currentTimeMillis());
		gameDescriptors.put(descriptor.getId(), descriptor);
		return descriptor;
	}
	
	/**
	 * This loads in a descriptor from a saved game.
	 * @param descriptor
	 */
	public void loadDescriptor(GameDescriptor descriptor) {
		descriptor.setId(currentId++);
		gameDescriptors.put(descriptor.getId(), descriptor);
	}
	
	/**
	 * Updates a player's starting positions for a game descriptor
 	 * @param id The ID of the game descriptor.
	 * @param player The player's name
	 * @param positions The set of positions
	 */
	public void setPlayerPositions(int id, String player, int[] positions){ 
		GameDescriptor d = gameDescriptors.get(id);
		for (int i = 0; i < d.getMaxPlayers(); i++) {
			if (d.getPlayers()[i] != null && d.getPlayers()[i].equals(player)) {
				d.setPositions(i, positions);
				return;
			}
		}
		throw new IllegalArgumentException("No player " + player + " in " + id +". Could not set player positions.");
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
				
		Game g = new Game(d.getPlayers()[0], d.getPlayers()[1], d.getSeed(), d.getPositions(), d.getShipInventory());
		return g;
	}
	
	/**
	 * This ends a game, sending the appropriate packets to all involved players.
	 * @param id The ID of the game.
	 * @param server The game server.
	 * @param draw True if the game ended in a draw.
	 * @param loser The loser, if there was one.
	 * @param message The message to include
	 * @param fromDisconnect Whether the game was over from an early disconnect
	 * @param sendPacket Whether the packet should be sent to clients
	 */
	public void endGame(int id, GameServer server, boolean draw, String loser, String message, boolean fromDisconnect, boolean sendPacket) {
		// Create a packet for the winner
		GameOverPacket winPacket = new GameOverPacket();
		winPacket.result = draw ? GameResult.DRAW : GameResult.WIN;
		winPacket.message = message;
		winPacket.fromDisconnect = fromDisconnect;
		
		// Create a packet for the loser
		GameOverPacket lossPacket = new GameOverPacket();
		lossPacket.result = GameResult.LOSS;
		lossPacket.message = message;
		lossPacket.fromDisconnect = fromDisconnect;
			
		// Send to all players
		for (Connection c : server.getServer().getConnections()) {
			Session s = (Session) c;
			if (s.getSessionType() == SessionType.GAME && s.getGameDescriptorId() == id) {
				if (sendPacket) {
					if (!draw && s.getAccount().getName().equals(loser)) {
						s.sendTCP(lossPacket);
						// Update the player's statistics
						s.getAccount().getPlayer().getStatistics().setLosses(
								s.getAccount().getPlayer().getStatistics().getLosses() + 1);
						
					} else {
						s.sendTCP(winPacket);
						if (draw) {
							s.getAccount().getPlayer().getStatistics().setDraws(
									s.getAccount().getPlayer().getStatistics().getDraws() + 1);
						} else {
							s.getAccount().getPlayer().getStatistics().setWins(
									s.getAccount().getPlayer().getStatistics().getWins() + 1);
						}
					}
					
					UpdatePlayerStatisticsPacket packet = new UpdatePlayerStatisticsPacket();
					packet.name = s.getAccount().getPlayer().getName();
					packet.statistics = s.getAccount().getPlayer().getStatistics();
					server.getServer().sendToAllTCP(packet);
				}
				
				// Update the session type back to lobby
				s.setSessionType(SessionType.LOBBY);
			}
		}
		
		// Remove the game descriptor
		gameDescriptors.remove(id);
		GameDescriptorRemovedPacket packet = new GameDescriptorRemovedPacket();
		packet.id = id;
		server.getServer().sendToAllTCP(packet);
		
		// Lawg dawg
		server.getLogger().debug("Game " + id + " is over. Message: " + message);
	}
	
	public GameDescriptor getGameDescriptor(int id) {
		return gameDescriptors.get(id);
	}
}
