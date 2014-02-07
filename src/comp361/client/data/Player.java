package comp361.client.data;

import comp361.shared.data.Statistics;

public class Player {

	private String name;
	private Statistics aStatistics;
	
	public Player(String name) {
		this.name = name;
		aStatistics = new Statistics();
	}
	
	public String getName() {
		return name;
	}	
	
	public Statistics getStatistics()
	{
		return aStatistics;
	}
}
