package examples.newgame;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class NewGamePanel extends JPanel {

	private CoralReefGenerator reefGenerator;
	private RegenerateCoralAction regenerateCoralAction;
	
	private static final int CORAL_PANEL_SPACING = 10;
	
	public NewGamePanel() {
		super(new BorderLayout());
		
		this.reefGenerator = new CoralReefGenerator();
		this.regenerateCoralAction = new RegenerateCoralAction(reefGenerator);
		
		// Setup the UI
		this.buildUI();
	}
	
	private void buildUI() {
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
				CORAL_PANEL_SPACING,
				CORAL_PANEL_SPACING,
				0,
				CORAL_PANEL_SPACING));
		this.add(coralContainer, BorderLayout.WEST);
		
		
		this.add(new JLabel("ABC"));
	}

	
}
