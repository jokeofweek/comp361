package item;

enum armourType {CHEST, HEAD, HANDS, FEET};

public class Armour extends Item
{
	double armourValue;
	armourType armourSpot;
	
	public Armour(){};
	
	public Armour(String nm, double armVal, armourType tp)
	{
		this.name = nm;
		this.type = itemType.EQUIPABLE;
		this.armourValue = armVal;
		this.armourSpot = tp;
	}
	
}