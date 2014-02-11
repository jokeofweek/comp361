package comp361.client.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Make things look baller.
 */
public class SwagFactory {

	public static final Font FONT = new Font("Consolas", Font.PLAIN, 16);
	
	public static BufferedImage LOGO_IMAGE;
	public static BufferedImage SMALL_LOGO_IMAGE;
	
	public static final int BUTTON_HEIGHT = 30;
	
	static {
		try {
			LOGO_IMAGE = ImageIO.read(new File("images/logo.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try {
			SMALL_LOGO_IMAGE = ImageIO.read(new File("images/logo_small.png"));
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private SwagFactory() {
	}

	public static void style(JComponent component) {
		if (component instanceof JButton) {
			styleButton((JButton) component);
		} else if (component instanceof JPanel) {
			stylePanel((JPanel) component);
		} else if (component instanceof JLabel) {
			styleLabel((JLabel) component);
		} else {
			// Do nothing for now
		}
	}

	private static void styleButton(JButton button) {
		button.setBackground(new Color(185, 225, 255));
		button.setFont(FONT);
	}

	private static void stylePanel(JPanel panel) {
		panel.setBackground(Color.white);
	}

	private static void styleLabel(JLabel label) {
		label.setFont(FONT);
	}

}
