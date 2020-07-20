package org.han.xlib;

/*
 * Copyright 2020 Hanro
 * 
 * All subsequent copies of this code should contain this header in addition to any other license information.
 * 
 * If no other license information is provided by original author then:
 * All rights reserved by original author
 */
import java.io.File;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Hanro Debug Library
 * 
 * @author hanro50
 * @version 2.0
 */
public class Debug {
	static Debug DB;

	static public final String version = "0.0.1-SnapShot";
	/**
	 * Override this debug library. Useful if you have an external debug library
	 */
	public static Logger Override;
	/**
	 * Whether {@link #wrn(String) Debug.wrm(String)} and {@link #rep(String)
	 * Debug.rep(String)} should display a message
	 */
	static public boolean Debug = true;
	static public boolean Asciidebug = true;
	static private String Lastclass = "";
	static private ASCII_CODES HD = ASCII_CODES.RESET;
	static private final File LOG = FileObj.Fetch("", "Log", "txt");

	public static void boot(String[] args) {
		LOG.delete();
		System.setProperty("app.path", FileObj.ClassPath);
		if (args != null && args.length > 0) {
			for (String str : args) {
				String str0 = str.toLowerCase().trim();
				if (str0.contains("nocolor") || str0.contains("nocolour")
						|| System.getProperty("os.name").contains("Window")) {
					Asciidebug = false;
					out("Turning off Ascii mode");
					break;
				}
			}
		}
		if (Asciidebug)
			out("If you see Ascii controll characters. Launch with the perameter \"nocolour\".");
	}

	private static boolean getAsciidebug() {
		return Asciidebug;
	}

	public static void Version() {
		out("Printing Debug Information:");
		System.setProperty("app.version", "Barcode scanner " + version);
		System.setProperty("app.date", (new Date()).toString());
		System.setProperty("app.author", "Hanro50");

		List<String> keys = new ArrayList<String>();
		Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
		keys.addAll(System.getProperties().stringPropertyNames());
		keys.sort(cmp);

		for (int i = 0; i < keys.size(); i++) {
			out("-> " + String.format("%-30s", keys.get(i)) + " : " + System.getProperty(keys.get(i)));
		}
		out("Continuing on with rest of application->");
	}

	/**
	 * For printing errors to the output log
	 * 
	 * @see Also see {@link #wrn(String) Debug.wrm(String)} for a debug version of
	 *      this command.
	 * @param Line The output line
	 */
	public static void err(String Line) {
		Error(Line, "[E]");
	}

	/**
	 * This command is disabled when this program is not in debug mode. For printing
	 * errors to the output log
	 * 
	 * @see Also see {@link #err(String) Debug.err(String)} for a non debug version
	 *      of this command.
	 * @param Line The output line
	 */
	public static void wrn(String Line) {
		if (Debug) {
			Error(Line, "[W]");
		}
	}

	/**
	 * Internal binder for printing error messages to the console
	 * 
	 * @see Also see {@link #wrn(String) Debug.wrn(String)} for a debug binder of
	 *      this command.
	 * @see Also see {@link #err(String) Debug.err(String)} for a non debug binder
	 *      of this command.
	 * @param Line
	 */
	protected static void Error(String Line, String Type) {
		Line = Type + ":" + Line;
		if (Override != null) {
			String out = Caller(ASCII_CODES.Bright_RED, false, " : ") + Line;
			Override.log(Level.WARNING, out);
			writetolog(out);
			return;
		}

		String out = Format(Line, ASCII_CODES.Bright_YELLOW, ASCII_CODES.Bright_WHITE, ASCII_CODES.Tab);
		if (Debug) {
			System.err.println(out);
		}
		writetolog(out);
	}

	/**
	 * This command is disabled when this program is not in debug mode
	 * 
	 * @see Also see {@link #out(String) Debug.out(String)} for a non debug version
	 *      of this command.
	 * @param Line The output line
	 */

	public static void rep(String Line) {
		if (Debug) {
			info(Line, "[R]");
		}
	}

	/**
	 * Prints a formated message to the output console.
	 * 
	 * @see Also see {@link #rep(String) Debug.rep(String)} for a debug version of
	 *      this command.
	 * @param Line
	 */
	public static void out(String Line) {
		info(Line, "[I]");
	}

	/**
	 * Internal binder for printing messages to the console
	 * 
	 * @see Also see {@link #rep(String) Debug.rep(String)} for a debug binder of
	 *      this command.
	 * @see Also see {@link #out(String) Debug.out(String)} for a non debug binder
	 *      of this command.
	 * @param Line
	 */
	protected static void info(String Line, String Type) {
		Line = Type + " : " + Line;
		if (Override != null) {
			Override.info(Line);
			writetolog(Line);
			return;
		}

		String out = Format(Line, ASCII_CODES.Bright_GREEN, ASCII_CODES.Bright_WHITE, ASCII_CODES.Tab);
		System.out.println(out);
		writetolog(out);
	}

	/**
	 * Output to a log file
	 * 
	 * @param Line
	 */

