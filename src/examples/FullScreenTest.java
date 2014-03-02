package examples;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

import comp361.client.ui.setup.NewGamePanel;


public class FullScreenTest extends JFrame {
	public FullScreenTest() {
		Dimension d = new Dimension(800, 600);
		setSize(d);
		setPreferredSize(d);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice device = ge.getDefaultScreenDevice();
		this.setUndecorated(true);
		this.setResizable(false);
		device.setFullScreenWindow(this);
		device.setDisplayMode(new DisplayMode(800, 600, 32, DisplayMode.REFRESH_RATE_UNKNOWN));
		
		//this.add(new NewGamePanel());
		this.setVisible(true);
	}
	
	public static void main(String[] args) {
		new FullScreenTest();
	}
}
