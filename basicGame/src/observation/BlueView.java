package observation;

public class BlueView implements Observer 
{
	public void update(Object o)
	{
		System.out.println(o.toString());
//		System.out.println("\n");
	}
}
