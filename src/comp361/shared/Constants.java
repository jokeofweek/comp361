package comp361.shared;

import java.awt.Color;
import java.nio.charset.Charset;

public class Constants {
	public static final Charset CHARSET = Charset.forName("UTF-8");
	public static final int PORT = 5000;
	public static final String DATA_PATH = "./data/";
	public static final String CLIENT_DATA_PATH = DATA_PATH + "client/";
	public static final String GFX_DATA_PATH = CLIENT_DATA_PATH + "gfx/";
	public static final String SERVER_DATA_PATH = DATA_PATH + "server/";
	
	// Screen size
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	// Tile constants
	public static final int TILE_SIZE = 20;
	
	// Map shape constants
	public static final int MAP_WIDTH = 30;
	public static final int MAP_HEIGHT = 30;
	public static final int CORAL_WIDTH = 10;
	public static final int CORAL_HEIGHT = 24;
	public static final int CORAL_X_OFFSET = 10;
	public static final int CORAL_Y_OFFSET = 3;
	public static final int BASE_HEIGHT = 10;
	public static final int BASE_Y_OFFSET = 10;
	
	// Color constants
	public static final Color FOG_OF_WAR_COLOR = new Color(0f, 0f, 0f, 0.5f);
	public static final Color SONAR_COLOR = new Color(0f, 1.0f, 0f, 0.1f);
	public static final Color SHIP_SELECTION_COLOR = new Color(1.0f, 1.0f, 0f, 0.5f);
	public static final Color MOVE_COLOR = new Color(0.0f, 0.0f, 1.0f, 0.5f);
	public static final Color MOVE_BORDER_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.5f);
}
