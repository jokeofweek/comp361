package comp361.client.resources;

import java.io.File;
import java.io.FileInputStream;

import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackEvent;
import javazoom.jl.player.advanced.PlaybackListener;

import comp361.shared.Constants;

/**
 * MP3 audio player adapted from
 * http://thiscouldbebetter.wordpress.com/2011/06/14/playing-an-mp3-from-java-using-jlayer/
 */
public class BackgroundPlayer extends PlaybackListener {
	private AdvancedPlayer player;
	private File audioFile;

	public BackgroundPlayer() {
		audioFile = new File(Constants.BACKGROUND_MUSIC_PATH);
	}

	public void play() {
		try {
			this.player = new AdvancedPlayer(
				new FileInputStream(this.audioFile),
			    javazoom.jl.player.FactoryRegistry.systemRegistry().createAudioDevice()
			);
			
			this.player.setPlayBackListener(this);
			this.player.play();
		} catch (Exception e) {
			// Could not play audio
		}
    }

	public void playbackStarted(PlaybackEvent playbackEvent) {
	}

	public void playbackFinished(PlaybackEvent playbackEvent) {
		this.play();
	}
}