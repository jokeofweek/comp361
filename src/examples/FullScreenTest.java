package examples;

import java.awt.Dimension;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import javax.swing.JFrame;

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
	}
	
	public static void main(String[] args) {
		new FullScreenTest();
	}
}
