package comp361.shared;

import java.awt.Color;
import java.nio.charset.Charset;

public class Constants {
	public static final Charset CHARSET = Charset.forName("UTF-8");
	public static final int PORT = 5000;
	public static final String DATA_PATH = "./data/";
	public static final String CLIENT_DATA_PATH = DATA_PATH + "client/";
	public static final String GFX_DATA_PATH = CLIENT_DATA_PATH + "gfx/";
	public static final String SFX_DATA_PATH = CLIENT_DATA_PATH + "sfx/";
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
	
	
	public static final Color SHIP_SELECTION_COLOR = new Color(1.0f, 1.0f, 0f, 0.5f);
	public static final Color SHIP_PLACE_OK_COLOR = new Color(1.0f, 1.0f, 0f, 0.1f);
	public static final Color MOVE_COLOR = new Color(0.0f, 0.0f, 1.0f, 0.5f);
	public static final Color MOVE_HOVER_COLOR = new Color(1.0f, 1.0f, 1.0f, 0.5f);
	public static final Color MOVE_BORDER_COLOR = new Color(0.0f, 0.0f, 0.0f, 0.5f);
	public static final float SUNKEN_SHIP_ALPHA = 0.3f;
	public static final Color HEALTH_BAR_BACKGROUND = new Color(0.922f, 0.216f, 0, 1);
	public static final Color HEALTH_BAR_FOREGROUND = new Color(0.275f, 0.549f, 0.322f, 1);
	public static final Color GAME_EVENT_COLOR = new Color(1.0f, (152 / 255.0f), 0.0f, 0.70f);
	
	public static final int PULSE_ALPHA_INTERVALS = 10;
	public static final float PULSE_ALPHA_DELTA = 0.025f;
	public static final float SONAR_RED = 1.0f;
	public static final float SONAR_GREEN = 0.0f;
	public static final float SONAR_BLUE = 0.0f;
	public static final float SONAR_ALPHA = 0.1f;
	
	
	public static final int NUM_PLAYERS = 2;
}
