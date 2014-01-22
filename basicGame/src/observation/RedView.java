package observation;

public class RedView implements Observer 
{
	public void update(Object o) 
	{
		System.err.println(o.toString());
//		System.out.println("\n");
	}

}
