package item;

public class chestArmour extends Armour 
{
	public chestArmour(String nm, double armVal)
	{
		this.name = nm;
		this.armourValue = armVal;
		this.type = itemType.EQUIPABLE;
		this.armourSpot = armourType.CHEST;
	}
	
	public String toString()
	{
		return "Name: " + this.name + "\n" +  "Type: Chest Armour " + "\n" +  "Armour Value: " + armourValue; 
	}
}
