package character;

public class AttributeSet
{
	private double health;
	private double mana;
	private double damage;
	private double armour;
	
	private int strength;
	private int dexterity;
	private int intelligence;
	private int constitution;
	
	public AttributeSet(double hlt, double mn, double dmg, double arm, int str, int dex, int intel, int con)
	{
		this.health = hlt;
		this.mana = mn;
		this.damage = dmg;
		this.armour = arm;
		
		this.strength = str;
		this.dexterity = dex;
		this.intelligence = intel;
		this.constitution = con;
	}
	
	double getHealth()
	{
		return this.health;
	}
	
	double getMana()
	{
		return this.mana;
	}
	
	double getDamage()
	{
		return this.damage;
	}
	
	double getArmour()
	{
		return this.armour;
	}
	
	int getStrength()
	{
		return this.strength;
	}
	
	int getDexterity()
	{
		return this.dexterity;
	}
	
	int getIntelligence()
	{
		return this.intelligence;
	}
	
	int getConstitution()
	{
		return this.constitution;
	}
	
	public String toString()
	{
		return "Health :" + health + "\n" + "Mana: " + mana + "\n" + "Damage: " + damage + "\n" + "Armour: " + armour + "\n" + "Strength: " + strength + "\n" + "Decterity: " + dexterity + "\n" + "Intelligence: " + intelligence + "\n" + "Constitution: " + constitution; 
	}
	
}