package comp361.client.resources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import comp361.shared.Constants;
import comp361.shared.data.Direction;

public class ResourceManager {
	private static ResourceManager instance;

	private final String EVENT_FILENAME = "exclamation.png";
	
	private final String DEFAULT_STATE = null;
	private final String HIT_STATE = "hit";
	private final String DEAD_STATE = "dead";
	
	private final Direction[] directions = Direction.values();
	private final String[] colors = {"blue", "red"};
	private final String[] states = {DEFAULT_STATE, HIT_STATE, DEAD_STATE};
	private final String[] baseParts = {"base-top", "base-body", "base-bottom"};
	
	private final Image WATER_IMAGE = ImageManager.getInstance().getImage("bg-anim");
	private final Image MINE_IMAGE = ImageManager.getInstance().getImage("mine-anim");
	private final Image REEF_IMAGE = ImageManager.getInstance().getImage("reef");
	private final Map<String, Image> images = new HashMap<String, Image>();

	public static ResourceManager getInstance() {
		if (instance == null) {
			ImageManager.getInstance();
			SoundManager.getInstance();
			instance = new ResourceManager();
		}
		return instance;
	}

	private ResourceManager() {
		// Load all the resources
		try {
			loadHeadImages();
			loadBodyImages();
			loadTailImages();
			loadEventImage();
			loadBaseImages();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}

	public Image getWaterImage() {
		return WATER_IMAGE;
	}

	public Image getShipBodyImage(Direction dir, int health, int maxHealth) {
		String state = getState(health, maxHealth);
		String filename = getFilename("ship-body", dir.ordinal()+1, state);
		return images.get(filename);
	}

	public Image getShipHeadImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String filename = getFilename("ship-head", dir.ordinal()+1, isOwner, state);
		return images.get(filename);
	}
	
	public Image getShipTailImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String filename = getFilename("ship-tail", dir.ordinal()+1, isOwner, state);
		return images.get(filename);
	}
	
	public Image getReefImage() {
		return REEF_IMAGE;
	}
	
	public Image getEventImage() {
		return images.get(Constants.GFX_DATA_PATH + EVENT_FILENAME);
	}
	
	public Image getBaseImage(int y, boolean isDestroyed) {
		String name = "base-";
		
		switch (y) {
		case Constants.BASE_Y_OFFSET:
			name += "top";
			break;
		case Constants.BASE_Y_OFFSET + Constants.BASE_HEIGHT - 1:
			name += "bottom";
			break;
		default:
			name += "body";
		}
		
		name += "-anim";
		
		if (isDestroyed) {
			name += "-dead";
		}
		
		name += ".gif";
		return ImageManager.getInstance().getImage(name);
	}
	
	public Image getMineImage() {
		return MINE_IMAGE;
	}

	private void loadHeadImages() throws IOException {
		String filename = null;
		BufferedImage image = null;
		
		for (Direction dir : directions) {
			for (String state : states) {
				for (String color : colors) {
					filename = getFilename("ship-head", dir.ordinal()+1, color, state);
					image = ImageIO.read(new File(filename));
					images.put(filename, image);
				}
			}
		}
	}
	
	private void loadBodyImages() throws IOException {
		String filename = null;
		BufferedImage image = null;
		
		for (Direction dir : directions) {
			for (String state : states) {
				filename = getFilename("ship-body", dir.ordinal()+1, state);
				image = ImageIO.read(new File(filename));
				images.put(filename, image);
			}
		}
	}
	
	private void loadTailImages() throws IOException {
		String filename = null;
		BufferedImage image = null;
		
		for (Direction dir : directions) {
			for (String state : states) {
				for (String color : colors) {
					filename = getFilename("ship-tail", dir.ordinal()+1, color, state);
					image = ImageIO.read(new File(filename));
					images.put(filename, image);
				}
			}
		}
	}
	
	private void loadEventImage() throws IOException {
		String filename = Constants.GFX_DATA_PATH + EVENT_FILENAME;
		BufferedImage image = ImageIO.read(new File(filename));
		images.put(filename, image);
	}
	
	private void loadBaseImages() throws IOException {
		String filename = null;
		Image image = null;
		
		for (String part : baseParts) {
			filename = Constants.GFX_DATA_PATH + part + "-anim.gif";
			image = new ImageIcon(filename).getImage();
			images.put(filename, image);
		}
	}

	private String getFilename(String part, int variant, String state) {
		return getFilename(part, variant, null, state);
	}
	
	private String getFilename(String part, int variant, boolean isOwner, String state) {
		String color = isOwner ? "blue" : "red";
		return getFilename(part, variant, color, state);
	}
	
	private String getFilename(String filename, int variant, String color, String state) {
		if (color != null) {
			filename += "-" + color;
		}
		
		if (variant > 0) {
			filename += "-" + variant;
		}
		
		if (state != null) {
			filename += "-" + state;
		}
		
		return Constants.GFX_DATA_PATH + filename + ".png";
	}
	
	private String getState(int health, int maxHealth) {
		if (health == maxHealth) {
			return DEFAULT_STATE;
		} else if (health == 0) {
			return DEAD_STATE;
		} else {
			return HIT_STATE;
		}
	}
}