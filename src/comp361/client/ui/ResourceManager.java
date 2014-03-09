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
	
	private final Direction[] directions = Direction.values();
	private final String[] colors = {"blue", "red"};
	private final String[] states = {null, "hit"};
	
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
		String filename = getFilename("body", s.getDirection(), state);
		return images.get(filename);
	}

	public BufferedImage getHeadImage(Ship s, boolean isOwner) {
		String state = s.getHealth(0) < s.getMaxHealthPerSquare() ? "hit" : null;
		String filename = getFilename("head", s.getDirection(), isOwner, state);
		return images.get(filename);
	}
	
	public BufferedImage getTailImage(Ship s, boolean isOwner) {
		String state = s.getHealth(s.getSize() - 2) < s.getMaxHealthPerSquare() ? "hit" : null;
		String filename = getFilename("tail", s.getDirection(), isOwner, state);
		return images.get(filename);
	}
	
	public BufferedImage getReefImage() {
		String filename = Constants.GFX_DATA_PATH + REEF_FILENAME;
		return images.get(filename);
	}

	private void loadHeadImages() throws IOException {
		String filename = null;
		BufferedImage image = null;
		
		for (Direction dir : directions) {
			for (String state : states) {
				for (String color : colors) {
					filename = getFilename("head", dir, color, state);
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
				filename = getFilename("body", dir, state);
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
					filename = getFilename("tail", dir, color, state);
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

	private String getFilename(String part, Direction dir, String state) {
		return getFilename(part, dir, null, state);
	}
	
	private String getFilename(String part, Direction dir, boolean isOwner, String state) {
		String color = isOwner ? "blue" : "red";
		return getFilename(part, dir, color, state);
	}
	
	private String getFilename(String part, Direction dir, String color, String state) {
		String filename = "ship-" + part;
		
		if (color != null) {
			filename += "-" + color;
		}
		
		filename += "-" + (dir.ordinal()+1);
		
		if (state != null) {
			filename += "-" + state;
		}
		
		return Constants.GFX_DATA_PATH + filename + ".png";
	}
}
