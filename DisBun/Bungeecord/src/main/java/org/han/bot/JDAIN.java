package org.han.bot;

import java.util.List;
import java.util.UUID;

import org.han.common.RoleObj;
import org.han.link.TextMsg;
import org.han.xlib.Debug;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.Webhook;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.exceptions.InsufficientPermissionException;

public class JDAIN implements TextMsg {
	
	@Override
	public void send(long ChannelID, String ServName, String player, String DiscordID, UUID UUID, String Message, RoleObj roleobj ) {
		if (!BotCon.isRunning()) return;
		// TODO Auto-generated method stub
		if (ChannelID != 0L) {
			try {
				TextChannel CHL = BotCon.jda.getTextChannelById(ChannelID);
				if (CHL == null) {
					TextMsg.remChannel(ChannelID);
					Debug.err("Invalid channel (NULL return value), Updating config");
					return;
				}

				try {
					Webhook F;
					List<Webhook> W = CHL.retrieveWebhooks().complete();
					L1: {
						for (Webhook webhook : W) {
							if (webhook.getOwner().getUser().getIdLong() == BotCon.getJDA().getSelfUser().getIdLong()) {
								F = webhook;
								break L1;
							}
						}
						F = CHL.createWebhook("MCLINK").complete();
					}//L1;
					WebhookClient client = new WebhookClientBuilder(F.getUrl()).build();

					WebhookMessageBuilder builder = new WebhookMessageBuilder();

					builder.setContent(Message);
					builder.setUsername(player);
					User user = null;
					if (DiscordID != null) {
						try {
							user = BotCon.getJDA().retrieveUserById(DiscordID).complete();
						} catch (ErrorResponseException e) {
							Debug.err(e.getMessage());
						}
					} else {
						Debug.wrn("User (" + player + ") not linked");
					}
					if (user != null) {
						builder.setAvatarUrl(user.getAvatarUrl());
					} else {
						builder.setAvatarUrl("https://crafatar.com/avatars/" + UUID.toString());
					}
					WebhookMessage message = builder.build();
					client.send(message);
					client.close();

					return;
				} catch (InsufficientPermissionException e) {
					Debug.err(e.getMessage());
				} catch (NullPointerException e) {
					Debug.Trace(e);
				}

				MessageEmbed E = new EmbedBuilder()
						.setAuthor(player, null, "https://crafatar.com/avatars/" + UUID.toString())
						.setDescription(Message).setFooter(ServName).build();
				CHL.sendMessage(E).queue();

			} catch (java.lang.NumberFormatException e) {
				Debug.err("Invalid channelID (" + ChannelID + ")");
			}
		}
	}
}
