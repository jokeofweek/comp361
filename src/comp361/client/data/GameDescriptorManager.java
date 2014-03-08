package comp361.client.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.server.GameDescriptorListPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GameDescriptorReadyUpdatePacket;
import comp361.shared.packets.server.GameDescriptorStartPacket;

public class GameDescriptorManager extends Observable {

	public Map<Integer, GameDescriptor> gameDescriptors;
	public int currentId;

	public GameDescriptorManager() {
		this.gameDescriptors = new HashMap<>();
	}

	public void addDescriptor(GameDescriptor descriptor) {
		this.internalAddDescriptor(descriptor);
		setChanged();
		notifyObservers();
	}

	public Collection<GameDescriptor> getGameDescriptors() {
		return gameDescriptors.values();
	}

	public GameDescriptor getGameDescriptor(int id) {
		return gameDescriptors.get(id);
	}

	public Collection<Integer> getGameDescriptorIds() {
		return gameDescriptors.keySet();
	}

	/**
	 * Helper method for inserting a player without notifying observers.
	 * 
	 * @param player
	 *            The player to add.
	 */
	private void internalAddDescriptor(GameDescriptor descriptor) {
		this.gameDescriptors.put(descriptor.getId(), descriptor);
	}

	/**
	 * Updates the list of game descriptors according to a
	 * {@link GameDescriptorListPacket}, effectively adding all descriptors at
	 * once.
	 * 
	 * @param packet
	 */
	public void updateGameDescriptorList(GameDescriptorListPacket packet) {
		for (GameDescriptor descriptor : packet.descriptors) {
			this.internalAddDescriptor(descriptor);
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Updates a game descriptor based on a
	 * {@link GameDescriptorPlayerUpdatePacket}
	 * 
	 * @param packet
	 *            the packet in question
	 */
	public void updateGameDescriptor(GameDescriptorPlayerUpdatePacket packet) {
		if (packet.joined) {
			gameDescriptors.get(packet.id).addPlayer(packet.name);
		} else {
			if (gameDescriptors.get(packet.id) != null) {
				gameDescriptors.get(packet.id).removePlayer(packet.name);

				// If it was the last player, remove the game.
				if (!gameDescriptors.get(packet.id).hasPlayers()) {
					gameDescriptors.remove(packet.id);
				}
			}
		}
		setChanged();
		notifyObservers();
	}

	/**
	 * Updates a game descriptor based on a
	 * {@link GameDescriptorReadyUpdatePacket}
	 * 
	 * @param packet
	 *            the packet in question
	 */
	public void updateGameDescriptor(GameDescriptorReadyUpdatePacket packet) {
		GameDescriptor descriptor = gameDescriptors.get(packet.id);
		if (packet.ready) {
			descriptor.addReadyPlayer(packet.name);
		} else {
			descriptor.removeReadyPlayer(packet.name);
		}
		setChanged();
		notifyObservers();
	}
	
	public void updateGameDescriptor(GameDescriptorStartPacket packet) {
		gameDescriptors.get(packet.id).setStarted(true);
		setChanged();
		notifyObservers();
	}

	/**
	 * Updates a game's seed. The game's ready players set is cleared.
	 * 
	 * @param packet
	 */
	public void updateGameSeed(int id, long seed) {
		GameDescriptor descriptor = gameDescriptors.get(id);
		if (descriptor != null) {
			descriptor.setSeed(seed);
			descriptor.clearReadyPlayers();
			setChanged();
			notifyObservers();
		}
	}

}
