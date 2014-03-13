package comp361.client.ui.lobby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import comp361.client.ui.SwagFactory;

public class InvitePanel extends JPanel {

	private static final int HEIGHT = 125;
	private static final int BUTTON_SPACING = 5;
	
	private String name;
	
	public InvitePanel(String name) {
		this.name = name;
		
		this.build();
	}
	
	private void build() {
		setLayout(new BorderLayout());
		SwagFactory.style(this);
		
		setBorder(BorderFactory.createLineBorder(Color.gray));
		
		Dimension d = new Dimension(InviteOverlayPanel.WIDTH, HEIGHT);
		setSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setPreferredSize(d);
		
		// Create the hide button
		JButton hideButton = new JButton("Hide");
		SwagFactory.style(hideButton);
		hideButton.setHorizontalAlignment(SwingConstants.RIGHT);
		
		JPanel hideButtonContainer = new JPanel();
		hideButtonContainer.add(hideButton);
		//add(hideButton, BorderLayout.NORTH);
		
		// Put the actual invitation text		
		JLabel inviteLabel = new JLabel(name +  " has invited you to play a game.");
		inviteLabel.setAlignmentX(SwingConstants.CENTER);
		inviteLabel.setVerticalAlignment(SwingConstants.CENTER);
		add(inviteLabel);
		
		// Create the accept/reject buttons
		JButton acceptButton = new JButton("Accept");
		SwagFactory.style(acceptButton);
		JButton rejectButton = new JButton("Reject");
		SwagFactory.style(rejectButton);
		
		JPanel buttonsPanel = new JPanel(new GridLayout(1, 2, BUTTON_SPACING, 0));
		Border spacingBorder = BorderFactory.createEmptyBorder(BUTTON_SPACING, BUTTON_SPACING, BUTTON_SPACING, BUTTON_SPACING);
		Border topBorder = BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black);
		buttonsPanel.setBorder(BorderFactory.createCompoundBorder(topBorder, spacingBorder));
		buttonsPanel.add(acceptButton);
		buttonsPanel.add(rejectButton);
		
		add(buttonsPanel, BorderLayout.SOUTH);
	}
	
}
