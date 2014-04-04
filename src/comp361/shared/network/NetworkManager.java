package comp361.shared.network;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.EndPoint;
import comp361.shared.data.ArmorType;
import comp361.shared.data.CannonType;
import comp361.shared.data.CellType;
import comp361.shared.data.Direction;
import comp361.shared.data.Field;
import comp361.shared.data.Game;
import comp361.shared.data.GameDescriptor;
import comp361.shared.data.GameResult;
import comp361.shared.data.MoveType;
import comp361.shared.data.Player;
import comp361.shared.data.PlayerUpdateStatus;
import comp361.shared.data.Ship;
import comp361.shared.data.Statistics;
import comp361.shared.data.range.BeforeTailRange;
import comp361.shared.data.range.CenterRange;
import comp361.shared.data.range.Range;
import comp361.shared.data.range.TailRange;
import comp361.shared.packets.client.JoinGamePacket;
import comp361.shared.packets.client.LeaveGamePacket;
import comp361.shared.packets.client.LoginPacket;
import comp361.shared.packets.client.NewGameDescriptorPacket;
import comp361.shared.packets.client.RegisterPacket;
import comp361.shared.packets.client.RequestSavedGamesPacket;
import comp361.shared.packets.client.UpdateReadyPacket;
import comp361.shared.packets.server.GameDescriptorCreatedPacket;
import comp361.shared.packets.server.GameDescriptorListPacket;
import comp361.shared.packets.server.GameDescriptorPlayerUpdatePacket;
import comp361.shared.packets.server.GameDescriptorReadyUpdatePacket;
import comp361.shared.packets.server.GameDescriptorRemovedPacket;
import comp361.shared.packets.server.GameDescriptorStartPacket;
import comp361.shared.packets.server.GameStartPacket;
import comp361.shared.packets.server.GenericError;
import comp361.shared.packets.server.LoginError;
import comp361.shared.packets.server.PlayerListPacket;
import comp361.shared.packets.server.PlayerUpdatePacket;
import comp361.shared.packets.server.RegisterError;
import comp361.shared.packets.server.SavedGameContainer;
import comp361.shared.packets.server.SavedGamesListPacket;
import comp361.shared.packets.server.UpdatePlayerStatisticsPacket;
import comp361.shared.packets.shared.ChangeSeedPacket;
import comp361.shared.packets.shared.GameMovePacket;
import comp361.shared.packets.shared.GameOverPacket;
import comp361.shared.packets.shared.InGameMessagePacket;
import comp361.shared.packets.shared.MessagePacket;
import comp361.shared.packets.shared.RequestSavePacket;
import comp361.shared.packets.shared.SaveResponsePacket;
import comp361.shared.packets.shared.SavedGameInvitePacket;
import comp361.shared.packets.shared.SavedGameInviteResponsePacket;
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
		kryo.register(BeforeTailRange.class);
		kryo.register(TailRange.class);
		kryo.register(CenterRange.class);
		kryo.register(GameDescriptor.class);
		kryo.register(Field.class);
		kryo.register(Ship.class);
		kryo.register(Game.class);
		kryo.register(ArrayList.class);
		kryo.register(HashMap.class);
		kryo.register(HashSet.class);
		kryo.register(String[].class);
		kryo.register(Date.class);
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
		kryo.register(InGameMessagePacket.class);
		kryo.register(GameDescriptorStartPacket.class);
		kryo.register(GameStartPacket.class);
		kryo.register(MoveType.class);
		kryo.register(CannonType.class);
		kryo.register(GameMovePacket.class);
		kryo.register(GameResult.class);
		kryo.register(GameOverPacket.class);
		kryo.register(GameDescriptorRemovedPacket.class);
		kryo.register(UpdatePlayerStatisticsPacket.class);
		kryo.register(RequestSavePacket.class);
		kryo.register(SaveResponsePacket.class);
		kryo.register(SavedGameContainer.class);
		kryo.register(RequestSavedGamesPacket.class);
		kryo.register(SavedGamesListPacket.class);
		kryo.register(SavedGameInvitePacket.class);
		kryo.register(SavedGameInviteResponsePacket.class);
	}

	private NetworkManager() {
	}

	
}
