package org.han.mc.bungee.module;

import java.util.Timer;
import java.util.TimerTask;

import org.han.bot.BotCon;
import org.han.link.GetDetails;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;
import org.han.xlib.Debug;

import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class TopicLoader {
	public static boolean enabled = false;
	static Timer timer;

	public static void load() {
		deload();
		if (BPlugin.Config.isTUDisabled())
			return;
		Debug.rep("Loadind Topic changer");
		timer = new Timer();
		timer.schedule(new AutoUpdate(), 10000, BPlugin.Config.ServerTopicUpdate() * 60000);
		enabled = true;
	}

	public static void deload() {
		if (enabled) {
			Debug.out("Deloading Topic changer");
			if (timer != null)
				timer.cancel();
		}
		enabled = false;
	}

	static public class AutoUpdate extends TimerTask {

		public String gettopicfilter(String serv) {
			if (BPlugin.Config.isGlobal()) {
				return BPlugin.Config.gettopicformat();
			}
			return BPlugin.ServConfig.ServerTopic(serv);
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
}
