package comp361.server.console;

import java.io.InputStream;
import java.io.PrintStream;

/**
 * This interface represents some kind of console that the server can output to.
 * Having it in an interface allows us to easily swap out a text console with a
 * GUI console.
 */
public interface Console {

	public InputStream in();

	public PrintStream out();

}
