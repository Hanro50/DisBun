package org.han.bot;


import org.han.bot.com.Msg;
import org.han.mc.bungee.BPlugin;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

public class Print {
	static public Print Err(MessageChannel channel, User Mention, String Message) {
		return new Print(channel, Mention, "** %U ("+BPlugin.Langsys.JDAFail()+") ```" + Message + "```**");
	}

	static public Print Err(Msg m,String Message) {
		return new Print(m,"** %U ("+BPlugin.Langsys.JDAFail()+") ```" + Message + "```**");
	}

	static public Print Suc(MessageChannel channel, User Mention) {
		return new Print(channel, Mention, " %U "+BPlugin.Langsys.JDASuccess() );
	}

	static public Print Suc(Msg m) {
		return new Print(m," %U "+BPlugin.Langsys.JDASuccess() );
	}

	static public Print Out(MessageChannel channel, User Mention, String Message) {
		return new Print(channel, Mention, Message);
	}

	static public Print Out(Msg m,String Message) {
		return new Print(m,Message);
	}
	
	static public Print DM(User Mention, String Message) {
		return new Print(Msg.GetDM(Mention), Mention, Message);
	}

	static public Print DM(Msg m,String Message) {
		return new Print(m.GetDM(), m.Sender.User, Message);
	}
	

	// Special cases
	static final String U = " %U ";

	// Plain text handling
	// > ``` **
	static final String I = " □ ";
	static final String B = " ▢ ";
	static final String Q = " ▣ ";

	boolean indent = false;
	boolean bold = false;
	boolean qoate = false;

	String Text;
	MessageChannel chn;
	User Mention;
	
	public Print(Msg m ,String Text) {
		this.Text = Text;
		this.chn = m.Channel;
		this.Mention =  m.Sender.User;
		run();
	}

	public Print(MessageChannel channel, User Mention, String Message) {
		this.Text = Message;
		this.chn = channel;
		this.Mention = Mention;
		run();
	}

	public void run() {
		// User tag stuff
		Text = Text.replaceAll(U, " " + Mention.getAsMention() + " ");

		// Skips code if Message is under limit
		if (Text.length() <= 1995) {
			chn.sendMessage(Text).queue();
			return;
		}

		// If not. Start handling special characters
		Text = ("\n" + Text).replaceAll("```", I).replaceAll("\\*\\*", B).replaceAll("\n>>> ", Q);

		// Set up variables for phase 2
		int charcount = 0;
		String post = "";
		String split = "\n";
		String[] Lines = (Text + split).split(split);

		// Start loop
		for (int i = 0; i < Lines.length; i++) {
			// if segment can be sent without breaking anything up. Do it (1990 is used to
			// allow for breathing room)
			if ((Lines[i].length() + post.length() + charcount) < 1990) {
				Lines[i] = Check(Lines[i]);
				charcount = Lines[i].length() + post.length();

				post = post + Lines[i] + split;
			}
			// If not. Start decoding
			else {
				charcount = 0;
				// Checks the segment length
				if (Lines[i].length() > 500) {
					// Paragraphs produce segments that are to big. Lets use sentences
					String oldc = split;
					String[] TSA = (Lines[i]).split(".");
					if ((TSA.length <= 0) || (TSA[0].length() > 500)) {
						// Did someone forget punctuation...no matter?
						TSA = (Lines[i]).split(" ");
						if ((TSA.length <= 0) || (TSA[0].length() > 500)) {
							// WTF ARE YOU TRYING TO PRINT
							split = "";
						} else
							split = " ";
					} else
						split = ".";
					// We need to recompile the string array
					String T = Lines[i];
					for (int i2 = i + 1; i2 < Lines.length; i2++) {
						T = oldc + T;
					}
					// Lets reset everything and try again
					i = -1;
					Lines = (T).split(split);
				} else {
					// We need to split stuff apart. Lets not forget formatting
					post = post + (indent ? "```" : "") + (bold ? "**" : "");

					chn.sendMessage(post).complete();
					post = (qoate ? ">>> " : "") + (bold ? "**" : "") + (indent ? "```" : "") + Check(Lines[i]);
				} // end of inner if
			} // end of outer if
		} // end of loop
		chn.sendMessage(post).complete();

	}

	private String Check(String in) {
		boolean loopcheck = true;
		// System.out.println(in);
		while (loopcheck) {
			loopcheck = false;
			if (in.contains(I)) {
				in = in.replaceFirst(I, "```");
				indent = !indent;
				loopcheck = true;
				System.out.println("contains:" + in);
			} else if (in.trim().equals(I.trim())) {
				in = "```";
				indent = !indent;
				System.out.println("Is:" + in);
			}

			if (in.contains(B)) {
				in = in.replaceFirst(B, "**");
				bold = !bold;
				loopcheck = true;
				System.out.println("contains:" + in);
			} else if (in.trim().equals(B.trim())) {
				in = "**";
				bold = !bold;
				System.out.println("Is:" + in);
			}

			if ((!qoate) && (in.contains(Q))) {
				in = in.replaceFirst(Q, "\\n>>> ");
				qoate = true;
				loopcheck = true;
				System.out.println("contains:" + in);
			} else if ((!qoate) && in.trim().equals(Q.trim())) {
				in = "\n>>> ";
				qoate = true;
				System.out.println("Is:" + in);
			}
		}
		return in;
	}

}
