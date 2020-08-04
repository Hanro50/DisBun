package org.han.mc.bungee.module;

import java.util.Timer;
import java.util.TimerTask;

import org.han.bot.BotCon;
import org.han.link.GetDetails;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.ModuleLoader;
import org.han.xlib.Debug;

import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class TopicLoader extends DisBunTimerModule {
	static final String Format = "Format.";
	static final String GlobalFormat = Format + "Global";
	private static final String Utopicglobal = "Global";

	public void load(ModuleLoader Loader) {

		Debug.rep("Loadind Topic changer");
		timer = new Timer();
		timer.schedule(new AutoUpdate(Loader, this), 10000, Loader.UPDATETIMER(this) * 60000);
		enabled = true;
	}

	public void deload() {
		if (enabled) {
			Debug.rep("Deloading Topic changer");
			if (timer != null)
				timer.cancel();
		}
		enabled = false;
	}

	public class AutoUpdate extends TimerTask {
		ModuleLoader Loader;
		DisBunTimerModule mod;

		AutoUpdate(ModuleLoader Loader, DisBunTimerModule mod) {
			this.Loader = Loader;
			this.mod = mod;
		}

		public String gettopicfilter(String serv) {
			if (Loader.ChkBool(mod, Utopicglobal)) {
				return Loader.ChkStr(mod, GlobalFormat);
			}
			return Loader.ChkStr(mod, Format + serv);
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Debug.rep("Updating channel topics");
			TextMsg.getchannels().forEach(channelID -> {
				String out = "";
				for (String Serv : TextMsg.GetServ(channelID)) {
					out = out + gettopicfilter(Serv).replaceAll("%PlayerCount%", //
							String.valueOf(BPlugin.getproxyserv().getServerInfo(Serv).getPlayers().size()))
							.replaceAll("%ServName%", Serv);
				}
				if (out.length() > 1024) {
					out = out.substring(0, 1020) + "...";

				}
				try {
					GetDetails.getGuild().getGuildChannelById(channelID).getManager().setTopic(out).complete();
				} catch (InsufficientPermissionException error) {
					Debug.err(
							"This module has crashed due to insuffeciant permissions [" + error.getPermission() + "]");
					Debug.err("[" + error.getGuild(BotCon.getJDA()).getName() + "] ["
							+ error.getChannel(BotCon.getJDA()).getName() + "]");
					deload();
				}

			});
		}

	}

	@Override
	public String HelpText() {
		// TODO Auto-generated method stub
		return "Should the topic changer module be enabled";
	}

	@Override
	public int DefaultTime() {
		// TODO Auto-generated method stub
		return 11;
	}

	@Override
	public String Name() {
		// TODO Auto-generated method stub
		return "TopicLoader";
	}

	@Override
	public void AdCon(ModuleLoader Loader) {
		// TODO Auto-generated method stub
		Loader.regSettings(this, "Should topic updates be set globally?", Utopicglobal, "true");
		Loader.regSettings(this,
				"Server topic format\n %ServName% for the server name\n %PlayerCount% for the player count",
				GlobalFormat, "%PlayerCount% player(s) are currently playing on %ServName%");
		

		for (String f : BPlugin.getservinfo().keySet()) {
			Loader.regSettings(this,
					"Server topic format\n %ServName% for the server name\n %PlayerCount% for the player count",
					Format + f, "%PlayerCount% player(s) are currently playing on %ServName%");
		}

		// .forEach(f -> {
		//
		//
		// });
	}

}
