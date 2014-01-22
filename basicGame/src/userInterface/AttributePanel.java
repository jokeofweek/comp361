package userInterface;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import character.AttributeSet;

public class AttributePanel extends JPanel implements ActionListener
{
	//Needed strings
	public static final String ATTRIBUTELABELSTRING = "Choose your Attributes";
	public static final String POINTSREMAININGSTRING = "Points Remaining: ";
	public static final String ATTRIBUTECATEGORYSTRING = "Attributes";
	public static final String ADDBUTTONLABEL = "+";
	
	public static final String HEALTHLABELSTRING = "Health: ";
	public static final String MANALABELSTRING = "Mana: ";
	public static final String DAMAGELABELSTRING = "Damage: ";
	public static final String ARMOURLABELSTRING = "Armour: ";
	
	public static final String STRENGTHLABELSTRING = "Strength: ";
	public static final String DEXTERITYLABELSTRING = "Dexterity: ";
	public static final String INTELLIGENCELABELSTRING = "Intelligence: ";
	public static final String CONSTITUTIONLABELSTRING = "Constitution: ";
	
	int pointsRemaining = 30;
	
	private Image backgroundImage;
	
	
	//The attributeSet to return
	AttributeSet attributes;
	
	//For AttributeSet
	private double health = 100;
	private double mana = 100;
	private double damage = 5;
	private double armour = 5;
	
	private int strength = 10;
	private int dexterity = 10;
	private int intelligence = 10;
	private int constitution = 10;
	
	//JLabels needed
	JLabel AttributeLabel;
	JLabel PointsRemainingLabel;
	JLabel AttributeCategoryLabel;
	JLabel HealthLabel;
	JLabel ManaLabel;
	JLabel DamageLabel;
	JLabel ArmourLabel;
	JLabel StrengthLabel;
	JLabel DexterityLabel;
	JLabel IntelligenceLabel;
	JLabel ConstitutionLabel;
	
	//JButtons needed
	JButton strengthIncrementButton;
	JButton dexterityIncrementButton;
	JButton intelligenceIncrementButton;
	JButton constitutionIncrementButton;
	
	
	
	public AttributePanel() throws IOException
	{
		backgroundImage = ImageIO.read(new File("data/npvPRaN.jpg"));
		
		GridBagLayout gridBag = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		this.setLayout(gridBag);
		
		//Set and add the Header
		AttributeLabel = new JLabel(ATTRIBUTELABELSTRING);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		AttributeLabel.setFont(new Font("Times", Font.BOLD, 24));
		AttributeLabel.setBackground(Color.red);
		AttributeLabel.setOpaque(true);
		add(AttributeLabel, c);
		
		//Set and add the Points Remaining 
		PointsRemainingLabel = new JLabel(POINTSREMAININGSTRING + pointsRemaining);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		PointsRemainingLabel.setFont(new Font("Times", Font.PLAIN, 20));
		//PointsRemainingLabel.setBackground(Color.gray);
		PointsRemainingLabel.setOpaque(true);
		add(PointsRemainingLabel, c);
		
		//Set Attribute Category Label
		AttributeCategoryLabel = new JLabel(ATTRIBUTECATEGORYSTRING);
		c.gridx = 0;
		c.gridy = 2;
		AttributeCategoryLabel.setFont(new Font("Times", Font.PLAIN, 20));
		AttributeCategoryLabel.setBackground(Color.white);
		AttributeCategoryLabel.setOpaque(true);
		add(AttributeCategoryLabel, c);
		
		//Set the Strength Label
		StrengthLabel = new JLabel(STRENGTHLABELSTRING + strength);
		c.gridx = 0;
		c.gridy = 3;
		StrengthLabel.setFont(new Font("Times", Font.PLAIN, 20));
		StrengthLabel.setBackground(Color.white);
		StrengthLabel.setOpaque(true);
		add(StrengthLabel, c);
		
		//Set the Strength Increment Button
		strengthIncrementButton = new JButton(ADDBUTTONLABEL);
		strengthIncrementButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 3;
		strengthIncrementButton.setFont(new Font("Times", Font.PLAIN, 16));
		strengthIncrementButton.setBackground(Color.gray);
		strengthIncrementButton.setOpaque(true);
		add(strengthIncrementButton, c);
		
		//Set the Dexterity Label
		DexterityLabel = new JLabel(DEXTERITYLABELSTRING + dexterity);
		c.gridx = 0;
		c.gridy = 4;
		DexterityLabel.setFont(new Font("Times", Font.PLAIN, 20));
		DexterityLabel.setBackground(Color.white);
		DexterityLabel.setOpaque(true);
		add(DexterityLabel, c);
		
		//Set the Dexterity Increment Button
		dexterityIncrementButton = new JButton(ADDBUTTONLABEL);
		dexterityIncrementButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 4;
		dexterityIncrementButton.setFont(new Font("Times", Font.PLAIN, 16));
		dexterityIncrementButton.setBackground(Color.gray);
		dexterityIncrementButton.setOpaque(true);
		add(dexterityIncrementButton, c);
		
		//Set the Intelligence Label
		IntelligenceLabel = new JLabel(INTELLIGENCELABELSTRING + intelligence);
		c.gridx = 0;
		c.gridy = 5;
		IntelligenceLabel.setFont(new Font("Times", Font.PLAIN, 20));
		IntelligenceLabel.setBackground(Color.white);
		IntelligenceLabel.setOpaque(true);
		add(IntelligenceLabel, c);
		
		//Set the Dexterity Increment Button
		intelligenceIncrementButton = new JButton(ADDBUTTONLABEL);
		intelligenceIncrementButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 5;
		intelligenceIncrementButton.setFont(new Font("Times", Font.PLAIN, 16));
		intelligenceIncrementButton.setBackground(Color.gray);
		intelligenceIncrementButton.setOpaque(true);
		add(intelligenceIncrementButton, c);
		
		//Set the Constitution Label
		ConstitutionLabel = new JLabel(CONSTITUTIONLABELSTRING + constitution);
		c.gridx = 0;
		c.gridy = 6;
		ConstitutionLabel.setFont(new Font("Times", Font.PLAIN, 20));
		ConstitutionLabel.setBackground(Color.white);
		ConstitutionLabel.setOpaque(true);
		add(ConstitutionLabel, c);
		
		//Set the Constitution Increment Button
		constitutionIncrementButton = new JButton(ADDBUTTONLABEL);
		constitutionIncrementButton.addActionListener(this);
		c.gridx = 1;
		c.gridy = 6;
		constitutionIncrementButton.setFont(new Font("Times", Font.PLAIN, 16));
		constitutionIncrementButton.setBackground(Color.gray);
		constitutionIncrementButton.setOpaque(true);
		add(constitutionIncrementButton, c);
		
		//Set the Health Label
		HealthLabel = new JLabel(HEALTHLABELSTRING + (health));
		c.gridx = 0;
		c.gridy = 7;
		HealthLabel.setFont(new Font("Times", Font.PLAIN, 20));
		HealthLabel.setBackground(Color.white);
		HealthLabel.setOpaque(true);
		add(HealthLabel, c);
		
		//Set the Mana Label
		ManaLabel = new JLabel(MANALABELSTRING + (mana));
		c.gridx = 0;
		c.gridy = 8;
		ManaLabel.setFont(new Font("Times", Font.PLAIN, 20));
		ManaLabel.setBackground(Color.white);
		ManaLabel.setOpaque(true);
		add(ManaLabel, c);
		
		//Set the Damage Label
		DamageLabel = new JLabel(DAMAGELABELSTRING + (damage));
		c.gridx = 0;
		c.gridy = 9;
		DamageLabel.setFont(new Font("Times", Font.PLAIN, 20));
		DamageLabel.setBackground(Color.white);
		DamageLabel.setOpaque(true);
		add(DamageLabel, c);
		
		//Set the Armour Label
		ArmourLabel = new JLabel(ARMOURLABELSTRING + (armour));
		c.gridx = 0;
		c.gridy = 10;
		ArmourLabel.setFont(new Font("Times", Font.PLAIN, 20));
		ArmourLabel.setBackground(Color.white);
		ArmourLabel.setOpaque(true);
		add(ArmourLabel, c);
	}
	
