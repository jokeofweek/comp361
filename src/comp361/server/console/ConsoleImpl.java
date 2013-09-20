package comp361.server.console;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Implementation of {@link Console} which uses the standard Java System
 * console.
 */
public class ConsoleImpl implements Console {

	@Override
	public InputStream in() {
		return System.in;
	}

	@Override
	public PrintStream out() {
		return System.out;
	}

}
