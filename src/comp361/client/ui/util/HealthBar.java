package comp361.client.ui.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import comp361.shared.Constants;

public abstract class HealthBar extends JComponent implements Observer {

	private int height;
	
	public HealthBar(int height) {
		this.height = height;
		setMaximumSize(new Dimension(Integer.MAX_VALUE, height));
		setMinimumSize(new Dimension(50, height));
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public Dimension getSize() {
		return new Dimension(super.getSize().width, height);
	}
	
	@Override
	public Dimension getPreferredSize() {
		return new Dimension(super.getSize().width, height);
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(Constants.HEALTH_BAR_BACKGROUND);
		g.fillRect(0, 0, getWidth(), getHeight());
		g.setColor(Constants.HEALTH_BAR_FOREGROUND);
		g.fillRect(0, 0, Math.round(getWidth() * ((float)getValue() / getMaxValue())), getHeight());
	}
	
	public abstract int getValue();
	public abstract int getMaxValue();
	
	@Override
	public void update(Observable o, Object arg) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				repaint();
			}
		});
	}
	
	
}
