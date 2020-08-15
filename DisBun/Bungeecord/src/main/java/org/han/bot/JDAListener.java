package org.han.bot;

import java.awt.Color;
import java.io.IOException;
import java.util.UUID;

import javax.annotation.Nonnull;

import org.han.common.RoleObj;
import org.han.link.LinkPrep;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.BungeeIN;
import org.han.mc.bungee.module.PlaceHolderapiServerSide;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;
import org.han.xlib.Debug;

import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.ExceptionEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleAddEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRoleRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.role.RoleDeleteEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateColorEvent;
import net.dv8tion.jda.api.events.role.update.RoleUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.md_5.bungee.api.ChatColor;

public class JDAListener extends ListenerAdapter {
	TextMsg T = new BungeeIN();

	@Override
	public void onPrivateMessageReceived(@Nonnull PrivateMessageReceivedEvent event) {
		Msg m = new Msg(event);
		if (m.Sender.User.isBot())
			return;
		ComLink.DoCom(m);
		if (!m.isCom()) {
			if (LinkPrep.Excuter(m)) {
				Print.Suc(m);
				PlaceHolderapiServerSide.SYNC();
			} else
				Print.Err(m, "Not a valid link ID");
		}
	}

	@Override
	public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent event) {
		MsgSend(event);
	}

	@Override
	public void onGuildMessageUpdate(@Nonnull GuildMessageUpdateEvent event) {
		MsgSend(event);
	}

	@Override
	public void onException(@Nonnull ExceptionEvent event) {
		if (!event.isLogged())
			Debug.Trace(event.getCause());

	}

	// Role events
	@Override
	public void onRoleCreate(@Nonnull RoleCreateEvent event) {
		BPlugin.ClassicPerms();
	}

	@Override
	public void onRoleDelete(@Nonnull RoleDeleteEvent event) {
		PlaceHolderapiServerSide.SYNC();
		BPlugin.ClassicPerms();
		try {
			PermCalc.self.DelGroup(event.getRole());
		} catch (IOException e) {
		}
	}

	// Role Update Events
	@Override
	public void onRoleUpdateColor(@Nonnull RoleUpdateColorEvent event) {
		PlaceHolderapiServerSide.SYNC();
		BPlugin.ClassicPerms();
	}

	@Override
	public void onRoleUpdateName(@Nonnull RoleUpdateNameEvent event) {
		PlaceHolderapiServerSide.SYNC();
		BPlugin.ClassicPerms();
	}

	@Override
	public void onGuildMemberRoleAdd(@Nonnull GuildMemberRoleAddEvent event) {
		PlaceHolderapiServerSide.SYNC();
		UUID F = LinkUp.GetUUID(event.getUser().getId());
		if (F != null) {
			PermCalc.RoleUpdate(F);
		}

	}

	@Override
	public void onGuildMemberRoleRemove(@Nonnull GuildMemberRoleRemoveEvent event) {
		PlaceHolderapiServerSide.SYNC();
		UUID F = LinkUp.GetUUID(event.getUser().getId());
		if (F != null) {
			PermCalc.RoleUpdate(F);
		}
	}

	public void MsgSend(GenericGuildMessageEvent event) {
		BPlugin.SrvrunAsync(new Runnable() {
			@Override
			public void run() {
				Msg m = Msg.GMsg(event);
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
				String Booster = m.Sender.User.getId().equals("147677951683461120") ? "§6" : "";
				if (m.Sender.Member.getTimeBoosted() != null) {
					Booster = BPlugin.Config.Boosterchatcolour().replaceAll("&", "§");
				}

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

					role = new RoleObj(m.Sender.Member.getRoles().get(0).getName(), ChatColor.of(color).toString());
				} else {
					role = new RoleObj("", ChatColor.WHITE.toString());
				}

				T.sendfromJDA(m.Channel.getIdLong(), m.Sender.Member.getEffectiveName(), m.Sender.User.getId(),
						Booster + m.Message.getContentStripped() + Attch, role);
			}
		});
	}
}
