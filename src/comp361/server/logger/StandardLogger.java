package comp361.server.logger;

public class StandardLogger extends Logger {
	public void debug(String s) {
		if (getLevel().compareTo(Logger.Level.DEBUG) > 0) {
			return;
		}
		
		System.out.println("[DEBUG] " + s);
	}

	public void warn(String s) {
		if (getLevel().compareTo(Logger.Level.WARN) > 0) {
			return;
		}
		
		System.out.println("[WARN] " + s);
	}

	public void error(String s) {
		if (getLevel().compareTo(Logger.Level.ERROR) > 0) {
			return;
		}
		
		System.err.println("[ERROR] " + s);
	}

	public void fatal(String s) {
		System.err.println("[FATAL] " + s);
	}
}
