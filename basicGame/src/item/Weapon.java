package item;


public class Weapon extends Item 
{
	double weaponDamage;
	weaponType weaponFormat;
	handNum grip;
	
	public Weapon(){};
	
	public Weapon(String nm, double wepVal, weaponType tp, handNum handnum)
	{
		this.name = nm;
		this.weaponDamage = wepVal;
		this.type = itemType.EQUIPABLE;
		this.weaponFormat = tp;
		this.grip = handnum;
	}
}
