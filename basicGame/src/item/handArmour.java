package item;

public class handArmour extends Armour
{
	public handArmour(String nm, double armVal)
	{
		this.name = nm;
		this.armourValue = armVal;
		this.type = itemType.EQUIPABLE;
		this.armourSpot = armourType.HANDS;
	}
	
	public String toString()
	{
		return "Name: " + this.name + "\n" +  "Type: Hand Armour " + "\n" +  "Armour Value: " + armourValue; 
	}
}