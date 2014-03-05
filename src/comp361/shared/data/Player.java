package comp361.shared.data;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoSerializable;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

/**
 * This class represents the {@link Player} object used by the client.
 * It contains limited content about a player.
 */
public class Player implements KryoSerializable {

	private String name;
	private Statistics statistics;
	
	/**
	 * This constructor should only be used by the Kryo serializer,
	 * as the player's name cannot be changed after construction.
	 */
	public Player(){}
	
	public Player(String name) {
		this.name = name;
		this.statistics = new Statistics();
	}
	
	public String getName() {
		return name;
	}	
	
	public Statistics getStatistics()
	{
		return statistics;
	}
	
	public void setStatistics(Statistics statistics) {
		this.statistics = statistics;
	}
	
	@Override
	public boolean equals(Object other)
	{
		if(other instanceof Player)
			return ((Player)other).name.equals(this.name);
		return false;
	}
	
	@Override
	public void read(Kryo kryo, Input input) {
		name = kryo.readObject(input, String.class);
		statistics = kryo.readObject(input, Statistics.class);
	}

	@Override
	public void write(Kryo kryo, Output output) {
		kryo.writeObject(output, name);
		kryo.writeObject(output, statistics);
	}
}
