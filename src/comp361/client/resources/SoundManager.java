package comp361.client.resources;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import comp361.shared.Constants;

public class SoundManager {
	private static SoundManager instance;
	private Map<String, Clip> sounds;
	
	public static synchronized SoundManager getInstance() {
		if (instance == null) {
			instance = new SoundManager();
		}
		
		return instance;
	}
	
	private SoundManager() {
		// Initialize sound store
		this.sounds = new HashMap<String, Clip>();
		
		// Load all sounds from sound effects directory
		File resourceDir = new File(Constants.SFX_DATA_PATH);
		
		if (resourceDir != null && resourceDir.isDirectory()) {
			Clip clip;
			
			for (String resourceName : resourceDir.list()) {
				// Skip files that are not sounds
				if (!isSound(resourceName)) {
					continue;
				}
				
				String path = Constants.SFX_DATA_PATH + resourceName;
				File resourceFile = new File(path);
				
				try {
					// Get URL to resource
					URL url = resourceFile.toURI().toURL();
					
					// Set up audio stream for the resource
					AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(url);
					
					
					// Get a clip and load the audio
					clip = AudioSystem.getClip();
					clip.open(audioInputStream);
					
					// Insert clip into sound store
					sounds.put(resourceName.replaceFirst("\\.(wav)$", ""), clip);
				} catch (Exception e) {
					// Shit happens
				}
			}
		}
	}
	
	public void play(String name) {
		Clip clip = sounds.get(name);
		
		if (clip == null) {
			return;
		}
		
		// Stop clip if it is playing already
		if (clip.isRunning()) {
			clip.stop();
		}
		
		// Play from beginning
		clip.setFramePosition(0);
		clip.start();
	}
	
	private boolean isSound(String name) {
		return name.matches(".*\\.(wav)$");
	}
}
