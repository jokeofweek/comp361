package examples.newgame;

import java.awt.Dimension;

import javax.swing.JFrame;

import comp361.shared.Constants;

public class Application {
	public static void main(String[] args) {
		JFrame frame = new JFrame();
		Dimension d = new Dimension(Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT);
		frame.setSize(d);
		frame.setPreferredSize(d);
		frame.add(new NewGamePanel());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);
	}
}
