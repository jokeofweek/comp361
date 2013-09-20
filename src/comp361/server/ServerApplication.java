package comp361.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.Connection;

import comp361.server.console.Console;
import comp361.server.console.ConsoleImpl;
import comp361.server.network.ServerPacketListener;
import comp361.shared.network.NetworkManager;

public class ServerApplication {

	public static void main(String[] args) {
		new GameServer(new ConsoleImpl(), 5000);
	}

}
