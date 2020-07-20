package org.han.bot;

import java.util.ArrayList;
import java.util.Collection;

import javax.security.auth.login.LoginException;

import org.han.bot.com.Msg;
import org.han.mc.bungee.BPlugin;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;

import static net.dv8tion.jda.api.Permission.*;
import static org.han.xlib.Debug.*;

public class BotCon {
	static boolean Running = false;
	static JDA jda;

	public static boolean isRunning() {
		return Running;
	}

	public static JDA getJDA() {
		if (isRunning()) {
			return jda;
		}
		throw new IllegalStateException("Bot is not currently running");
	}

	public static Collection<Permission> perms() {
		Collection<Permission> ret = new ArrayList<Permission>();

		ret.add(MESSAGE_READ);
		ret.add(MESSAGE_WRITE);
		ret.add(MESSAGE_ADD_REACTION);
		ret.add(MESSAGE_MANAGE);
		ret.add(MESSAGE_EMBED_LINKS);
		ret.add(MESSAGE_ATTACH_FILES);
		ret.add(MESSAGE_HISTORY);
		
		ret.add(MANAGE_CHANNEL);
		ret.add(MANAGE_WEBHOOKS);
		ret.add(MANAGE_ROLES);
		return ret;
	}

	public synchronized static void Start() {
		String Token = BPlugin.Config.GetToken();
		if (isRunning()) Stop();
		
		try {
			jda = JDABuilder.createDefault(Token).addEventListeners(new JDAListener()).build();
			out("Starting bot");
			// out("Can't redirect JDA errors. Just ignore JDA's complaining");
			try {
				jda.awaitReady();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Trace(e);

			}
			Msg.AppInfo = jda.retrieveApplicationInfo().complete();
			if (BPlugin.Config.isShowInvite()) {
				out("Bot Invite:");
				out(Msg.AppInfo.getInviteUrl(0L, perms()));
			}
			out(jda.getSelfUser().getName() + " has been started\n");
			Running = true;

		} catch (LoginException | IllegalArgumentException e) {
			err("INVALID DISCORD TOKEN");
			err("Please update Token in config options");
			err("and then reload (/dbreload)");
		}
	}

	public synchronized static void Stop() {
		try {
			if (isRunning()) {
				out("Stopping bot");
				jda.shutdownNow();
			}
		} finally {
			Running = false;
		}
	}
}
