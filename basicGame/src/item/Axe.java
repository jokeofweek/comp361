package item;

public class Axe extends Weapon 
{
	public Axe(String nm, double wepVal, handNum handnum)
	{
		this.name = nm;
		this.weaponDamage = wepVal;
		this.type = itemType.EQUIPABLE;
		this.weaponFormat = weaponType.AXE;
		this.grip = handnum;
	}
	
	public String toString()
	{
		String grip;
		if(this.grip == handNum.ONE_HAND)
		{
			grip = "One Handed";
		}
		else
		{
			grip = "Two Handed";
		}
		
		return "Name: " + this.name + "\n" + "Weapon Type: Axe" + "\n" + grip + "Damage: " + this.weaponDamage;
	}
}
