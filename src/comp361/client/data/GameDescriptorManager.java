package comp361.client.data;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import comp361.shared.data.GameDescriptor;
import comp361.shared.packets.server.GameDescriptorListPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;

public class GameDescriptorManager extends Observable {

	public Map<Integer, GameDescriptor> gameDescriptors;
	public int currentId;
	
	public GameDescriptorManager(){
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
	 * @param player The player to add.
	 */
	private void internalAddDescriptor(GameDescriptor descriptor) {
		this.gameDescriptors.put(descriptor.getId(), descriptor);
	}
	
	/**
	 * Updates the list of game descriptors according to a {@link GameDescriptorListPacket}, 
	 * effectively adding all descriptors at once.
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
	 * Updates a game descriptor based on a {@link GameDescriptorPlayerUpdatePacket}
	 * @param packet the packet in question
	 */
	public void updateGameDescriptor(GameDescriptorPlayerUpdatePacket packet) {
		if (packet.joined) {
			gameDescriptors.get(packet.id).addPlayer(packet.name);
		} else {
			gameDescriptors.get(packet.id).removePlayer(packet.name);
			
			// If it was the last player, remove the game.
			if (!gameDescriptors.get(packet.id).hasPlayers()) {
				gameDescriptors.remove(packet.id);
			}
		}
		setChanged();
		notifyObservers();
	}
	
}
