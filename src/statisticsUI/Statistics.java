package statisticsUI;

import java.util.HashMap;

public class Statistics 
{
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
}
