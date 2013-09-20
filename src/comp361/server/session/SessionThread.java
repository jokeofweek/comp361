package comp361.server.session;

import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Queue;

import comp361.server.network.ServerReceivedPacket;
import comp361.shared.network.ClientPacketType;

public class SessionThread extends Thread {

	private Queue<byte[]> outgoingQueue;
	private Session session;
	private Socket socket;

	private static final int READ_TICKS = 25;
	private static final int WRITE_TICKS = 25;

	public SessionThread(Session session, Socket socket) throws SocketException {
		this.socket = socket;
		this.socket.setSoTimeout(READ_TICKS);
		this.outgoingQueue = session.getOutgoingQueue();
		this.session = session;
	}

	@Override
	public void run() {
		// Incoming variables
		int tmpRead;
		ClientPacketType type = null;
		byte[] data = null;
		int remaining = 0;
		int currentPosition = 0;
		int readBytes;
		int sizeBytes = 0;

		byte[] outgoingPacket;

		long ticks;

		while (session.getSessionType() != SessionType.DISCONNECTED) {
			// Handle reads
			ticks = System.currentTimeMillis() + READ_TICKS;
			do {
				try {
					// If we are awaiting size bytes, then read them in one at a
					// time
					if (sizeBytes > 0) {
						tmpRead = socket.getInputStream().read();

						if (tmpRead == -1) {
							throw new EOFException("End of stream");
						}
						remaining <<= 8;
						remaining |= (tmpRead & 0x0ff);
						sizeBytes--;
						if (sizeBytes == 0) {
							data = new byte[remaining];
						}
					} else if (remaining == 0 && type == null) {
						// New packet
						tmpRead = this.socket.getInputStream().read();
						// Bit-twiddling must be done here to convert it back to
						// an integer [0-255]
						tmpRead &= 0x00ff;
						// If tmpRead is now 255 (was -1 before bit twiddling),
						// that means we reached end of stream (ie. client
						// closed socket).
						if (tmpRead == 255) {
							throw new EOFException("End of stream");
						} else if (tmpRead >= ClientPacketType.values().length) {
							// Invalid packet type...
							session.disconnect();
						} else {
							type = ClientPacketType.values()[tmpRead];
							remaining = 0;
							sizeBytes = 4;
						}
					} else {
						// Read in as many bytes as we can
						readBytes = this.socket.getInputStream().read(data,
								currentPosition, remaining);

						if (readBytes == -1) {
							throw new EOFException("End of stream.");
						}

						remaining -= readBytes;
						currentPosition += readBytes;

						// If we have 0 remaining, then we have a complete
						// packet.
						if (remaining == 0) {
							// Test the packet to make sure it is valid.
							System.out.println("<- " + type + "("
									+ (data.length + 5) + ")");
							session.getServer().receivePacket(
									new ServerReceivedPacket(this.session,
											type, data));
							currentPosition = 0;
							type = null;
						}
					}
				} catch (SocketTimeoutException e) {
					// This is fine...
				} catch (IOException e) {
					// Socket disconnected!
					session.disconnect();
					return;
				}

			} while (ticks > System.currentTimeMillis());

			// Handle writes
			ticks = System.currentTimeMillis() + WRITE_TICKS;
			do {
				if (!outgoingQueue.isEmpty()) {
					outgoingPacket = outgoingQueue.remove();
					try {
						this.socket.getOutputStream().write(outgoingPacket);
					} catch (IOException e) {
						session.disconnect();
					}
				} else {
					try {
						Thread.sleep(5);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			} while (ticks > System.currentTimeMillis());
		}

		// At this point we close the socket.
		if (socket.isConnected()) {
			try {
				socket.close();
			} catch (IOException e) {
			}
		}

	}
}