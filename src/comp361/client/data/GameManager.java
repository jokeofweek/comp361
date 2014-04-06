package comp361.client.data;

import java.util.List;
import java.util.Observable;

import comp361.client.GameClient;
import comp361.client.data.event.GameEvent;
import comp361.shared.data.Game;
import comp361.shared.data.GameResult;
import comp361.shared.packets.shared.GameMovePacket;
import comp361.shared.packets.shared.GameOverPacket;

public class GameManager extends Observable {

	private GameClient client;
	private Game game;

	public GameManager(GameClient client) {
		this.client = client;
	}

	public void setGame(Game game) {
		this.game = game;

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
		return (isPlayer1() && game.isP1Turn()) || (!isPlayer1() && !game.isP1Turn());
	}

	public void applyMove(GameMovePacket packet, boolean isOwn) {
		// If it's not your turn, do nothing
		if (isOwn && !isTurn()) {
			return;
		}

		List<GameEvent> events = game.applyMove(packet, isOwn);

		// If it was your own move, send to server
		if (isOwn) {
			client.getClient().sendTCP(packet);
		}

		setChanged();
		notifyObservers(events);

		// Test if the game is over!
		String winner = game.getWinner();
		if (winner != null) {
			GameOverPacket overPacket = new GameOverPacket();
			overPacket.message = null;
			overPacket.fromDisconnect = false;
			if ((isPlayer1() ? game.getP1() : game.getP2()).equals(winner)) {
				overPacket.result = GameResult.WIN;
			} else {
				overPacket.result = GameResult.LOSS;
			}
			// Send the packet to the server if it was our own game
			if (isOwn) {
				client.getClient().sendTCP(overPacket);
			}
			// Register that the game is over
			gameOver(overPacket);
			client.publishMessage(overPacket);
		}
	}

	public void gameOver(GameOverPacket packet) {
		System.out.println("Game is over!");
		// TODO handle game over
	}

}
