package comp361.shared.network;

import java.awt.Point;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.EndPoint;
import comp361.shared.data.ArmorType;
import comp361.shared.data.CellType;
import comp361.shared.data.Direction;
import comp361.shared.data.Field;
import comp361.shared.data.Game;
import comp361.shared.data.GameDescriptor;
import comp361.shared.data.Player;
import comp361.shared.data.PlayerUpdateStatus;
import comp361.shared.data.Ship;
import comp361.shared.data.Statistics;
import comp361.shared.data.range.CenterRange;
import comp361.shared.data.range.Range;
import comp361.shared.data.range.TailRange;
import comp361.shared.packets.client.JoinGamePacket;
import comp361.shared.packets.client.LeaveGamePacket;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.client.UpdateReadyPacket;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;
import comp361.shared.packets.server.GameDescriptorListPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GameDescriptorReadyUpdatePacket;
import comp361.shared.packets.server.GameDescriptorStartPacket;
import comp361.shared.packets.server.GameStartPacket;
import comp361.shared.packets.server.GenericError;
import comp361.shared.packets.server.LoginError;
import comp361.shared.packets.server.PlayerListPacket;
import comp361.shared.packets.server.PlayerUpdatePacket;
import comp361.shared.packets.server.RegisterError;
import comp361.shared.packets.shared.ChangeSeedPacket;
import comp361.shared.packets.shared.MessagePacket;
import comp361.shared.packets.shared.SetupMessagePacket;

public class NetworkManager {

	/**
	 * This registers all objects which are going to be sent across the network
	 * with the {@link Kryo} serializer.
	 * 
	 * @param endPoint
	 *            either the client or server.
	 */
	public static void register(EndPoint endPoint) {
		Kryo kryo = endPoint.getKryo();
		kryo.setReferences(true);
		kryo.register(MessagePacket.class);
		kryo.register(RegisterPacket.class);
		kryo.register(RegisterError.class);
		kryo.register(LoginPacket.class);
		kryo.register(LoginError.class);
		kryo.register(GenericError.class);
		kryo.register(PlayerListPacket.class);
		kryo.register(PlayerUpdatePacket.class);
		kryo.register(PlayerUpdateStatus.class);
		kryo.register(Statistics.class);
		kryo.register(Player.class);
		kryo.register(ArmorType.class);
		kryo.register(Direction.class);
		kryo.register(CellType.class);
		kryo.register(CellType[].class);
		kryo.register(CellType[][].class);
		kryo.register(Point.class);
		kryo.register(Range.class);
		kryo.register(TailRange.class);
		kryo.register(CenterRange.class);
		kryo.register(GameDescriptor.class);
		kryo.register(Field.class);
		kryo.register(Ship.class);
		kryo.register(Game.class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(HashSet.class);
		kryo.register(int[].class);
		kryo.register(int[][].class);
		kryo.register(NewGameDescriptorPacket.class);
		kryo.register(GameDescriptorCreatedPacket.class);
		kryo.register(GameDescriptorListPacket.class);
		kryo.register(GameDescriptorPlayerUpdatePacket.class);
		kryo.register(JoinGamePacket.class);
		kryo.register(LeaveGamePacket.class);
		kryo.register(UpdateReadyPacket.class);
		kryo.register(GameDescriptorReadyUpdatePacket.class);
		kryo.register(ChangeSeedPacket.class);
		kryo.register(SetupMessagePacket.class);
		kryo.register(GameDescriptorStartPacket.class);
		kryo.register(GameStartPacket.class);
	}

	private NetworkManager() {
	}

	private static class JavaSerializer extends Serializer {
		private ObjectOutputStream objectStream;
		private Output lastOutput;

		public void write (Kryo kryo, Output output, Object object) {
			try {
				if (output != lastOutput) {
					objectStream = new ObjectOutputStream(output);
					lastOutput = output;
				} else
					objectStream.reset();
				objectStream.writeObject(object);
				objectStream.flush();
			} catch (Exception ex) {
				throw new KryoException("Error during Java serialization.", ex);
			}
		}

		public Object read (Kryo kryo, Input input, Class type) {
			try {
				return new ObjectInputStream(input).readObject();
			} catch (Exception ex) {
				throw new KryoException("Error during Java deserialization.", ex);
			}
		}
	}
	
}
