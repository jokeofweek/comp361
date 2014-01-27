package examples;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

public class GridPanel extends JPanel {
	
	private BufferedImage grass;
	private final int WIDTH = 640;
	private final int HEIGHT = 480;
	
	public GridPanel() throws IOException {
		grass = ImageIO.read(new File("data/client/gfx/grass.png"));
		
		Dimension d = new Dimension(WIDTH, HEIGHT);
		setSize(d);
		setPreferredSize(d);
	}
	
	@Override
	public void paint(Graphics g) {
		int tileWidth = grass.getWidth();
		int tileHeight = grass.getHeight();
		System.out.println("Paint");
		for (int x = 0; x < this.getWidth() / tileWidth; x++) {
			for (int y = 0; y < this.getHeight() / tileHeight; y++) {
				g.drawImage(grass, x * tileWidth, y * tileHeight, tileWidth, tileHeight, null);
			}
		}
		
		
	}

}
