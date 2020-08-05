package org.han.bot.com;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.han.bot.Print;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import net.dv8tion.jda.api.entities.User;

public class Operator {
	public static boolean isOped(User user) {
		return data.load().Info.contains(user.getId()) || (Msg.isBotOwner(user));
	}

	 public static void add(Msg m, User user) {
		if (isOped(user)) {
			Print.Err(m, "User already oped");
		}

		data T = data.load();
		T.Info.add(user.getId());
		T.save();
		Print.Suc(m);
	}

	 public static void rem(Msg m, User user) {
		if (isOped(user)) {
			if (m.isBotOwner()) {
				Print.Err(m, "User cannot be deoped as they are the bot owner");
				return;
			}
			data T = data.load();
			T.Info.remove(user.getId());
			T.save();
			Print.Suc(m);
			return;
		}
		Print.Err(m, "User cannot be deoped as they are not oped");
	}

	static class data {
		List<String> Info;
		static data self;

		static data load() {
			if (self == null) {
				File F = FileObj.Fetch("", "trusted", "json");
				try {
					self = FileObj.fromjson(FileObj.read(F)[0], data.class);
				} catch (IOException e) {
					Debug.out("Generating new operator file");
					self = new data();
					self.save();
				}
			}
			return self;
		}

		data() {
			Info = new ArrayList<String>();
		}

		void save() {
			File F = FileObj.Fetch("", "trusted", "json");
			try {
				String[] T = { FileObj.tojson(this),
						"\\\\Use the trust command as the bot owner to add users to this list" };
				FileObj.writeNF(T, F, "operator file");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
