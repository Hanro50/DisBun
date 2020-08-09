package org.han.mc.bungee;

import java.awt.Color;
import java.util.UUID;

import org.han.bot.BotCon;
import org.han.link.GetDetails;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.xlib.Debug;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
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
				try {
					if (ID != null) {
						M = GetDetails.getGuild().retrieveMemberById(ID, true).complete();
					}

					if (M == null) {
						E = new EmbedBuilder().setAuthor(String.format(Message, playerName), null,
								"https://crafatar.com/avatars/" + playerID).setColor(color).build();

					} else {
						E = new EmbedBuilder().setAuthor(String.format(Message, M.getEffectiveName()), null,
								M.getUser().getAvatarUrl()).setColor(color).build();
					}

					TextMsg.GetChn(ServName).forEach(f -> {
						TextChannel CHL = BotCon.getJDA().getTextChannelById(f);
						if (CHL != null) {
							CHL.sendMessage(E).queue();
						}
					});
				} catch (IllegalStateException e) {
					Debug.wrn("JDA is not running yet");
				}
			}
		});

		// CHL.sendMessage(E).queue();
	}

	public static void networkjoin(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isNetworkjoin()) {
			serverjoin(serverInfo, player);
			return;
		}
		Print(Color.green, BPlugin.Langsys.StringJoinNetworkText(), player, serverInfo.getName());

	}

	public static void serverjoin(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isServerjoin())
			return;
		Print(Color.green, serverInfo.getName() + ":" + BPlugin.Langsys.StringJoinText(), player, serverInfo.getName());
	}

	public static void networkleave(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isNetworkjoin()) {
			serverleave(serverInfo, player);
			return;
		}
		Print(Color.red, BPlugin.Langsys.StringLeftNetworkText(), player, serverInfo.getName());
	}

	public static void serverleave(ServerInfo serverInfo, ProxiedPlayer player) {
		if (!BPlugin.Config.isServerjoin())
			return;
		Print(Color.red, serverInfo.getName() + ":" + BPlugin.Langsys.StringLeftText(), player, serverInfo.getName());
	}

}
