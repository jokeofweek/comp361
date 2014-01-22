package item;

public class Sword extends Weapon 
{
	public Sword(String nm, double wepVal, handNum handnum)
	{
		this.name = nm;
		this.weaponDamage = wepVal;
		this.type = itemType.EQUIPABLE;
		this.weaponFormat = weaponType.SWORD;
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
		
		return "Name: " + this.name + "\n" + "Weapon Type: Sword" + "\n" + grip + "Damage: " + this.weaponDamage;
	}
}
