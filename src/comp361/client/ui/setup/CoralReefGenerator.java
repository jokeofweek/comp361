package comp361.client.ui.setup;

import java.util.Observable;
import java.util.Random;

import comp361.shared.Constants;

public class CoralReefGenerator extends Observable {	

	private final static int CORAL_PERCENT = 10;
	
	private boolean[][] hasCoral;
	private long seed;
	
	public CoralReefGenerator() {
	}
	
	public void regenerateReef() {
		regenerateReef(System.currentTimeMillis());
	}
	
	public void regenerateReef(long seed) {
		this.seed = seed;
		
		hasCoral = new boolean[Constants.CORAL_WIDTH][Constants.CORAL_HEIGHT];

		
		Random random = new Random(seed);
		int remainingBlocks = (Constants.CORAL_WIDTH * Constants.CORAL_HEIGHT) / CORAL_PERCENT;
		
		int x, y;
		// Keep on trying random places until we've placed all remaining blocks
		while (remainingBlocks > 0) {
			x = random.nextInt(Constants.CORAL_WIDTH);
			y = random.nextInt(Constants.CORAL_HEIGHT);
			if (!hasCoral[x][y]) {
				hasCoral[x][y] = true;
				remainingBlocks--;
			}
		}
		
		// Notify observers that the reef has changed.
		setChanged();
		notifyObservers();
	}

	public boolean[][] getReef() {
		return hasCoral;
	}
	
	public long getSeed() {
		return seed;
	}
	
	
	
	
	
}
