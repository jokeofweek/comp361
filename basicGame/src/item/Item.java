package item;


public class Item implements Cloneable
{
	String name;
	itemType type;
	
	public Item(){};
	
	public Item(String nm, itemType tp)
	{
		name = nm;
		type = tp;
	}
	
	public Item clone()
	{
		Item foo;
		try
		{
			foo = (Item) super.clone();
		}
		
		catch (CloneNotSupportedException e)
		{
			//Will never happen
			return null;
		}
		
		return foo;
	}
	
	public String toString()
	{
		return "Item Name: " + this.name;
	}
}
