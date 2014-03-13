package comp361.client.resources;

import java.awt.Image;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import comp361.shared.Constants;

public class ImageManager {
	private static ImageManager instance;
	private Map<String, Image> images;
	
	public static synchronized ImageManager getInstance() {
		if (instance == null) {
			instance = new ImageManager();
		}
		
		return instance;
	}
	
	private ImageManager() {
		// Initialize image store
		this.images = new HashMap<String, Image>();
		
		// Load all images from graphics directory
		File resourceDir = new File(Constants.GFX_DATA_PATH);
		
		if (resourceDir != null && resourceDir.isDirectory()) {
			Image image;
			
			for (String resourceName : resourceDir.list()) {
				// Skip files that are not images
				if (!isImage(resourceName)) {
					continue;
				}
				
				// Store image
				String path = Constants.GFX_DATA_PATH + resourceName;
				image = new ImageIcon(path).getImage();
				images.put(resourceName.replaceFirst("\\.(png|gif)", ""), image);
			}
		}
	}
	
	public Image getImage(String name) {
		Image result = images.get(name.replaceFirst("\\.(png|gif)", ""));
		
		// Default to unknown image (which, ironically, could also fail)
		if (result == null) {
			result = images.get("unknown");
		}
		
		return result;
	}
	
	private boolean isImage(String name) {
		return name.matches(".*\\.(png|gif)$");
	}
}
