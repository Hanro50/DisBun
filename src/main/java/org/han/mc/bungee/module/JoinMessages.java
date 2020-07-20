package org.han.mc.bungee.module;

import java.awt.Color;
import java.util.UUID;

import org.han.bot.BotCon;
import org.han.link.GetDetails;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class JoinMessages {
	private static void Print(Color color, String Message, ProxiedPlayer player, String ServName) {
		final UUID playerID = player.getUniqueId();
		final String playerName = player.getName();

		BPlugin.SrvrunAsync(new Runnable() {
			@Override
			public void run() {
				String ID = LinkUp.GetDiscordID(playerID);
				Member M = null;
				MessageEmbed E;
				// TODO Auto-generated method stub
				if (ID != null) {
					M = GetDetails.getGuild().retrieveMemberById(ID, true).complete();
				}

				if (M == null) {
					E = new EmbedBuilder()
							.setAuthor(playerName + Message, null, "https://crafatar.com/avatars/" + playerID)
							.setColor(color).build();

				} else {
					E = new EmbedBuilder().setAuthor(M.getEffectiveName() + Message, null, M.getUser().getAvatarUrl())
							.setColor(color).build();
				}

				TextMsg.GetChn(ServName).forEach(f -> {
					BotCon.getJDA().getTextChannelById(f).sendMessage(E).queue();
				});
			}

		});

		// CHL.sendMessage(E).queue();
	}

	public static void networkjoin(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isNetworkjoin()) {
			serverjoin(serverInfo, player);
			return;
		}
		Print(Color.green, " has joined the network", player, serverInfo.getName());

	}

	public static void serverjoin(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isServerjoin())
			return;
		Print(Color.green, " has joined " + serverInfo.getName(), player, serverInfo.getName());
	}

	public static void networkleave(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isNetworkjoin()) {
			serverleave(serverInfo, player);
			return;
		}
		Print(Color.red, " has left the network", player, serverInfo.getName());
	}

	public static void serverleave(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isServerjoin())
			return;
		Print(Color.red, " has left " + serverInfo.getName(), player, serverInfo.getName());
	}

}
