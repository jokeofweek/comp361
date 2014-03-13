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
	
	private final String WATER_FILENAME = "bg-anim.gif";
	private final String REEF_FILENAME = "reef.png";
	private final String MINE_FILENAME = "mine-anim.gif";
	
	private final String DEFAULT_STATE = null;
	private final String HIT_STATE = "hit";
	private final String DEAD_STATE = "dead";
	
	private final Direction[] directions = Direction.values();
	private final String[] colors = {"blue", "red"};
	private final String[] states = {DEFAULT_STATE, HIT_STATE, DEAD_STATE};
	private final String[] baseParts = {"base-top", "base-body", "base-bottom"};
	
	private final Image waterImage = new ImageIcon(Constants.GFX_DATA_PATH + WATER_FILENAME).getImage();
	private final Image mineImage = new ImageIcon(Constants.GFX_DATA_PATH + MINE_FILENAME).getImage();
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
			loadReefImage();
			loadBaseImages();
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(1);
		}

	}

	public Image getWaterImage() {
		return waterImage;
	}

	public Image getBodyImage(Direction dir, int health, int maxHealth) {
		String state = getState(health, maxHealth);
		String filename = getFilename("ship-body", dir.ordinal()+1, state);
		return images.get(filename);
	}

	public Image getHeadImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String filename = getFilename("ship-head", dir.ordinal()+1, isOwner, state);
		return images.get(filename);
	}
	
	public Image getTailImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String filename = getFilename("ship-tail", dir.ordinal()+1, isOwner, state);
		return images.get(filename);
	}
	
	public Image getReefImage() {
		String filename = Constants.GFX_DATA_PATH + REEF_FILENAME;
		return images.get(filename);
	}
	
	public Image getBaseImage(int y) {
		String filename = Constants.GFX_DATA_PATH + "base-";
		
		switch (y) {
		case Constants.BASE_Y_OFFSET:
			
			filename += "top";
			break;
		case Constants.BASE_Y_OFFSET + Constants.BASE_HEIGHT - 1:
			filename += "bottom";
			break;
		default:
			filename += "body";
		}
		
		filename += "-anim.gif";
		return images.get(filename);
	}
	
	public Image getMineImage() {
		return mineImage;
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
	
	private void loadReefImage() throws IOException {
		String filename = Constants.GFX_DATA_PATH + REEF_FILENAME;
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
