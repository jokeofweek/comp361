package comp361.client.ui.setup;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ReadyAction extends AbstractAction {

	private RegenerateCoralAction regenerateAction;

	public ReadyAction(RegenerateCoralAction regenerateAction) {
		this.regenerateAction = regenerateAction;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		// Disable the regenerate action and this action when we're ready
		this.regenerateAction.setEnabled(false);
		this.setEnabled(false);
	}	
	
}
