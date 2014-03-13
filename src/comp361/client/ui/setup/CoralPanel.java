package comp361.client.ui.setup;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JPanel;

import comp361.client.resources.ResourceManager;
import comp361.client.ui.SwagFactory;
import comp361.client.ui.setup.NewGamePanel.ReadyActionListener;
import comp361.shared.Constants;
import comp361.shared.data.Direction;
import comp361.shared.data.Ship;

public class CoralPanel extends JPanel implements Observer {
	// Number of tiles along the x and y of the panel
	private static final int PANEL_TILES_X = Constants.CORAL_WIDTH + Constants.CORAL_X_OFFSET;
	private static final int PANEL_TILES_Y = Constants.CORAL_HEIGHT;

	// Calculate base offset
	private static final int BASE_OFFSET = Constants.BASE_Y_OFFSET - Constants.CORAL_Y_OFFSET;

	// Size of the panel (in pixels)
	private static final int PANEL_WIDTH = (PANEL_TILES_X) * Constants.TILE_SIZE;
	private static final int PANEL_HEIGHT = PANEL_TILES_Y * Constants.TILE_SIZE;

	// Preload images
	private Image waterImage = ResourceManager.getInstance().getWaterImage();
	private Image reefImage = ResourceManager.getInstance().getReefImage();

	private CoralReefGenerator reefGenerator;

	private int[] shipWidths;
	private int[] shipPositions;
	private int selectedShip = 0;
	private boolean[][] reefMask;
	private ReadyActionListener readyActionListener;

	public CoralPanel(int shipInventory, CoralReefGenerator reefGenerator, ReadyActionListener readyActionListener) {
		this.reefGenerator = reefGenerator;
		this.reefMask = reefGenerator.getReef();
		this.readyActionListener = readyActionListener;
				
		// Setup the widths and positions
		shipWidths = new int[Ship.SHIP_INVENTORIES[shipInventory].length];
		shipPositions = new int[Ship.SHIP_INVENTORIES[shipInventory].length];
		for (int i = 0; i < shipWidths.length; i++) {
			shipWidths[i] = Ship.SHIP_INVENTORIES[shipInventory][i].getSize();
			shipPositions[i] = (i + 1);
		}
		
		
		// Add self as listener to the reef
		this.reefGenerator.addObserver(this);
		
		// Add the mouse listener
		this.addMouseListener(new CoralMouseListener());
		
		// Set the size to be the size of the field plus ten columns on the left
		// for the base.
		SwagFactory.style(this);
		Dimension d = new Dimension(PANEL_WIDTH, PANEL_HEIGHT);
		setPreferredSize(d);
		setSize(d);
		setMaximumSize(d);
		setMinimumSize(d);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		
		BufferedImage buffer = new BufferedImage(PANEL_WIDTH, PANEL_HEIGHT, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = (Graphics2D) buffer.getGraphics();
		

		g2d.setColor(Color.white);
		g2d.fillRect(0, 0, getWidth(), getHeight());
		drawWater(g2d);
		drawReef(g2d);
		drawBase(g2d);
		drawValidShipPositionSquares(g2d);
		drawSelectedShipHighlight(g2d);
		drawShips(g2d);
		
		g.drawImage(buffer, 0, 0, PANEL_WIDTH, PANEL_HEIGHT, null);
	}

	private void drawWater(Graphics2D g) {
		// Draw water everywhere
		for (int x = 0; x < PANEL_TILES_X; x++) {
			for (int y = 0; y < PANEL_TILES_Y; y++) {
				g.drawImage(waterImage, x * Constants.TILE_SIZE, y * Constants.TILE_SIZE, this);
			}
		}
	}

	private void drawReef(Graphics2D g) {
		// Draw reef
		for (int x = 0; x < Constants.CORAL_WIDTH; x++) {
			for (int y = 0; y < Constants.CORAL_HEIGHT; y++) {
				// Draw reef at (x,y) if present
				if (reefMask[x][y]) {
					// Calculate panel x and y position
					int xPos = (x + Constants.CORAL_X_OFFSET) * Constants.TILE_SIZE;
					int yPos = y * Constants.TILE_SIZE;
					g.drawImage(reefImage, xPos, yPos, null);
				}
			}
		}
	}

	private void drawBase(Graphics2D g) {
		// Draw base image on left of screen
		for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
			g.drawImage(ResourceManager.getInstance().getBaseImage(y + Constants.BASE_Y_OFFSET),
					0, (y + BASE_OFFSET) * Constants.TILE_SIZE, this);
		}
	}

	private void drawValidShipPositionSquares(Graphics2D g) {
		Color oldColor = g.getColor();
		g.setColor(Constants.SHIP_PLACE_OK_COLOR);

		// Draw valid ship position above base
		g.fillRect(0, (BASE_OFFSET - 1) * Constants.TILE_SIZE,
				Constants.TILE_SIZE, Constants.TILE_SIZE);

		// Draw valid ship position below base
		g.fillRect(0, (Constants.BASE_HEIGHT + BASE_OFFSET) * Constants.TILE_SIZE, 
				Constants.TILE_SIZE, Constants.TILE_SIZE);

		// Draw valid ship positions along right side of base
		for (int y = 0; y < Constants.BASE_HEIGHT; y++) {
			g.fillRect(Constants.TILE_SIZE, (y + BASE_OFFSET) * Constants.TILE_SIZE, 
					Constants.TILE_SIZE, Constants.TILE_SIZE);
		}

		// Set color back
		g.setColor(oldColor);
	}

