package org.whstsa.library.util;

public class Logger {
	
	public static boolean DEBUG = true;

	@Deprecated
	public static final Logger DEFAULT_LOGGER = new Logger();
	
	private static final int TAG_SPACING = 10;
	
	private static final String[] LOG_COLORS = {
		Constants.ANSI_BLUE
	};
	
	private static final String[] WARN_COLORS = {
		Constants.ANSI_RED
	};
	
	private static final String[] ERR_COLORS = {
		Constants.ANSI_BOLD,
		Constants.ANSI_RED
	};
	
	private static final String[] DEBUG_COLORS = {
		Constants.ANSI_ITALICS,
		Constants.ANSI_BLUE
	};
	
	public enum Level {
		INFO(LOG_COLORS, "Info"), WARN(WARN_COLORS, "Warning"), ERROR(ERR_COLORS, "Error"), DEBUG(DEBUG_COLORS, "Debug");
		
		public final String[] decoration;
		public final String[] tags;
		
		private Level(String[] decoration, String[] tags) {
			this.decoration = decoration;
			this.tags = tags;
		}
		
		private Level(String[] decoration, String tag) {
			this(decoration, new String[] {tag});
		}
	}
	
	public final Object[] tags;
	
	public Logger(String ...tags) {
		this.tags = tags;
	}
	
	public Logger(Logger parent, String ...globalTags) {
		this.tags = ArrayUtils.merge(parent.tags, globalTags);
	}
	
	public static String assertion(boolean condition) {
		return condition ? "YES": "NO";
	}
	
	public void log(String[] tags, String ...content) {
		this.print(Level.INFO, tags, content);
	}
	
	public void log(String ...content) {
		this.print(Level.INFO, content);
	}
	
	public void warn(String[] tags, String ...content) {
		this.print(Level.WARN, tags, content);
	}
	
	public void warn(String ...content) {
		this.print(Level.WARN, content);
	}
	
	public void error(String[] tags, String ...content) {
		this.print(Level.ERROR, tags, content);
	}
	
	public void printStacktrace(Exception e) {
		Logger.output(Level.ERROR.decoration, new String[] {}, e.getStackTrace());
	}
	
	public void error(String ...content) {
		this.print(Level.ERROR, content);
	}
	
	public void debug(String ...content) {
		if (DEBUG) {
			this.print(Level.DEBUG, content);
		}
	}
	
	public void print(Level logLevel, Object[] tags, String ...content) {
		this.output(logLevel.decoration, ArrayUtils.merge(logLevel.tags, tags), content);
	}
	
	public void print(Level logLevel, String ...contents) {
		this.print(logLevel, new String[] {}, contents);
	}
	
	private void output(String[] colors, Object[] tags, String ...content) {
		Logger.output(colors, this.tags, tags, content);
	}
	
	private static void output(String[] colors, Object[] globalTags, Object[] tags, String ...content) {
		StringBuilder computedTags = new StringBuilder();
		for (Object s : globalTags) {
			computedTags.append(Logger.createTag(s));
		}
		for (Object s : tags) {
			computedTags.append(Logger.createTag(s));
		}
		System.out.println(ArrayUtils.join(colors) + computedTags + ArrayUtils.join(content) + Constants.ANSI_RESET);
	}
	
	private static String createTag(Object rawTag) {
		String tag;
		try {
			tag = (String) rawTag;
		} catch (ClassCastException e) {
			return "";
		}
		tag = tag.toUpperCase();
		if (tag.length() > (Logger.TAG_SPACING - 2)) {
			tag = tag.substring(0, Logger.TAG_SPACING - 2);
		}
		int spacesNeeded = (Logger.TAG_SPACING - 2) - tag.length();
		String assembledTag = "[";
		for (int i = 0; i <= spacesNeeded; i++) {
			assembledTag += " ";
		}
		return assembledTag + tag + "] ";
	}
}
