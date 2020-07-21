package org.han.bot;

import java.awt.Color;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.han.bot.com.ComLink;
import org.han.bot.com.Msg;
import org.han.link.LinkPrep;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.BungeeIN;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;
import org.han.xlib.Debug;

import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class JDAListener extends ListenerAdapter {
	TextMsg T = new BungeeIN();

	public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
		Msg m = new Msg(event);
		if (m.Sender.User.isBot())
			return;
		if (LinkPrep.Excuter(m))
			Print.Suc(m);
		else
			Print.Err(m, "Not a valid link ID");
	}

	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		Msg m = new Msg(event);
		ComLink.DoCom(m);
		if (m.Sender.User.isBot() || m.isCom())
			return;
		String Attch = "";

		if (m.Message.getAttachments().size() > 0) {
			Attch = "(";
			for (Attachment A : m.Message.getAttachments()) {
				Attch = Attch + A.getUrl() + " ";
			}
			Attch = Attch + ")";

		}
		String botwriter = event.getAuthor().getId().equals("147677951683461120") ? "§6" : "";
		RoleObj role = null;
		Color color = null;
		if (m.Sender.Member.getRoles().size() > 0) {
			for (Role Role : m.Sender.Member.getRoles()) {
				if (Role.getColor() != null) {
					color = Role.getColor();
					break;
				}
			}
			if (color == null) {
				color = Color.white;
			}

			role = new RoleObj(m.Sender.Member.getRoles().get(0).getName(),
					"#" + Integer.toHexString(color.getRGB()).substring(2));
		} else {
			role = new RoleObj("", "#" + Integer.toHexString(Color.white.getRGB()).substring(2));
		}

		T.sendfromJDA(m.Channel.getIdLong(), m.Sender.Member.getEffectiveName(), m.Sender.User.getId(),
				botwriter + m.Message.getContentStripped() + Attch, role);
	}

	public void onException(@Nonnull ExceptionEvent event) {
		if (!event.isLogged())
			Debug.Trace(event.getCause());

	}

	public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
		try {
			PermCalc.DelGroup(event.getRole());
		} catch (IOException e) {
		}
	}

	public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
		UUID F = LinkUp.GetUUID(event.getUser().getId());
		if (F != null) {
			PermCalc.RoleUpdate(F);
		}

	}

	public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
		UUID F = LinkUp.GetUUID(event.getUser().getId());
		if (F != null) {
			PermCalc.RoleUpdate(F);
		}
	}

}
