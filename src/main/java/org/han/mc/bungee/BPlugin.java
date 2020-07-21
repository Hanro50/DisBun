package org.han.mc.bungee;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

import org.han.bot.BotCon;
import org.han.bot.JDAIN;
import org.han.link.LinkPrep;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.com.*;
import org.han.mc.bungee.module.PlaceHolderapiServerSide;
import org.han.mc.bungee.module.TopicLoader;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Plugin;

public class BPlugin extends Plugin {

	public static BPlugin Self;
	public static BConfig Config;
	public static BServerConfig ServConfig;

	public static void main(String[] w) {
		FileObj.Init();
		Debug.boot(w);

		Config = new BConfig();

		Debug.out(Config.GetToken());

		BotCon.Start();
		JDAIN T = new JDAIN();
		try {
			BotCon.getJDA().awaitReady();

		} catch (InterruptedException | IllegalStateException e) {
			// TODO Auto-generated catch block
			Debug.Trace(e);
		}
		TextMsg.SaveLink(724534746058588203L, "survival");
		T.sendfromMC("survival", "Hanro50", UUID.fromString("6ad09b98-7cba-4558-adeb-a2d28257d710"), "testing");
	}

	public static String RelinkText(ProxiedPlayer player) {
		return RelinkText(player, "\n");
	}

	public static String RelinkText(ProxiedPlayer player, String leading) {
		return (player.hasPermission(Linkcom.permission) || !Config.isPermcheck() ? ChatColor.RED + leading + "DM "
				+ BotCon.getJDA().getSelfUser().getName() + " the following message<" + ChatColor.WHITE
				+ LinkPrep.Primer(player.getUniqueId()) + ChatColor.RED + "> to link your account" : "");
	}

	public static void reloadConfig() {
		Debug.out("Loading:");
		Config = new BConfig();
		ServConfig = new BServerConfig(getproxyserv().getServers().keySet());
		Debug.Debug = Config.DebugMode();
		SrvrunAsync(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Collection<ProxiedPlayer> F = getproxyserv().getPlayers();
				if (F.size() > 0) {
					Debug.out("Running checks");
					F.forEach(player -> {
						if (LinkUp.isforcedlink(player.getServer().getInfo().getName())
								&& LinkUp.GetDiscordID(player.getUniqueId()) == null) {
							String send = ChatColor.BOLD + "" + ChatColor.GREEN //
									+ "\n This server now requires you to link your discord account to join it"
									+ RelinkText(player) + " and regain access";
							player.disconnect(new ComponentBuilder(send).create());
						}
					});

				}

				BotCon.Start();

				if (BotCon.isRunning()) {
					PermCalc.load();
					TopicLoader.load();
					PlaceHolderapiServerSide.load();
					PlaceHolderapiServerSide.SYNC();
				}
				if (Config.isGlobal()) {
					Debug.rep("Chat format sync pulse being sent out");
					getproxyserv().getServers().keySet().forEach(Fserv -> {
						Events.ConfigUpdate(getproxyserv().getServers().get(Fserv));
						Debug.rep("Notified: " + Fserv);
					});
				}
				Debug.rep("Done loading");
			}
		});
	}

	@Override
	public void onEnable() {
		// You should not put an enable message in your plugin.
		// BungeeCord already does so
		Self = this;
		getProxy().registerChannel(TextMsg.Channel);
		getProxy().getPluginManager().registerListener(this, new Events());
		getProxy().getPluginManager().registerCommand(this, new Linkcom());
		getProxy().getPluginManager().registerCommand(this, new UnLinkcom());
		getProxy().getPluginManager().registerCommand(this, new Reloadcom());
		getProxy().getPluginManager().registerCommand(this, new InfoCom());
		FileObj.Init(getProxy().getPluginsFolder(), getDataFolder().getName());
		Debug.Override = getLogger();
		
		Debug.out("Plugin File : " + FileObj.ClassPath);
		reloadConfig();

		// BotCon.Start();

		// PermCalc.load();
		// TopicLoader.load();
		//Debug.out("Fully loaded");
	}

	@Override
	public void onDisable() {
		BotCon.Stop();
		PermCalc.deload();
		getLogger().info("Goodbye world...");

	}

	public static Map<String, ServerInfo> getservinfo() {
		return Self.getProxy().getServers();
	}

	public static ProxyServer getproxyserv() {
		return Self.getProxy();
	}

	public static void SrvrunAsync(Runnable runner) {
		ProxyServer.getInstance().getScheduler().runAsync(Self, runner);
	}

}
