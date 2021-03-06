package comp361.client.resources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import comp361.shared.Constants;
import comp361.shared.data.Direction;

public class ResourceManager {
	private static ResourceManager instance;
	private static final ImageManager images = ImageManager.getInstance();
	private static final String BASE = "base";
	private static final String BLUE = "blue";
	private static final String BODY = "body";
	private static final String BOTTOM = "bottom";
	private static final String DASH = "-";
	private static final String DEAD = "dead";
	private static final String HEAD = "head";
	private static final String HIT = "hit";
	private static final String KAMIKAZE = "kamikaze";
	private static final String RADAR = "radar";
	private static final String RED = "red";
	private static final String SHIP = "ship";
	private static final String TAIL = "tail";
	private static final String TOP = "top";

	// Preload images that don't change
	private final Image MINE_IMAGE = images.getImage("mine");
	private final Image POI_IMAGE = images.getImage("poi");
	private final Image REEF_IMAGE = images.getImage("reef");
	private final Image WATER_IMAGE = images.getImage("water");

	public static ResourceManager getInstance() {
		if (instance == null) {
			instance = new ResourceManager();
		}
		return instance;
	}

	private ResourceManager() {
		// Chillin' like a villain
	}

	public Image getMineImage() {
		return MINE_IMAGE;
	}

	public Image getEventImage() {
		return POI_IMAGE;
	}

	public Image getReefImage() {
		return REEF_IMAGE;
	}

	public Image getWaterImage() {
		return WATER_IMAGE;
	}

	public Image getShipBodyImage(Direction dir, int health, int maxHealth) {
		String state = getState(health, maxHealth);
		String direction = String.valueOf(dir.ordinal() + 1);
		return images.getImage(join(SHIP, BODY, direction, state));
	}

	public Image getRadarBodyImage(Direction dir, int health, int maxHealth) {
		String state = getState(health, maxHealth);
		String direction = String.valueOf(dir.ordinal() + 1);
		return images.getImage(join(RADAR, BODY, direction, state));
	}

	public Image getShipHeadImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String direction = String.valueOf(dir.ordinal() + 1);
		String color = getColor(isOwner);
		return images.getImage(join(SHIP, HEAD, color, direction, state));
	}

	public Image getShipTailImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String direction = String.valueOf(dir.ordinal() + 1);
		String color = getColor(isOwner);
		return images.getImage(join(SHIP, TAIL, color, direction, state));
	}

	public Image getKamikazeImage(Direction dir, boolean isOwner) {
		String direction = String.valueOf(dir.ordinal() + 1);
		String color = getColor(isOwner);
		return images.getImage(join(KAMIKAZE, color, direction));
	}

	public Image getBaseImage(int y, boolean isDestroyed) {
		String state = isDestroyed ? DEAD : null;
		String part;

		if (y == Constants.BASE_Y_OFFSET) {
			part = TOP;
		} else if (y == Constants.BASE_Y_OFFSET + Constants.BASE_HEIGHT - 1) {
			part = BOTTOM;
		} else {
			part = BODY;
		}

		return images.getImage(join(BASE, part, state));
	}

	public static BufferedImage toBufferedImage(Image image, ImageObserver o) {
		int size = Constants.TILE_SIZE;
		BufferedImage buffer = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
		buffer.createGraphics().drawImage(image, 0, 0, size, size, o);
		return buffer;
	}

	private String getState(final int health, final int maxHealth) {
		if (health == maxHealth) {
			return null;
		} else if (health == 0) {
			return DEAD;
		} else {
			return HIT;
		}
	}

	private String getColor(boolean isOwner) {
		return isOwner ? BLUE : RED;
	}

	private String join(String... strings) {
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < strings.length; i++) {
			if (strings[i] == null) {
				continue;
			}

			sb.append(strings[i]);

			if (i < strings.length - 1 && strings[i+1] != null) {
				sb.append(DASH);
			}
		}

		return sb.toString();
	}
}
