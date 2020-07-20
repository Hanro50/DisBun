package org.han.link;

import org.han.bot.BotCon;
import org.han.mc.bungee.BPlugin;
import org.han.xlib.Debug;
import net.dv8tion.jda.api.entities.Guild;

public class GetDetails {
	static Guild Temp;
	public static Guild getGuild() throws IllegalStateException {
		if (Temp != null && Temp.getId().equals(BPlugin.Config.GetServID())) return Temp;
		try {
			Temp = BotCon.getJDA().getGuildById(BPlugin.Config.GetServID());
			if (Temp == null) {
				throw new IllegalStateException("Serv Value not enitialize properly");
			}
			return Temp;
		} catch (NumberFormatException F) {
			Debug.wrn("Serv Value not enitialize properly");
			throw new IllegalStateException("Serv Value not enitialize properly");
		}
	}

	static public boolean Write(String value) {
		return BPlugin.Config.SetServID(value);
	}

}
