package org.han.xlib;

/**
 * This is used by the debug class
 * 
 * @author hanro50
 *
 */
public abstract class Remapper {

	static protected ASCII_CODES HD = ASCII_CODES.RESET;

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
			if (!Debug.getAsciidebug()) {
				return Key2;
			}
			return Key;
		};
	}

	public abstract void out(String in, StackTraceElement obj);

	public abstract void err(String in, StackTraceElement obj);

	public abstract void Trace(Throwable e, StackTraceElement obj);
}
