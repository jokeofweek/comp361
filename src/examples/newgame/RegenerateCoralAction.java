package examples.newgame;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingUtilities;

/**
 * This action takes care of regenerating a coral reef on click.
 * By using an action, we can also disable this once a player
 * has marked they are ready.
 */
public class RegenerateCoralAction extends AbstractAction {

	private CoralReefGenerator reefGenerator;
	
	public RegenerateCoralAction(CoralReefGenerator reefGenerator) {
		this.reefGenerator = reefGenerator;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// On click, we want to regenerate the reef
		reefGenerator.regenerateReef();
	}

}
