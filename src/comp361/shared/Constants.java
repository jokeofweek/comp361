package comp361.shared;

import java.nio.charset.Charset;

public class Constants {
	public static final Charset CHARSET = Charset.forName("UTF-8");
	public static final int PORT = 5000;
	public static final String DATA_PATH = "./data/";
	public static final String SERVER_DATA_PATH = DATA_PATH + "server/";
	
	// Screen size
	public static final int SCREEN_WIDTH = 800;
	public static final int SCREEN_HEIGHT = 600;
	
	// Tile sizes
	public static final int TILE_WIDTH = 24;
	public static final int TILE_HEIGHT = 24;
}
