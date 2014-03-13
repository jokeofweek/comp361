package comp361.client.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Make things look baller.
 */
public class SwagFactory {

	public static Font FONT;
	public static float FONT_SIZE = 18f;
	
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
		
		File font_file = new File("fonts/SF Pixelate Bold.ttf");
		try {
			FONT = Font.createFont(Font.TRUETYPE_FONT, font_file).deriveFont(FONT_SIZE);
		} catch (FontFormatException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	private SwagFactory() {
		
	}

	public static void style(Component component) {
		if (component instanceof JButton) {
			styleButton((JButton) component);
		} else if (component instanceof JPanel) {
			stylePanel((JPanel) component);
		} else if (component instanceof JLabel) {
			styleLabel((JLabel) component);
		} else if(component instanceof JTextField) {
			styleTextField((JTextField) component);
		} else if (component instanceof JComboBox) {
			styleComboBox((JComboBox) component);
		} else if(component instanceof JEditorPane){
			styleEditorPane((JEditorPane) component);
		}
		else {
			//do nothing
		}
	}

	public static void styleButtonHeight(JComponent button, int width) {
		Dimension d = new Dimension(width, SwagFactory.BUTTON_HEIGHT);
		button.setMaximumSize(d);
		button.setMinimumSize(d);
		button.setPreferredSize(d);
		button.setSize(d);
	}
	
	private static void styleEditorPane(JEditorPane pane) {
		pane.setFont(FONT);
	}
	
	private static void styleTextField(JTextField field) {
		field.setFont(FONT);
	}
	
	private static void styleComboBox(JComboBox field) {
		field.setBackground(Color.WHITE);
		field.setFont(FONT);
	}
	
	private static void styleButton(JButton button) {
		styleButtonWithoutHeight(button);
		styleButtonHeight(button, button.getWidth());
	}
	
	public static void styleButtonWithoutHeight(JButton button) {
		button.setBackground(new Color(185, 225, 255));
		button.setFont(FONT);
	}

	private static void stylePanel(JPanel panel) {
		panel.setBackground(Color.white);
		for(Component c : panel.getComponents())
			style(c);
	}

	private static void styleLabel(JLabel label) {
		label.setFont(FONT);
	}

}
