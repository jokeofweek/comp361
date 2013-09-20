package comp361.server;

import comp361.server.console.ConsoleImpl;

public class ServerApplication {

	public static void main(String[] args) {
		new Server(new ConsoleImpl(), 5000).run();
	}
	
}
