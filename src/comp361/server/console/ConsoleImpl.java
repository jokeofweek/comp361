package comp361.server.console;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * Implementation of {@link Console} which uses the standard Java System
 * console.
 */
public class ConsoleImpl implements Console {

	@Override
	public void print(String string) {
		System.out.print(string);
	}

	@Override
	public void println(String string) {
		System.out.println(string);
	}
}
