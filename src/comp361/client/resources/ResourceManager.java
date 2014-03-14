package comp361.client.resources;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;

import comp361.shared.Constants;
import comp361.shared.data.Direction;

public class ResourceManager {
	private static ResourceManager instance;
	
	private final String ANIM = "anim";
	private final String BASE = "base";
	private final String BLUE = "blue";
	private final String BODY = "body";
	private final String BOTTOM = "bottom";
	private final String DASH = "-";
	private final String DEAD = "dead";
	private final String HEAD = "head";
	private final String HIT = "hit";
	private final String RED = "red";
	private final String SHIP = "ship";
	private final String TAIL = "tail";
	private final String TOP = "top";
	
	// Preload images that don't change
	private final Image MINE_IMAGE = ImageManager.getInstance().getImage("mine-anim");
	private final Image POI_IMAGE = ImageManager.getInstance().getImage("poi");
	private final Image REEF_IMAGE = ImageManager.getInstance().getImage("reef");
	private final Image WATER_IMAGE = ImageManager.getInstance().getImage("bg-anim");

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
		return ImageManager.getInstance().getImage(join(SHIP, BODY, direction, state));
	}

	public Image getShipHeadImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String direction = String.valueOf(dir.ordinal() + 1);
		String color = getColor(isOwner);
		return ImageManager.getInstance().getImage(join(SHIP, HEAD, color, direction, state));
	}
	
	public Image getShipTailImage(Direction dir, int health, int maxHealth, boolean isOwner) {
		String state = getState(health, maxHealth);
		String direction = String.valueOf(dir.ordinal() + 1);
		String color = getColor(isOwner);
		return ImageManager.getInstance().getImage(join(SHIP, TAIL, color, direction , state));
	}
	
	public Image getBaseImage(int y, boolean isDestroyed) {
		String state = isDestroyed ? DEAD : null;
		String part;
		
		if (y == Constants.BASE_Y_OFFSET) {
			part = TOP;
		} else if (y == Constants.BASE_Y_OFFSET) {
			part = BOTTOM;
		} else {
			part = BODY;
		}
		
		return ImageManager.getInstance().getImage(join(BASE, part, ANIM, state));
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
			
			if (i < strings.length - 1) {
				sb.append(DASH);
			}
		}
		
		return sb.toString();
	}
}