	//All the appropriate button responses
	public void actionPerformed(ActionEvent event)
	{
		if(event.getSource() == strengthIncrementButton && pointsRemaining > 0){
			strength++;
			pointsRemaining--;
			damage = damage + 1;
			PointsRemainingLabel.setText(POINTSREMAININGSTRING + pointsRemaining);
			StrengthLabel.setText(STRENGTHLABELSTRING + strength);
			DamageLabel.setText(DAMAGELABELSTRING + (damage));
		}
		
		else if(event.getSource() == dexterityIncrementButton && pointsRemaining > 0){
			dexterity++;
			pointsRemaining--;
			armour = armour + 1;
			PointsRemainingLabel.setText(POINTSREMAININGSTRING + pointsRemaining);
			DexterityLabel.setText(DEXTERITYLABELSTRING + dexterity);
			ArmourLabel.setText(ARMOURLABELSTRING + (armour));
		}
		
		else if(event.getSource() == intelligenceIncrementButton && pointsRemaining > 0){
			intelligence++;
			pointsRemaining--;
			mana = mana + 10;
			PointsRemainingLabel.setText(POINTSREMAININGSTRING + pointsRemaining);
			IntelligenceLabel.setText(INTELLIGENCELABELSTRING + intelligence);
			ManaLabel.setText(MANALABELSTRING + (mana));
		}
		
		else if(event.getSource() == constitutionIncrementButton && pointsRemaining > 0){
			constitution++;
			pointsRemaining--;
			health = health + 10;
			PointsRemainingLabel.setText(POINTSREMAININGSTRING + pointsRemaining);
			ConstitutionLabel.setText(CONSTITUTIONLABELSTRING + constitution);
			HealthLabel.setText(HEALTHLABELSTRING + (health));
		}
	}
	
	public AttributeSet getAttributes()
	{
		attributes = new AttributeSet(health, mana, damage, armour, strength, dexterity, intelligence, constitution);
		return attributes;
	}
	
	public void addNextButton(JButton nextButton)
	{
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 11;
		this.add(nextButton, c);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		g.drawImage(backgroundImage, 0, 0, this);
	}
	
}
