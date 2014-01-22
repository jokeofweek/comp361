package userInterface;



/*
* CardLayoutDemo.java
*
*/
import game.GameWindow;
import game.GodCharacter;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import character.Character;

public class CardLayoutDemo extends JFrame implements ActionListener {
   JPanel cards; //a panel that uses CardLayout
   final static String MASTERSAVE = "Start Game";
   final static String BASICINFO = "Basic Character Info";
   final static String ATTRIBUTES = "Attributes";
   final static String SKILLS = "Skill Selection";
   final static String INVENTORY = "Basic Inventory Selection";
   final static String CLASS = "Class Selection";
   final static String NEXTBUTTONSTRING = "Next";
      
   //Master save stuff
   JButton masterSaveButton;
   JButton masterNextButton;
   
   JButton basicNextButton;
   JButton attributeNextButton;
   
   BasicInfoPanel cardBasic;
   AttributePanel cardAttribute;
   
   ArrayList<String> cardNames = new ArrayList<String>();
   
   int i = 1;
   
   public CardLayoutDemo() {}
    
   public void addComponentToPane(Container pane){
       cardNames.add(MASTERSAVE);
       cardNames.add(BASICINFO);
       cardNames.add(ATTRIBUTES);
       cardNames.add(SKILLS);
       cardNames.add(INVENTORY);
       cardNames.add(CLASS);
	   
       //Initialise all the next buttons
       masterNextButton = new JButton(NEXTBUTTONSTRING);
       masterNextButton.addActionListener(this);
       
       basicNextButton = new JButton(NEXTBUTTONSTRING);
       basicNextButton.addActionListener(this);
       
       attributeNextButton = new JButton(NEXTBUTTONSTRING);
       attributeNextButton.addActionListener(this);
       
       //Make the master panel
       JPanel cardMaster = new JPanel();
       masterSaveButton = new JButton(MASTERSAVE);
       masterSaveButton.addActionListener(this);
       cardMaster.add(masterSaveButton);
       cardMaster.add(masterNextButton);
        
       
       
       //Create the name panel add all of the components
       try {
		cardBasic = new BasicInfoPanel();
		cardBasic.addNextButton(basicNextButton);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
       //The Attribute Panel
       try {
		cardAttribute = new AttributePanel();
		cardAttribute.addNextButton(attributeNextButton);
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
        
       //Create the panel that contains the "cards".
       cards = new JPanel(new CardLayout());
       cards.add(cardMaster, MASTERSAVE);
       cards.add(cardBasic, BASICINFO);
       cards.add(cardAttribute, ATTRIBUTES);

        
       pane.add(cards, BorderLayout.CENTER);
   }
    
   
   public void actionPerformed(ActionEvent event)
   {
	   if(event.getSource() == masterSaveButton){
		   System.out.println(GodCharacter.getInstance().getCharacter().toString());
       		setVisible(false);
    	
       		Thread workerThread = new Thread(new Worker());
       		workerThread.start();
       	
       		setVisible(false);
	   }
	   
	   else if(event.getSource() == masterNextButton){
		   CardLayout cl = (CardLayout)(cards.getLayout());
		   cl.show(cards, cardNames.get(i));
		   i++;
	   }
	   
	   else if(event.getSource() == basicNextButton) {
		   CardLayout cl = (CardLayout)(cards.getLayout());
		   cl.show(cards, cardNames.get(i));
		   GodCharacter.getInstance().getCharacter().setName(cardBasic.getName());
		   GodCharacter.getInstance().getCharacter().setRace(cardBasic.getRace());
		   System.out.println(GodCharacter.getInstance().getCharacter().toString());
		   i++;
	   }
	   
	   else if(event.getSource() == attributeNextButton) {
		   CardLayout cl = (CardLayout)(cards.getLayout());
		   cl.show(cards, cardNames.get(i));
		   GodCharacter.getInstance().getCharacter().setAttributes(cardAttribute.getAttributes());
		   System.out.println(GodCharacter.getInstance().getCharacter().toString());
		   i++;
		   
		   System.out.println(GodCharacter.getInstance().getCharacter().toString());
		   setVisible(false);
   	
		   Thread workerThread = new Thread(new Worker());
		   workerThread.start();
      	
		   setVisible(false);
	   }
   }
    
   /**
    * Create the GUI and show it.  For thread safety,
    * this method should be invoked from the
    * event dispatch thread.
    */
   private static void createAndShowGUI(){
       //Create and set up the window.
       JFrame frame = new JFrame("CharacterCreator");
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
       //Create and set up the content pane.
       CardLayoutDemo demo = new CardLayoutDemo();
       demo.setBackground(Color.black);
       demo.addComponentToPane(frame.getContentPane());

        
       //Display the window.
//       frame.pack();
       
       frame.setSize(800, 600);
       frame.setVisible(true);
   }
    
   public static void runUI() {
       /* Use an appropriate Look and Feel */
       try {
           //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
           UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
       } catch (UnsupportedLookAndFeelException ex) {
           ex.printStackTrace();
       } catch (IllegalAccessException ex) {
           ex.printStackTrace();
       } catch (InstantiationException ex) {
           ex.printStackTrace();
       } catch (ClassNotFoundException ex) {
           ex.printStackTrace();
       }
       /* Turn off metal's use of bold fonts */
       UIManager.put("swing.boldMetal", Boolean.FALSE);
        
       //Schedule a job for the event dispatch thread:
       //creating and showing this application's GUI.
       javax.swing.SwingUtilities.invokeLater(new Runnable() {
           public void run() {
               createAndShowGUI();
           }
       });
   }
}

class Worker implements Runnable {

	public Worker(){}
	public void run() {
		GameWindow gameWindow = new GameWindow();		
		gameWindow.start();				
	}
}