	protected static void writetolog(String Line) {
		String FL = "";
		try {
			if (!LOG.exists()) {
				if (!LOG.createNewFile()) {
					throw new IOException("Could not save Log");
				}
			} else {
				FL = FileObj.read(LOG, "\n");
			}

			FL = FL + Line.replaceAll("\033\\[.*?m", "");

			FileWriter writer = new FileWriter(LOG);

			writer.write(FL);
			writer.close();

			if (((double) LOG.length() / (1024)) > 50) {
				LOG.delete();
				err("Error Log File to big!");
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			Trace(e);
		} catch (StackOverflowError e) {
			System.exit(0);
			LOG.delete();
			err("Error Log File to big!");
			Trace(e);
		}
	}

	/**
	 * Trace binder for debug output.
	 * 
	 * @param e
	 */
	public static void Trace(Throwable e) {

		if (Override != null) {
			// Override.log(Level.WARNING, e.getMessage(), e);

			Override.severe(Caller(ASCII_CODES.Bright_RED));
			Override.severe(ASCII_CODES.Bright_YELLOW + "" + ASCII_CODES.Tab + "<" + e.toString().replace(": ", "> [")
					+ "] {" + ASCII_CODES.RESET);
			for (StackTraceElement STE : e.getStackTrace()) {
				Override.severe(ASCII_CODES.Tab.toString() + ASCII_CODES.Tab.toString() + ASCII_CODES.Bright_RED + "<E>"
						+ ASCII_CODES.Bright_WHITE + " " + STE.toString().replaceFirst("\\(", ASCII_CODES.CYAN + "(")
						+ " " + ASCII_CODES.RESET);
			}
			Override.severe(ASCII_CODES.Bright_YELLOW + (ASCII_CODES.Tab + "}") + ASCII_CODES.RESET);

			return;
		}

		System.err.println(Caller(ASCII_CODES.Bright_RED) + ASCII_CODES.Bright_YELLOW + ASCII_CODES.Tab + "<"
				+ e.toString().replace(": ", "> [") + "] {" + ASCII_CODES.RESET);
		for (StackTraceElement STE : e.getStackTrace()) {
			System.err.println(ASCII_CODES.Tab.toString() + ASCII_CODES.Tab.toString() + ASCII_CODES.Bright_RED + "<E>"
					+ ASCII_CODES.Bright_WHITE + " " + STE.toString().replaceFirst("\\(", ASCII_CODES.CYAN + "(")
					+ ASCII_CODES.RESET);
		}
		System.err.println(ASCII_CODES.Bright_YELLOW + (ASCII_CODES.Tab + "}") + ASCII_CODES.RESET);
	}

	// Header writer
	private static String Caller(ASCII_CODES headerc) {
		return Caller(headerc, true, "\n");
	}

	private static String Caller(ASCII_CODES headerc, boolean combine, String Endchar) {
		String ls = "";
		CHK: {
			Exception e = new Exception();
			for (StackTraceElement STE : e.getStackTrace()) {
				if (STE.getClassName() != Debug.class.getName()) {
					ls = STE.getClassName();
					break CHK;
				}
			}
			ls = "UNKNOWN";
		}

		if (Lastclass.equals(ls) && (HD == headerc) && combine)
			return "";
		Lastclass = ls;
		HD = headerc;
		return headerc + "[" + Lastclass + "]" + ASCII_CODES.RESET + Endchar;

	}

	// Formatter
	private static String Format(String Line, ASCII_CODES headerc, ASCII_CODES bodyc, ASCII_CODES indent) {
		return Caller(headerc) + bodyc + indent
				+ Line.trim().replaceAll("\n", ASCII_CODES.RESET + "\n" + bodyc + indent) + ASCII_CODES.RESET;
	}

	// Ascii color printing
	public static enum ASCII_CODES {
		// Normal colors|Bright versions
		RESET(0), Tab("    "), // Special
		BLACK(30), Bright_BLACK(30, 1), // Black
		RED(31), Bright_RED(31, 1), // Red
		GREEN(32), Bright_GREEN(32, 1), // Green
		YELLOW(33), Bright_YELLOW(33, 1), // Yellow
		BLUE(34), Bright_BLUE(34, 1), // Blue
		PURPLE(35), Bright_PURPLE(35, 1), // Purple
		CYAN(36), Bright_CYAN(36, 1), // Cyan
		WHITE(37), Bright_WHITE(37, 1); // White

		ASCII_CODES(String value) {
			Key2 = value;
			Key = value;
		}

		ASCII_CODES(int value) {
			if (value != 0)
				Key = "\033[0m\033[" + value + "m";
			else
				Key = "\033[" + value + "m";
			Key2 = "";
		}

		ASCII_CODES(int value1, int value2) {
			Key = "\033[0m" + "\033[" + value1 + ";" + value2 + "m";
			Key2 = "";
		}

		// 2 Key values are needed as the non Ascii "Custom characters" need to be
		// accounted for
		final String Key2;
		final String Key;

		public String toString() {
			if (!getAsciidebug()) {
				return Key2;
			}
			return Key;
		};
	}

	/*
	 * 
	 * 
	 * public static void out(String Line) { Log.info(Line);
	 * 
	 * } public static void Trace(Throwable e) { Log.log(Level.WARNING,
	 * e.getMessage(), e); } public static void err(String Line) {
	 * Log.log(Level.WARNING, Line); }
	 */
}
