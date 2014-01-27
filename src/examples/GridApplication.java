package examples;

import java.awt.BorderLayout;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GridApplication extends JFrame{

	private JPanel container;
	
	public GridApplication() throws IOException {
		container = new JPanel(new BorderLayout());
		
		JPanel buttonContainer = new JPanel();
		buttonContainer.add(new JButton("Test"));
		container.add(buttonContainer, BorderLayout.WEST);
		container.add(new GridPanel());
		
		add(container);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		pack();
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					new GridApplication();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}
	
}
