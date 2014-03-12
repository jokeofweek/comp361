package comp361.client.data;

import java.util.Observable;

import comp361.client.GameClient;
import comp361.shared.data.Game;
import comp361.shared.packets.shared.GameMovePacket;
import comp361.shared.packets.shared.GameOverPacket;

public class GameManager extends Observable {

	private GameClient client;
	private Game game;
	private boolean isTurn;
	
	public GameManager(GameClient client) {
		this.client = client;
	}
	
	public void setGame(Game game) {
		this.game = game;
		this.isTurn = game.getP1().equals(client.getPlayerName());
		
		notifyObservers();
		setChanged();
	}
	
	public Game getGame() {
		return game;
	}
	
	public boolean isPlayer1() {
		return game.getP1().equals(client.getPlayerName());
	}
	
	public boolean isTurn() {
		return isTurn;
	}
	
	public void applyMove(GameMovePacket packet, boolean isOwn) {
		// If it's not your turn, do nothing
		if (isOwn && !isTurn) {
			return;
		}
		
		game.applyMove(packet);

		// If it was your own move, send to server
		if (isOwn) {
			client.getClient().sendTCP(packet);
			isTurn = false;
		} else {
			isTurn = true;
		}
				
		setChanged();
		notifyObservers();
	}
	
	public void gameOver(GameOverPacket packet) {
		System.out.println("Game is over!");
		// TODO handle game over
	}
	
}
