package game;
import character.Character;

public class GodCharacter 
{
	private static final GodCharacter INSTANCE = new GodCharacter();
	private Character main;
	
	private GodCharacter()
	{
		main = Character.setToBasicCharacter();
	}
	
	public static GodCharacter getInstance()
	{
		return INSTANCE;
	}
	
	public Character getCharacter()
	{
		return main;
	}
}
