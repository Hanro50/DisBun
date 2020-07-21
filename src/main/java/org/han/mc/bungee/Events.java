package org.han.mc.bungee;

import java.util.UUID;

import org.han.bot.BotCon;
import org.han.bot.JDAIN;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.module.JoinMessages;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;
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
				event.setCancelled(true);
			}
		}
		if (!event.isCancelled() && event.getReason() == ServerConnectEvent.Reason.JOIN_PROXY) {
			JoinMessages.networkjoin(event.getTarget(), player);
			PermCalc.UpdatePerms(player);

		}
		else if (!event.isCancelled() ){
			JoinMessages.serverjoin(event.getTarget(), player);
			PermCalc.UpdatePerms(player);
		}
		// BPlugin.SrvrunAsync(new Runnable() {
		// @Override
		// public void run() {
		// ProxiedPlayer player = (ProxiedPlayer) event;

		// }
		// }
		// });

		// LinkUp.GetDiscordID()

	}

	@EventHandler 
	public void onProxyReloadEvent(ProxyReloadEvent event) {
		BPlugin.reloadConfig();
	}
	
	@EventHandler
	public void onServerSwitchEvent(ServerSwitchEvent event) {
		if (event.getFrom() != null) {
			JoinMessages.serverleave(event.getFrom(), event.getPlayer());
			//JoinMessages.serverjoin(event.getPlayer().getReconnectServer(), event.getPlayer());
		}

		// JoinMessages.serverleave(event.getTarget(), event.getPlayer());
	}

	@EventHandler
	public void onPlayerDisconnectEvent(PlayerDisconnectEvent event) {
		JoinMessages.networkleave(event.getPlayer().getServer().getInfo(), event.getPlayer());
	}

	// @EventHandler
	// public void onPostLoginEvent(PostLoginEvent event) {
	// JoinMessages.networkjoin(event.getPlayer().getServer().getInfo(),
	// event.getPlayer());
	// }
	// PlayerDisconnectEvent

	/*
	 * @EventHandler public void onLoginEvent(LoginEvent event) { PendingConnection
	 * player = event.getConnection(); if ((BPlugin.Config.isForceLink()) &&
	 * LinkUp.GetDiscordID(player.getUniqueId()) == null) { String send =
	 * ChatColor.BOLD + "" + ChatColor.GREEN // +
	 * "\n This server requires you to link your discord account to join it" +
	 * ChatColor.RED + "\nDM " + BotCon.getJDA().getSelfUser().getName() +
	 * " the following message<" + ChatColor.WHITE +
	 * LinkPrep.Primer(event.getConnection().getUniqueId()) + ChatColor.RED +
	 * "> to link your account" + ChatColor.RESET; player.disconnect(new
	 * ComponentBuilder(send).create()); }
	 * 
	 * //BPlugin.SrvrunAsync(new Runnable() { //@Override //public void run() {
	 * //ProxiedPlayer player = (ProxiedPlayer) event;
	 * 
	 * //} // } // });
	 * 
	 * // LinkUp.GetDiscordID() }
	 * 
	 * /*
	 * 
	 * @EventHandler public void onClientConnectEvent(ClientConnectEvent event) {
	 * Boot.SrvrunAsync(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * 
	 * } }); }
	 * 
	 * @EventHandler public void onPermissionCheckEvent(PermissionCheckEvent event)
	 * { Boot.SrvrunAsync(new Runnable() {
	 * 
	 * @Override public void run() { // TODO Auto-generated method stub
	 * 
	 * } }); }
	 */
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

			// (servername, plr, UUID, Message);
		}

	}

	@EventHandler
	public void on(PluginMessageEvent event) {
		if (!event.getTag().equalsIgnoreCase(TextMsg.Channel)) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
		String subChannel = in.readUTF();
		if (subChannel.equalsIgnoreCase(TextMsg.SubconfigPing)) {
			// the receiver is a ProxiedPlayer when a server talks to the proxy
			if (event.getReceiver() instanceof ProxiedPlayer) {
				ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
				ConfigUpdate(receiver.getServer().getInfo());
			}
			// {
			// ProxiedPlayer receiver = (ProxiedPlayer) event.getReceiver();
			// do things
			// }
			// the receiver is a server when the proxy talks to a server
			else if (event.getReceiver() instanceof Server) {
				ConfigUpdate(((Server) event.getReceiver()).getInfo());

				// do things
			}
		}
	}

	static public void ConfigUpdate(ServerInfo Srv) {
		if (BPlugin.Config.isGlobal()) {
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF(TextMsg.Subconfig);
			out.writeUTF(BPlugin.Config.GChatFilter());
			Srv.sendData(TextMsg.Channel, out.toByteArray());
			Debug.rep("Sync call sent to " + Srv.getName());
		}
	}

}
