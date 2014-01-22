package item;

import java.util.ArrayList;

import observation.Observable;
import observation.Observer;

public class Inventory implements Observable
{
	private ArrayList<Item> inventory;
	private ArrayList<Observer> observers;
	
	public Inventory()
	{
		this.inventory = new ArrayList<Item>();
		this.observers = new ArrayList<>();
	}
	
	public void addObserver(Observer toAdd)
	{
		observers.add(toAdd);
	}
	
	public void removeObserver(Observer toRemove)
	{
		observers.remove(toRemove);
	}
	
	public final void notifyObservers()
	{
		for(Observer obs : observers)
		{
			obs.update(this);
		}
	}
	
	public void addItem(Item toAdd)
	{
		//System.out.println("Item added: " + toAdd.toString());
		inventory.add(toAdd);
		
		notifyObservers();
	}
	
	public void dropItem(Item toDrop)
	{
		//System.out.println("Item dropped: " + toDrop.toString());
		inventory.remove(toDrop);
		
		notifyObservers();
	}
	
	public Item accessItem(Item toAccess)
	{
		for(Item item : this.inventory)
		{
			if(toAccess.name.equals(item.name))
			{
				return (Item) item.clone();
			}
		}
		
		System.out.println("Item not found");
		return null;
	}
	
	public boolean hasItem(Item toCheck)
	{
		for(Item item : this.inventory)
		{
			if(toCheck.name.equals(item.name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public String toString()
	{
		return inventory.toString();
	}
	
}
