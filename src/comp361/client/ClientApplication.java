package comp361.client;

import javax.swing.SwingUtilities;

import comp361.client.ui.StartWindow;

public class ClientApplication {

	public static void main(String[] args) {
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
