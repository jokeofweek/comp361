package userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import character.Race;

public class BasicInfoPanel extends JPanel implements ActionListener
{

	   final static String NAMEBUTTONSTRING = "Enter!";
	   final static String HUMANNAMESTRING = "Human";
	   final static String ELFNAMESTRING = "Elf";
	   final static String DWARFNAMESTRING = "Dwarf";
	   final static String NAMEDISPLAYLABELSTRING = "Name";
	   final static String RACEDISPLAYLABELSTRING = "Race";
	   
	   private String aName;
	   private Race aRace;
	   
	   private Image backgroundImage;
	   
	   //Character Name Panel Set Up
	   JButton nameButtonEnter;
	   JTextField nameTextField;
	   JLabel nameLabel;
	   JLabel nameDisplayLabel;
	   JLabel raceDisplayLabel;
	   
	   //Race RadioButtons
	   JRadioButton humanButton;
	   JRadioButton elfButton;
	   JRadioButton dwarfButton;
	   
	   boolean isHuman = false;
	   boolean isElf = false;
	   boolean isDwarf = false;
	   
	   public BasicInfoPanel() throws IOException
	   {
		   backgroundImage = ImageIO.read(new File("data/npvPRaN.jpg"));
		   
			GridBagLayout gridBag = new GridBagLayout();
			GridBagConstraints c = new GridBagConstraints();
			this.setLayout(gridBag);
	       
	       //Name Label
	       nameLabel = new JLabel("Select your Personal Details");
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 0;
	       nameLabel.setFont(new Font("Times", Font.BOLD, 18));
	       nameLabel.setBackground(Color.gray);
	       nameLabel.setOpaque(true);
	       add(nameLabel, c);
	       
	       //Name text field
	       nameTextField = new JTextField("Enter your name here!", 20);
	       nameTextField.addMouseListener(new MouseAdapter(){
	    	   @Override
	    	   public void mouseClicked(MouseEvent e){
	    		   nameTextField.setText("");
	    	   }
	       });
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 1;
	       nameTextField.setFont(new Font("Times", Font.PLAIN, 14));
	       nameTextField.setBackground(Color.white);
	       nameTextField.setOpaque(true);
	       add(nameTextField, c);
	       
	       //Radio Button for human
	       humanButton = new JRadioButton(HUMANNAMESTRING);
	       humanButton.addActionListener(this);
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 2;
	       add(humanButton, c);
	       
	       //Radio Button for Elf
	       elfButton = new JRadioButton(ELFNAMESTRING);
	       elfButton.addActionListener(this);
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 4;
	       add(elfButton, c);
	       
	       //Radio Button for dwarf
	       dwarfButton = new JRadioButton(DWARFNAMESTRING);
	       dwarfButton.addActionListener(this);
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 6;
	       add(dwarfButton, c);
	       
	       //Button group to hold the radio buttons
	       ButtonGroup group = new ButtonGroup();
	       group.add(humanButton);
	       group.add(elfButton);
	       group.add(dwarfButton);
	       
		   //Enter Button Set up
	       nameButtonEnter = new JButton(NAMEBUTTONSTRING);
	       nameButtonEnter.addActionListener(this);
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 8;
	       add(nameButtonEnter, c);
	       
	       //Name Display Label Set Up
	       nameDisplayLabel = new JLabel(NAMEDISPLAYLABELSTRING);
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 10;
	       nameDisplayLabel.setFont(new Font("Times", Font.ITALIC, 14));
	       nameDisplayLabel.setBackground(Color.gray);
	       nameDisplayLabel.setOpaque(true);
	       add(nameDisplayLabel, c);
	       
	       //Race Display Label Set Up
	       raceDisplayLabel = new JLabel(RACEDISPLAYLABELSTRING);
	       c.fill = GridBagConstraints.HORIZONTAL;
	       c.gridx = 0;
	       c.gridy = 12;
	       raceDisplayLabel.setFont(new Font("Times", Font.ITALIC, 14));
	       raceDisplayLabel.setBackground(Color.gray);
	       raceDisplayLabel.setOpaque(true);
	       add(raceDisplayLabel, c);
	   }
	   
	   @Override
	   public void paintComponent(Graphics g)
	   {
		   super.paintComponent(g);
		   g.drawImage(backgroundImage, 0, 0, this);
	   }
	   
	   public String getName()
	   {
		   return aName;
	   }
	   
	   public Race getRace()
	   {
		   return aRace;
	   }
	   
	   public void addNextButton(JButton nextButton)
	   {
			GridBagConstraints c = new GridBagConstraints();
		    c.fill = GridBagConstraints.HORIZONTAL;
		    c.gridx = 0;
		    c.gridy = 14;
		    this.add(nextButton, c);
	   }
	   
	   public void actionPerformed(ActionEvent event)
	   {
		   if(event.getSource() == humanButton) {
			   isHuman = true;
			   isElf = false;
			   isDwarf = false;
		   }
		   
		   else if(event.getSource() == elfButton) {
			   isHuman = false;
			   isElf = true;
			   isDwarf = false;
		   }
		   
		   else if(event.getSource() == dwarfButton) {
			   isHuman = false;
			   isElf = false;
			   isDwarf = true;
		   }
		   
		   else if(event.getSource() == nameButtonEnter && isHuman){
			   aRace = Race.HUMAN;
			   aName = nameTextField.getText();
			   nameDisplayLabel.setText(aName);
			   raceDisplayLabel.setText(aRace.toString());
		   }
		   
		   else if(event.getSource() == nameButtonEnter && isElf){
			   aRace = Race.ELF;
			   aName = nameTextField.getText();
			   nameDisplayLabel.setText(aName);
			   raceDisplayLabel.setText(aRace.toString());
		   }
		   
		   else if(event.getSource() == nameButtonEnter && isDwarf){
			   aRace = Race.DWARF;
			   aName = nameTextField.getText();
			   nameDisplayLabel.setText(aName);
			   raceDisplayLabel.setText(aRace.toString());
		   }
	   }
}
