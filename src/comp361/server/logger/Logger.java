package comp361.server.logger;

public abstract class Logger {
	private Level level;
	
	public Logger() {
		this(Level.DEBUG);
	}
	
	public Logger(Logger.Level l) {
		this.level = l;
	}
	
	public Level getLevel() {
		return this.level;
	}
	
	public void setLevel(Level l) {
		this.level = l;
	}
	
	public abstract void debug(String s);
	public abstract void warn(String s);
	public abstract void error(String s);
	public abstract void fatal(String s);
	
	enum Level {
		DEBUG,
		WARN,
		ERROR,
		FATAL;
	}
}
