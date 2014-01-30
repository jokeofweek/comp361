package examples.newgame;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class NewGamePanel extends JPanel {

	private CoralReefGenerator reefGenerator;
	
	private RegenerateCoralAction regenerateCoralAction;
	private ReadyAction readyAction;
	
	private static final int PANEL_SPACING = 10;
	
	public NewGamePanel() {
		super(new BorderLayout());
		
		this.reefGenerator = new CoralReefGenerator();
		this.regenerateCoralAction = new RegenerateCoralAction(reefGenerator);
		this.readyAction = new ReadyAction(regenerateCoralAction);
		
		// Setup the UI
		this.add(this.buildCoralUI(), BorderLayout.WEST);
		this.add(this.buildOptionsUI());
	}
	
	private JPanel buildCoralUI() {
		// Wrap the coral panel in another panel so that we can put a border on
		JPanel coralContainer = new JPanel(new BorderLayout());
		
		// Build the panel which actually holds the coral reef.
		CoralPanel coralPanel = new CoralPanel(reefGenerator);
		coralContainer.add(coralPanel);
		
		// Build the button for regenerating the coral
		JButton regenerateCoralButton = new JButton(regenerateCoralAction);
		regenerateCoralButton.setText("Regenerate Corals");
		coralContainer.add(regenerateCoralButton, BorderLayout.SOUTH);
		
		// Wrap the coral panel container with spacing since apparently
		// you can't put a border on CoralPanel?
		coralContainer.setBorder(BorderFactory.createEmptyBorder(
				PANEL_SPACING,
				PANEL_SPACING,
				PANEL_SPACING,
				PANEL_SPACING));
		return coralContainer;
	}
	
	private JPanel buildOptionsUI() {
		JPanel optionsContainer = new JPanel(new BorderLayout());
		
		optionsContainer.add(buildChatUI());
		
		// Build the ready button
		JButton readyButton = new JButton(readyAction);
		readyButton.setText("Ready");
		optionsContainer.add(readyButton, BorderLayout.SOUTH);

		optionsContainer.setBorder(BorderFactory.createEmptyBorder(
				PANEL_SPACING,
				PANEL_SPACING,
				PANEL_SPACING,
				PANEL_SPACING));
		
		return optionsContainer;
	}
	
	private JPanel buildChatUI() {
		JPanel chatContainer = new JPanel(new BorderLayout());
		
		chatContainer.add(new JTextArea());
		chatContainer.add(new JTextField(), BorderLayout.SOUTH);
		
		return chatContainer;
	}

	
}
