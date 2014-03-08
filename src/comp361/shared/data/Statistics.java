package comp361.shared.data;

import java.util.HashMap;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Statistics implements KryoSerializable {
	private HashMap<String, Object> aStatistics;
	
	public Statistics()
	{
		aStatistics = new HashMap<String, Object>();
	}
	
	/**
	 * @param pKey
	 * @return the value associated with this key
	 */
	public Object getStatisticValue(String pKey)
	{
		return aStatistics.get(pKey);
	}
	
	/**
	 * @param pKey key of object to add
	 * @param pValue vale of object to add
	 */
	public void addStatistic(String pKey, Object pValue)
	{
		aStatistics.put(pKey, pValue);
	}
	
	/**
	 * @return all of the statistics
	 */
	public HashMap<String, Object> getStatistics()
	{
		return aStatistics;
	}
	
	/**
	 * Just make up some statistics for an example
	 */
	public void initialiseStatisticExample()
	{
		String[] descriptions = { "Rank", "Games Played", "Games Won", "Ships Sunk", "Enemy Ships Sunk", "K/D Ration" };
		Object[] data = { "Lieutenant", 14, 11, 45, 102, 2.267 };
		
		for(int i = 0; i < descriptions.length; i++)
		{
			this.addStatistic(descriptions[i], data[i]);
		}
	}
	
	/**
	 * Make up some other random stats
	 */
	public void initialiseOtherStatistics()
	{
		String[] descriptions = { "Rank", "Games Played", "Games Won", "Ships Sunk", "Enemy Ships Sunk", "K/D Ration" };
		Object[] data = { "Landlubber", 1, 0, 2, 1, 0.005 };
		
		for(int i = 0; i < descriptions.length; i++)
		{
			this.addStatistic(descriptions[i], data[i]);
		}
	}
	
	/**
	 * Make up some other random stats
	 */
	public void initialiseEvenOtherStatistics()
	{
		String[] descriptions = { "Rank", "Games Played", "Games Won", "Ships Sunk", "Enemy Ships Sunk", "K/D Ration" };
		Object[] data = { "NOOB", 0, 0, 0, 0, 0 };
		
		for(int i = 0; i < descriptions.length; i++)
		{
			this.addStatistic(descriptions[i], data[i]);
		}
	}

	@Override
	public void read(Kryo kryo, Input input) {
		aStatistics = kryo.readObject(input, HashMap.class);
	}

	@Override
	public void write(Kryo kryo, Output output) {
		kryo.writeObject(output, aStatistics);
		
	}
}
