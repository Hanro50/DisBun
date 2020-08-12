package org.han.link;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.han.xlib.Remapper;

public class LogDebug extends Remapper {
	static Logger out;

	public LogDebug(Logger Logger) {
		out = Logger;
	}

	@Override
	public void out(String in, StackTraceElement obj) {
		// TODO Auto-generated method stub
		in = in.trim();
		if (in.contains("\n")) {
			for (String s : in.split("\n")) {
				out.info(ASCII_CODES.Bright_WHITE + s + ASCII_CODES.RESET);
			}
		} else {
			out.info(ASCII_CODES.Bright_WHITE + in + ASCII_CODES.RESET);
		}

	}

	@Override
	public void err(String in, StackTraceElement obj) {
		// TODO Auto-generated method stub
		out.severe("[" + obj.getClassName() + "]:");

		in = in.trim();
		if (in.contains("\n")) {
			for (String s : in.split("\n")) {
				out.severe("\t" + ASCII_CODES.Bright_WHITE + s + ASCII_CODES.RESET);
			}
		} else {
			out.severe("\t" + ASCII_CODES.Bright_WHITE + in + ASCII_CODES.RESET);
		}
	}

	@Override
	public void Trace(Throwable e, StackTraceElement obj) {
		// TODO Auto-generated method stub
		out.severe("[" + obj.getClassName() + "]");

		for (StackTraceElement Trace : e.getStackTrace()) {
			out.severe(ASCII_CODES.Bright_RED + "\t<E> " + ASCII_CODES.Bright_WHITE
					+ Trace.toString().replaceFirst("\\(", ASCII_CODES.CYAN + "(") + ASCII_CODES.RESET);
		}
	}
}
