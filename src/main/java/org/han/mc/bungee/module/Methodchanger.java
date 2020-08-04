package org.han.mc.bungee.module;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.han.bot.BotCon;
import org.han.mc.bungee.ModuleLoader;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.RichPresence;

public class Methodchanger extends DisBunTimerModule {
	List<String> Methods;
	static final String[] Default = {
			"(First line is ignored)#type (0-playing,1-Streaming,2-Listening,3-Watching,4-Custom(Broken)),Message,Link",
			"1,message!", "0,minecraft,", "1,the Disbun github page,https://github.com/Hanro50/DisBun" };

	public void load(ModuleLoader Loader) {

		Debug.rep("Activity changer loading...");
		enabled = true;

		File F = FileObj.Fetch("", "MethodFile", "txt");
		String[] Motds = null;
		// TS = System.nanoTime();
		if (!F.exists()) {
			try {
				FileObj.writeNF(Default, F, "Stock configuration files");
				Motds = Default;
			} catch (IOException e) {
			}
		} else {
			try {
				Motds = FileObj.read(F);
			} catch (IOException e) {
				Debug.Trace(e);
			}
		}
		if (Motds == null || Motds.length < 1) {
			Debug.err("INVALID MOTD FILE");
			Debug.err("Using default");
			Motds = Default;
		}
		Methods = new ArrayList<String>();
		for (String Motd : Motds) {
			Methods.add(Motd);
		}

		timer = new Timer();
		timer.schedule(new Updateter(), 10, 60000 * Loader.UPDATETIMER(this));
	}

	public void deload() {
		if (Methods != null)
			Methods.clear();
		if (timer != null)
			timer.cancel();
		timer = null;
		enabled = false;
	}

	public class Updateter extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			Debug.rep("Changing activity");
			final int i = (int) (Math.abs(Math.random() * Methods.size() - 1) + 1);

			String[] F = Methods.get(i).split(",");

			if (F.length < 2) {
				F = new String[] { "0", Methods.get(i) };
			}
			final String[] FINALF = F;
			Activity act = new Activity() {
				@Override
				public boolean isRich() {
					// TODO Auto-generated method stub
					return false;
				}

				@Override
				public RichPresence asRichPresence() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public String getName() {
					// TODO Auto-generated method stub
					return FINALF[1];
				}

				@Override
				public String getUrl() {
					// TODO Auto-generated method stub
					if (FINALF.length >= 3) {
						return FINALF[2];
					}
					return null;
				}

				@Override
				public ActivityType getType() {
					// TODO Auto-generated method stub
					try {
						return ActivityType.fromKey(Integer.valueOf(FINALF[0]));
					} catch (NumberFormatException e) {
					}
					Debug.err("Format error on line " + i + " in methodfile");
					return ActivityType.DEFAULT;
					// return ActivityType.CUSTOM_STATUS;
				}

				@Override
				public Timestamps getTimestamps() {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public Emoji getEmoji() {
					// TODO Auto-generated method stub
					return null;
				}

			};

			BotCon.getJDA().getPresence().setActivity(act);
			Debug.rep("Done changing activity");
			return;

		}

		// Debug .err("Line " + i + " in the method file is not valid!");

	}

	@Override
	public String HelpText() {
		// TODO Auto-generated method stub
		return "If the method changer should be used";
	}

	@Override
	public int DefaultTime() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String Name() {
		// TODO Auto-generated method stub
		return "MethodChanger";
	}

	@Override
	public void AdCon(ModuleLoader Loader) {
		// TODO Auto-generated method stub

	}

}
