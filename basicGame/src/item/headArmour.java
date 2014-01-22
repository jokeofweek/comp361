package item;

public class headArmour extends Armour 
{
	public headArmour(String nm, double armVal)
	{
		this.name = nm;
		this.armourValue = armVal;
		this.type = itemType.EQUIPABLE;
		this.armourSpot = armourType.HEAD;
	}
	
	public String toString()
	{
		return "Name: " + this.name + "\n" +  "Type: Head Armour " + "\n" +  "Armour Value: " + armourValue; 
	}
}
