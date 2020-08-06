package org.han.mc.bungee;

import java.util.UUID;

import org.han.bot.BotCon;
import org.han.bot.JDAIN;
import org.han.link.Channels;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.module.AdvancementHandler;
import org.han.mc.bungee.module.DeathMessageHandler;
import org.han.mc.bungee.module.PlaceHolderapiServerSide;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;
import org.han.mc.spigot.module.Advancement;
import org.han.mc.spigot.module.DeathMessage;
import org.han.xlib.Debug;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.event.ProxyReloadEvent;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class Events implements Listener {
	TextMsg T = new JDAIN();

	@EventHandler
	public void onServerConnectEvent(ServerConnectEvent event) {
		ProxiedPlayer player = event.getPlayer();
		if ((LinkUp.isforcedlink(event.getTarget().getName())) && LinkUp.GetDiscordID(player.getUniqueId()) == null) {
			if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
				String send = ChatColor.BOLD + "" + ChatColor.GREEN //
						+ "This server requires you to link your discord account to join it"
						+ BPlugin.RelinkText(player);

				player.disconnect(new ComponentBuilder(send).create());

			} else {
				String send = ChatColor.BOLD + "" + ChatColor.GREEN //
						+ "This server requires you to link your discord account to join it";
				player.sendMessage(new ComponentBuilder(send).create());

			}
			event.setCancelled(true);
		}
		if (!event.isCancelled()) {
			if (event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
				JoinMessages.networkjoin(event.getTarget(), player);
			} else if (!event.isCancelled()) {
				JoinMessages.serverjoin(event.getTarget(), player);
			}
			PermCalc.self.UpdatePerms(player);
		}

	}

	@EventHandler
	public void onProxyReloadEvent(ProxyReloadEvent event) {
		BPlugin.reloadConfig();
	}

	@EventHandler
	public void onServerSwitchEvent(ServerSwitchEvent event) {
		if (event.getFrom() != null) {
			JoinMessages.serverleave(event.getFrom(), event.getPlayer());
			// JoinMessages.serverjoin(event.getPlayer().getReconnectServer(),
			// event.getPlayer());
		}

		// JoinMessages.serverleave(event.getTarget(), event.getPlayer());
	}

	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
		if (event != null && event.getPlayer() != null && event.getPlayer().getServer() != null
				&& event.getPlayer().getServer().getInfo() != null)
			JoinMessages.networkleave(event.getPlayer().getServer().getInfo(), event.getPlayer());
	}

	@EventHandler
	public void onChatEvent(ChatEvent event) {
		if (!BotCon.isRunning())
			return;
		if (!event.isCommand()) {
			BPlugin.SrvrunAsync(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ProxiedPlayer player = (ProxiedPlayer) event.getSender();
					String Message = event.getMessage();
					String plr = player.getName();
					String servername = player.getServer().getInfo().getName();
					UUID UUID = player.getUniqueId();
					T.sendfromMC(servername, plr, UUID, Message);
				}
			});
		}
	}

	@EventHandler
	public void on(PluginMessageEvent event) {
		if (!event.getTag().equalsIgnoreCase(Channels.Main)) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		String subChannel = in.readUTF();
		if (subChannel.equalsIgnoreCase(Channels.Advancement)) {
			if (event.getReceiver() instanceof ProxiedPlayer) {
				AdvancementHandler.DisplayAdvancement(Advancement.decode(in.readUTF()),
						((ProxiedPlayer) event.getReceiver()).getServer().getInfo());

			} else if (event.getReceiver() instanceof Server) {
				AdvancementHandler.DisplayAdvancement(Advancement.decode(in.readUTF()),
						((Server) event.getReceiver()).getInfo());
			}
		}else if(subChannel.equalsIgnoreCase(Channels.DeathMessage)) {
			if (event.getReceiver() instanceof ProxiedPlayer) {
				DeathMessageHandler.DisplayDeathMessage(DeathMessage.decode(in.readUTF()),
						((ProxiedPlayer) event.getReceiver()).getServer().getInfo());

			} else if (event.getReceiver() instanceof Server) {
				DeathMessageHandler.DisplayDeathMessage(DeathMessage.decode(in.readUTF()),
						((Server) event.getReceiver()).getInfo());
			}
			
			
		} else if (subChannel.equalsIgnoreCase(Channels.configPing))

		{
			PlaceHolderapiServerSide.SYNC();
			// the receiver is a ProxiedPlayer when a server talks to the proxy
			if (event.getReceiver() instanceof ProxiedPlayer) {
				ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
				ConfigUpdate(receiver.getServer().getInfo());

			} else if (event.getReceiver() instanceof Server) {
				ConfigUpdate(((Server) event.getReceiver()).getInfo());
			}
		}
	}

	static public void ConfigUpdate(ServerInfo Srv) {
		if (BPlugin.Config.isGlobal()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(Channels.configsend);
			out.writeUTF(BPlugin.Config.GChatFilter());
			Srv.sendData(Channels.Main, out.toByteArray());
			Debug.rep("Sync call sent to " + Srv.getName());
		}
	}

}
