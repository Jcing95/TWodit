package de.jcing.util;

import java.io.PrintStream;
import java.util.ArrayList;

public abstract class Log {

	public enum LEVEL {
		debug, info, error, disable
	}

	private static LEVEL level = LEVEL.debug;

	private static ArrayList<PrintStream> additionalPrintstreams;
	private static final PrintStream debug = System.out;
	private static final PrintStream info = System.out;
	private static final PrintStream error = System.err;

	private static final String debugPrefix = "DBG - ";
	private static final String infoPrefix = "INF - ";
	private static final String errorPrefix = "ERR - ";

	private static final int callerIndex = 2;

	public static void debug(String message) {
		if (level.compareTo(LEVEL.debug) <= 0) {
			print(getCallerName(callerIndex) + "(): ", message, debugPrefix, debug);
		}
	}

	public static void debug(String name, String message) {
		if (level.compareTo(LEVEL.debug) <= 0) {
			print(name, message, debugPrefix, debug);
		}
	}

	public static void info(String message) {
		if (level.compareTo(LEVEL.info) <= 0) {
			print(getCallerName(callerIndex) + "(): ", message, infoPrefix, info);
		}
	}

	public static void info(String name, String message) {
		if (level.compareTo(LEVEL.info) <= 0) {
			print(name, message, infoPrefix, info);
		}
	}

	public static void error(String message) {
		if (level.compareTo(LEVEL.error) <= 0) {
			print(getCallerName(callerIndex) + "(): ", message, errorPrefix, error);
		}
	}

	public static void error(String name, String message) {
		if (level.compareTo(LEVEL.error) <= 0) {
			print(name, message, errorPrefix, error);
		}
	}

	public static void print(String name, String message, String prefix, PrintStream stream) {
		String msg = prefix + name + message;
		stream.println(msg);
		if (additionalPrintstreams != null) {
			for (PrintStream s : additionalPrintstreams)
				s.println(msg);
		}
	}

	public static PrintStream getDebug() {
		return debug;
	}

	public static PrintStream getInfo() {
		return info;
	}

	public static PrintStream getError() {
		return error;
	}

	public static String getCallerName(int index) {
		Exception e = new Exception();
		return e.getStackTrace()[index].getClassName() + "." + e.getStackTrace()[index].getMethodName();
	}

	public static String getCallerClass(int index) {
		return new Exception().getStackTrace()[index].getClassName();
	}

	public static Logger getInstance() {
		return new Logger(getCallerName(callerIndex));
	}

	public static void setLevel(LEVEL level) {
		Log.level = level;
	}

	public static class Logger {
		private LEVEL level = LEVEL.debug;
		private String suffix;
		private String name;

		public Logger(String name) {
			this.name = name;
			suffix = "";
		}

		public Logger setSuffix(String suffix) {
			this.suffix = suffix;
			return this;
		}

		public Logger setName(String name) {
			this.name = name;
			return this;
		}

		public Logger setLevel(LEVEL level) {
			this.level = level;
			return this;
		}

		public void debug(String message) {
			if (level.compareTo(LEVEL.debug) <= 0 && Log.level.compareTo(LEVEL.debug) <= 0) {
				print(name + suffix + ": ", message, debugPrefix, debug);
			}
		}

		public void info(String message) {
			if (level.compareTo(LEVEL.info) <= 0 && Log.level.compareTo(LEVEL.info) <= 0) {
				print(name + suffix + ": ", message, infoPrefix, info);
			}
		}

		public void error(String message) {
			if (level.compareTo(LEVEL.error) <= 0 && Log.level.compareTo(LEVEL.error) <= 0) {
				print(name + suffix + ": ", message, errorPrefix, error);
			}
		}
	}

}
