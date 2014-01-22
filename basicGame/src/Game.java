import item.Inventory;
import item.chestArmour;
import item.handArmour;
import item.headArmour;
import observation.BlueView;
import observation.RedView;
import userInterface.CardLayoutDemo;

public class Game 
{
	public static void main(String[] args)
	{
//		chestArmour basic = new chestArmour("temp", 15);
//		headArmour temp = new headArmour("temp2", 5);
//		handArmour hand = new handArmour("temp3", 6);
//		
//		Inventory test = new Inventory();
//		test.addObserver(new RedView());
//		test.addObserver(new BlueView());
//		
//		test.addItem(basic);
//		test.addItem(temp);
		//test.notifyObservers();
//		test.addItem(hand);
		//test.notifyObservers();
		
		CardLayoutDemo.runUI();
	}
}
