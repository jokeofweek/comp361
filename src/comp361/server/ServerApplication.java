package comp361.server;

import comp361.server.console.ConsoleImpl;

public class ServerApplication {

	public static void main(String[] args) {
		new GameServer(new ConsoleImpl(), 5000);
	}

}