	private void drawSelectedShipHighlight(Graphics2D g) {
		Color oldColor = g.getColor();
		g.setColor(Constants.SHIP_SELECTION_COLOR);

		int[] renderOffsets;

		// Draw a border around the selected ship
		if (selectedShip >= 0 && selectedShip < shipWidths.length) {
			renderOffsets = getShipRenderOffsets(shipPositions[selectedShip]);

			g.fillRect(renderOffsets[0], renderOffsets[1],
					shipWidths[selectedShip] * Constants.TILE_SIZE, Constants.TILE_SIZE);
		}

		// Set color back
		g.setColor(oldColor);
	}

	private void drawShips(Graphics2D g) {
		int[] renderOffsets;

		for (int i = 0; i < shipPositions.length; i++) {
			renderOffsets = getShipRenderOffsets(shipPositions[i]);
			drawShip(g, renderOffsets, shipWidths[i]);
		}
	}

	private void drawShip(Graphics2D g, int[] offsets, int shipWidth) {
		ResourceManager rm = ResourceManager.getInstance();
		BufferedImage headImage = (BufferedImage)rm.getHeadImage(Direction.RIGHT, 1, 1, true);
		g.drawImage(headImage, ((shipWidth - 1) * Constants.TILE_SIZE) + offsets[0], offsets[1], null);
		
		BufferedImage tailImage = (BufferedImage)rm.getTailImage(Direction.RIGHT, 1, 1, true);
		g.drawImage(tailImage, offsets[0], offsets[1], null);
		
		BufferedImage bodyImage = (BufferedImage)rm.getBodyImage(Direction.RIGHT, 1, 1);
		for (int i = 1; i < shipWidth - 1; i++) {
			g.drawImage(bodyImage, (i * Constants.TILE_SIZE) + offsets[0], offsets[1], null);
		}
	}

	/**
	 * This determines the x and y position of the top left corner for a given ship
	 * position.
	 * @param position The position of the ship starting at 0 (representing above the base)
	 * 	through Constants.BASE_HEIGHT + 1 for the slot below the base.
	 * @return an array with the X coordinate as the first element and the Y coordinate as the second.
	 */
	private int[] getShipRenderOffsets(int position) {
		int y = position - 1;
		int baseOffset = Constants.BASE_Y_OFFSET - Constants.CORAL_Y_OFFSET;
		return new int[] {
			(y == -1 || y == Constants.BASE_HEIGHT) ? 0 : Constants.TILE_SIZE,
			(y + baseOffset) * Constants.TILE_SIZE,
		};
	}
	
	/**
	 * This searches through all ships to see if there is a ship locted at a given
	 * point in the grid.
	 * @param x the x position clicked
	 * @param y the y position clicked
	 * @return the index of the ship at this position, or -1 if there isn't any
	 */
	private int getShipAtPosition(int x, int y) {
		// Iterate through each ship, testing if we could be in it's render zone
		int[] renderOffsets;
		for (int i = 0; i < shipPositions.length; i++) {
			renderOffsets = getShipRenderOffsets(shipPositions[i]);
			if (x >= renderOffsets[0] && y >= renderOffsets[1] &&
					x <= renderOffsets[0] + (shipWidths[i] * Constants.TILE_SIZE) &&
					y <= renderOffsets[1] + Constants.TILE_SIZE) {
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * This searches all slots where a ship can be placed to see if one was 
	 * clicked on.
	 * @param x the x position clicked
	 * @param y the y position clicked
	 * @return the base position clicked, or -1 if there was one.
	 */
	private int getSlotAtPosition(int x, int y) {
		int[] renderOffsets;
		// Iterate through all possible positions (+2 for top and bottom)
		for (int i = 0; i < Constants.BASE_HEIGHT + 2; i++) {
			renderOffsets = getShipRenderOffsets(i);
			if (x >= renderOffsets[0] && y >= renderOffsets[1] &&
					x <= renderOffsets[0] + Constants.TILE_SIZE &&
					y <= renderOffsets[1] + Constants.TILE_SIZE) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public void update(Observable o, Object arg) {
		// Repaint on update from the reef generator
		if (o == this.reefGenerator && this.isVisible()) {
			reefMask = reefGenerator.getReef();
			repaint();
		}
	}

	private class CoralMouseListener extends MouseAdapter {
		@Override
		public void mouseClicked(MouseEvent e) {
			// If we clicked a ship, select that one
			int shipAtPosition = getShipAtPosition(e.getX(), e.getY());
			if (shipAtPosition != -1) {
				selectedShip = shipAtPosition;
				repaint();
				return;
			}
			// If there was no ship at that position, check if we selected a slot.
			// If we did select a slot, and we had a selected ship, move it!
			int slotAtPosition = getSlotAtPosition(e.getX(), e.getY());
			if (selectedShip != -1 && slotAtPosition != -1) {
				shipPositions[selectedShip] = slotAtPosition;
				selectedShip = -1;
				
				// No longer ready!
				if (readyActionListener.isReady()) {
					readyActionListener.actionPerformed(null);
				}
				
				repaint();
				return;
			}
		}
	}

	@Override
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
		repaint();
		return true;
	}
	
	public int[] getShipPositions() {
		return shipPositions;
	}
}
