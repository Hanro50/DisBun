package org.han.xlib;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class Debug {
	static public boolean Debugmode = true;
	static public boolean Asciidebug = true;
	static public String appinfo = "";
	//
	//
	static Remapper Output;

	static boolean getAsciidebug() {
		return Asciidebug;
	}

	/**
	 * Use for stand alone apps
	 * 
	 * @param args
	 */
	public static void boot(String[] args) {
		// LOG.delete();
		Output = new DefaultMapping();
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

	public static void boot(Remapper Output, boolean EnableAccii) {
		// LOG.delete();
		Debug.Asciidebug = EnableAccii;
		Debug.Output = Output;
		System.setProperty("app.path", FileObj.ClassPath);

	}

	public static StackTraceElement GetLastMethod() {
		Exception e = new Exception();
		StackTraceElement ls = e.getStackTrace()[0];
		for (StackTraceElement STE : e.getStackTrace()) {
			if (STE.getClassName() != Debug.class.getName()) {
				ls = STE;
				break;
			}
		}
		return ls;
	}

	public static void Version() {
		out("Printing Debug Information:");
		System.setProperty("app.version", appinfo);
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
		if (Debugmode) {
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
		Line = Line.trim().replaceAll("\n", "\n" + Type + " : ");
		Output.err(Line, GetLastMethod());
	}

	/**
	 * This command is disabled when this program is not in debug mode
	 * 
	 * @see Also see {@link #out(String) Debug.out(String)} for a non debug version
	 *      of this command.
	 * @param Line The output line
	 */

	public static void rep(String Line) {
		if (Debugmode) {
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
		Line = Line.trim().replaceAll("\n", "\n" + Type + " : ");
		Output.out(Line, GetLastMethod());
	}

	public static void Crash(String Reason, Throwable e) {
		err("CRITICAL ERROR");
		err(Reason);
		Trace(e);
		System.exit(-1);

	}

	/**
	 * Trace binder for debug output.
	 * 
	 * @param e
	 */
	public static void Trace(Throwable e) {
		Output.Trace(e, GetLastMethod());
	}

	static class DefaultMapping extends Remapper {
		static private String Lastclass = "";
		// static private final File LOG = FileObj.Fetch("", "Log", "txt");

		private static String Caller(ASCII_CODES headerc, StackTraceElement obj) {
			String ls = obj.getClassName();

			if (Lastclass.equals(ls) && (HD == headerc))
				return "";
			Lastclass = ls;
			HD = headerc;
			return headerc + "[" + Lastclass + "]" + ASCII_CODES.RESET;

		}

		@Override
		public void out(String in, StackTraceElement obj) {
			// TODO Auto-generated method stub
			in = Caller(ASCII_CODES.Bright_GREEN, obj) + "\n\t"
					+ in.replaceAll("\n", ASCII_CODES.RESET + "\n\t" + ASCII_CODES.Bright_WHITE);
			System.out.println(in);

		}

		@Override
		public void err(String in, StackTraceElement obj) {
			// TODO Auto-generated method stub
			in = Caller(ASCII_CODES.Bright_YELLOW, obj) + "\n\t"
					+ in.replaceAll("\n", ASCII_CODES.RESET + "\n\t" + ASCII_CODES.Bright_WHITE);
			System.err.println(in);

		}

		@Override
		public void Trace(Throwable e, StackTraceElement obj) {
			// TODO Auto-generated method stub
			System.err.println(Caller(ASCII_CODES.Bright_RED, obj) + ASCII_CODES.Bright_YELLOW + ASCII_CODES.Tab + "<"
					+ e.toString().replace(": ", "> [") + "] {" + ASCII_CODES.RESET);
			for (StackTraceElement STE : e.getStackTrace()) {
				System.err.println(ASCII_CODES.Tab.toString() + ASCII_CODES.Tab.toString() + ASCII_CODES.Bright_RED
						+ "<E>" + ASCII_CODES.Bright_WHITE + " "
						+ STE.toString().replaceFirst("\\(", ASCII_CODES.CYAN + "(") + ASCII_CODES.RESET);
			}
			System.err.println(ASCII_CODES.Bright_YELLOW + (ASCII_CODES.Tab + "}") + ASCII_CODES.RESET);
		}
	}
}
