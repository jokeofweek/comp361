package comp361.client.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import comp361.shared.Constants;
import comp361.shared.data.Direction;
import comp361.shared.data.Ship;

public class ResourceManager {
	private static ResourceManager instance;
	
	private final String WATER_FILENAME = "bg-anim.gif";
	private final String REEF_FILENAME = "reef.png";
	private final String MINE_FILENAME = "mine.png";
	
	private final Direction[] directions = Direction.values();
	private final String[] colors = {"blue", "red"};
	private final String[] states = {null, "hit"};
	private final String[] baseParts = {"base-top", "base-body", "base-bottom"};
	
	private final ImageIcon waterImage = new ImageIcon(Constants.GFX_DATA_PATH + WATER_FILENAME);
	private final Map<String, BufferedImage> images = new HashMap<String, BufferedImage>();

	public static ResourceManager getInstance() {
		if (instance == null) {
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
			loadMineImage();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public ImageIcon getWaterImage() {
		return waterImage;
	}

	public BufferedImage getBodyImage(Ship s, int index) {
		String state = s.getHealth(index) < s.getMaxHealthPerSquare() ? "hit" : null;
		String filename = getFilename("ship-body", s.getDirection().ordinal()+1, state);
		return images.get(filename);
	}

	public BufferedImage getHeadImage(Ship s, boolean isOwner) {
		String state = s.getHealth(s.getSize() - 1) < s.getMaxHealthPerSquare() ? "hit" : null;
		String filename = getFilename("ship-head", s.getDirection().ordinal()+1, isOwner, state);
		return images.get(filename);
	}
	
	public BufferedImage getTailImage(Ship s, boolean isOwner) {
		String state = s.getHealth(0) < s.getMaxHealthPerSquare() ? "hit" : null;
		String filename = getFilename("ship-tail", s.getDirection().ordinal()+1, isOwner, state);
		return images.get(filename);
	}
	
	public BufferedImage getReefImage() {
		String filename = Constants.GFX_DATA_PATH + REEF_FILENAME;
		return images.get(filename);
	}
	
	public BufferedImage getBaseImage(int y) {
		String filename;
		
		switch (y) {
		case Constants.BASE_Y_OFFSET:
			filename = getFilename("base-top", -1, null);
			break;
		case Constants.BASE_Y_OFFSET + Constants.BASE_HEIGHT - 1:
			filename = getFilename("base-bottom", -1, null);
			break;
		default:
			filename = getFilename("base-body", -1, null);
		}
		
		return images.get(filename);
	}
	
	public BufferedImage getMineImage() {
		String filename = getFilename("mine", -1, null);
		return images.get(filename);
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
		BufferedImage image = null;
		
		for (String part : baseParts) {
			filename = getFilename(part, -1, null);
			image = ImageIO.read(new File(filename));
			images.put(filename, image);
		}
	}
	
	private void loadMineImage() throws IOException {
		String filename = Constants.GFX_DATA_PATH + MINE_FILENAME;
		BufferedImage image = ImageIO.read(new File(filename));
		images.put(filename, image);
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
}
