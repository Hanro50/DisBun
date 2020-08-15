package org.han.mc.bungee;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.han.bot.BotCon;
import org.han.bot.JDAIN;
import org.han.common.PermKeys;
import org.han.disbun.ModuleLoader;
import org.han.bot.ComLink;
import org.han.link.Channels;
import org.han.link.GetDetails;
import org.han.link.LangLoader;
import org.han.link.LinkPrep;
import org.han.link.LinkUp;
import org.han.link.LogDebug;
import org.han.link.TextMsg;
import org.han.mc.bungee.com.*;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import net.dv8tion.jda.api.entities.Role;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.Plugin;

public class BPlugin extends Plugin {
	public static BPlugin Self;
	public static BConfig Config;
	public static ModuleLoader loader;
	public static LangLoader Langsys;
	Coms coms;

	class Coms {
		Command Linkcom = new Linkcom();
		Command UnLinkcom = new UnLinkcom();
		Command Reloadcom = new Reloadcom();
		Command InfoCom = new InfoCom();
	}

	public static void main(String[] w) {
		FileObj.Init();
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
		Debug.out("Loading Config...");
		Config = new BConfig();
		Config.UpdateCheck();
		Debug.Debugmode = Config.DebugMode();
		Langsys = LangLoader.Load();
		loader = new ModuleLoader();
		loader.UpdateCheck();
		ComLink.UpdateCheck();

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
				Self.RegComs();

				ClassicPerms();

				if (BotCon.isRunning()) {
					loader.LOAD();
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
	public void onLoad() {
		Self = this;
		getProxy().registerChannel(Channels.Main);
		getProxy().getPluginManager().registerListener(this, new Events());

		FileObj.Init(getProxy().getPluginsFolder(), getDataFolder().getName());
		Debug.boot(new LogDebug(getLogger()), true);
		Debug.out("Plugin File : " + FileObj.ClassPath);

	}

	@Override
	public void onEnable() {
		reloadConfig();
	}

	@Override
	public void onDisable() {
		BotCon.Stop();
		loader.DELOAD();
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

	public void RegComs() {
		if (coms != null) {
			getProxy().getPluginManager().unregisterCommand(coms.InfoCom);
			getProxy().getPluginManager().unregisterCommand(coms.UnLinkcom);
			getProxy().getPluginManager().unregisterCommand(coms.Reloadcom);
			getProxy().getPluginManager().unregisterCommand(coms.InfoCom);
		}
		coms = new Coms();
		getProxy().getPluginManager().registerCommand(this, coms.InfoCom);
		getProxy().getPluginManager().registerCommand(this, coms.UnLinkcom);
		getProxy().getPluginManager().registerCommand(this, coms.Reloadcom);
		getProxy().getPluginManager().registerCommand(this, coms.InfoCom);
	}

	public static void ClassicPerms() {
		SrvrunAsync(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				List<Role> Roles = GetDetails.getGuild().getRoles();
				String[] ListOfPerms = new String[1 + Roles.size()];

				if (Config.isPermcheck()) {
					Debug.rep("Saving classic perms prefab");
					ListOfPerms[0] = "#List of available permissions";
					for (int i = 0; i < Roles.size(); i++) {
						ListOfPerms[i + 1] = PermKeys.KEY + Roles.get(i).getName();
					}
					try {
						FileObj.writeNF(ListOfPerms, FileObj.Fetch("", "Permissions", "list"), "List object");
						Debug.rep("Done saving classic perms prefab");
					} catch (IOException e) {
						Debug.Trace(e);
					}

				}
			}
		});
	}
}
