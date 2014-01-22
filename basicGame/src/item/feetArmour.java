package item;

public class feetArmour extends Armour
{
	public feetArmour(String nm, double armVal)
	{
		this.name = nm;
		this.armourValue = armVal;
		this.type = itemType.EQUIPABLE;
		this.armourSpot = armourType.FEET;
	}
	
	public String toString()
	{
		return "Name: " + this.name + "\n" +  "Type: Feet Armour " + "\n" +  "Armour Value: " + armourValue; 
	}
}
