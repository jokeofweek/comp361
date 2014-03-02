package comp361.client.ui.setup;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

public class ReadyAction extends AbstractAction {

	@Override
	public void actionPerformed(ActionEvent e) {
		// Disable the regenerate action and this action when we're ready
		this.setEnabled(false);
	}	
	
}
