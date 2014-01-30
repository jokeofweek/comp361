package examples.newgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import comp361.shared.Constants;

public class CoralPanel extends JPanel implements Observer {

	private static final int PANEL_WIDTH = (Constants.CORAL_WIDTH + Constants.CORAL_X_OFFSET) * Constants.TILE_SIZE;
	private static final int PANEL_HEIGHT = Constants.CORAL_HEIGHT * Constants.TILE_SIZE;
	private boolean first = true;
	
	private CoralReefGenerator reefGenerator;

	public CoralPanel(CoralReefGenerator reefGenerator) {
		this.reefGenerator = reefGenerator;
		
		// Add self as listener to the reef
		this.reefGenerator.addObserver(this);
		
		// Set the size to be the size of the field plus ten columns on the left
		// for the base.
		Dimension d = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		setPreferredSize(d);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		// Fill the background with black
		// TODO: Fill the background with water tile
		g.setColor(Color.black);
		g.fillRect(0, 0, PANEL_WIDTH, PANEL_HEIGHT);
		
		// Paint the coral tiles
		g.setColor(Color.green);
		for (int x = 0; x < Constants.CORAL_WIDTH; x++) {
			for (int y = 0; y < Constants.CORAL_HEIGHT; y++) {
				if (reefGenerator.getReef()[x][y]) {
					g.fillRect((x + Constants.CORAL_X_OFFSET) * Constants.TILE_SIZE, y * Constants.TILE_SIZE,
							Constants.TILE_SIZE, Constants.TILE_SIZE);
				}
			}
		}
		
		// Draw the base. To do this, we need to calculate how far away
		// the base is from the top, but since we're only drawing the map
		// portion starting at the top of the coral, we have to make sure
		// to draw the base at the right offset.
		int baseOffset = Constants.BASE_Y_OFFSET - Constants.CORAL_Y_OFFSET;
		g.setColor(Color.red);
		for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
			g.fillRect(0, (y + baseOffset) * Constants.TILE_SIZE, 
					Constants.TILE_SIZE, Constants.TILE_SIZE);
		}
		
		// Draw the squares where a ship can be placed
		g.setColor(Color.blue);
		g.fillRect(0, (-1 + baseOffset) * Constants.TILE_SIZE, 
				Constants.TILE_SIZE, Constants.TILE_SIZE);
		g.fillRect(0, (Constants.BASE_HEIGHT + baseOffset) * Constants.TILE_SIZE, 
				Constants.TILE_SIZE, Constants.TILE_SIZE);
		for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
			g.fillRect(Constants.TILE_SIZE, (y + baseOffset) * Constants.TILE_SIZE, 
					Constants.TILE_SIZE, Constants.TILE_SIZE);
		}
		
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// Repaint on update from the reef generator
		if (o == this.reefGenerator && this.isVisible()) {
			repaint();
		}
	}

}
