package comp361.client.ui;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.imageio.ImageIO;

import comp361.shared.Constants;
import comp361.shared.data.Direction;

public class ResourceManager {

	private static ResourceManager instance;
	private static final String WATER_FILE_NAME = "bg.png";
	private static final String SHIP_PART_FILE_NAME = "ship-<part>-<dir>.png";

	private final List<String> parts = Arrays.asList("body", "head", "tail");
	private final int BODY_INDEX = parts.indexOf("body");
	private final int HEAD_INDEX = parts.indexOf("head");
	private final int TAIL_INDEX = parts.indexOf("tail");

	private BufferedImage waterImage;
	private BufferedImage[][] shipImages;

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}

	private ResourceManager() {
		// Load all the resources
		try {
			waterImage = ImageIO.read(new File(Constants.GFX_DATA_PATH
					+ WATER_FILE_NAME));

			// Load all body images
			shipImages = new BufferedImage[parts.size()][Direction.values().length];
			int count = 0;
			for (String part : parts) {
				for (Direction d : Direction.values()) {
					shipImages[count][d.ordinal()] = ImageIO.read(new File(
							Constants.GFX_DATA_PATH
									+ SHIP_PART_FILE_NAME.replace("<dir>",
											"" + (d.ordinal() + 1)).replace(
											"<part>", part)));
				}
				count++;
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

	public BufferedImage getWaterImage() {
		return waterImage;
	}

	public BufferedImage getBodyImage(Direction dir) {
		return shipImages[BODY_INDEX][dir.ordinal()];
	}

	public BufferedImage getHeadImage(Direction dir) {
		return shipImages[HEAD_INDEX][dir.ordinal()];
	}
	
	public BufferedImage getTailImage(Direction dir) {
		return shipImages[TAIL_INDEX][dir.ordinal()];
	}
}
