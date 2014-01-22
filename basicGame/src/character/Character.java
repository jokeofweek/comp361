package character;

import javax.swing.JTable.PrintMode;

import item.Inventory;

public class Character 
{
	private String name;
	private AttributeSet attributes;
	private Race race;
	Inventory inventory;
	
	public Character(String nm, AttributeSet attr, Race rc)
	{
		name = nm;
		attributes = attr;
		race = rc;
		inventory = new Inventory();
	}
	
	public Character(String nm, AttributeSet attr, Race rc, Inventory inv)
	{
		name = nm;
		attributes = attr;
		race = rc;
		inventory = inv;
	}
	
	public Race getRace()
	{
		return this.race;
	}
	
	public String getName()
	{
		return this.name;
	}
	
	/**
	 * @param pName set the new name
	 */
	public void setName(String pName)
	{
		name = pName;
	}
	
	/**
	 * @param pRace se the new Race
	 */
	public void setRace(Race pRace)
	{
		race = pRace;
	}
	
	public AttributeSet getAttributes()
	{
		return this.attributes;
	}
	
	public void setAttributes(AttributeSet pAttributes)
	{
		attributes = pAttributes;
	}
	
	public Inventory getInventory()
	{
		return this.inventory;
	}
	
	public String toString()
	{
		return ("Name: " + name + "\n" + "Race: " + race.toString() + "\n" + attributes.toString()+ "\n" + inventory.toString());
	}
	
	public static Character setToBasicCharacter()
	{
		String name = "";
		Race race = Race.HUMAN;
		AttributeSet attributes = new AttributeSet(100, 100, 5, 5, 10, 10, 10, 10);
		Inventory inventory = new Inventory();
		
		Character toReturn = new Character(name, attributes, race, inventory);
		return toReturn;
		
	}
}

	
