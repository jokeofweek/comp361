package comp361.client;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import comp361.client.ui.StartWindow;

public class ClientApplication {

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			// Could not load cross-platform look and feel
		}
		
		// Show the start window!
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				StartWindow startWindow = new StartWindow();
				startWindow.setVisible(true);
			}
		});
	}

}
