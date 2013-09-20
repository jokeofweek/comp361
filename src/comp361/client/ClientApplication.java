package comp361.client;

import java.io.IOException;
import java.net.Socket;

public class ClientApplication {

	public static void main(String[] args) throws InterruptedException, IOException {
		Socket socket = new Socket("localhost", 5000);
		Thread.sleep(5000);
		socket.close();
	}
	
}
