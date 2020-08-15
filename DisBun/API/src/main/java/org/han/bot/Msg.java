package org.han.bot;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.entities.MessageReaction.ReactionEmote;
import net.dv8tion.jda.api.events.message.guild.GenericGuildMessageEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.sharding.ShardManager;

public class Msg {

	static public final String Prefix = "!";
	static public ApplicationInfo AppInfo;
	static public ShardManager SH;
	static public JDA jda;

	public Sender Sender;
	public Guild Guild;
	public Message Message;
	public MessageChannel Channel;
	public boolean isServer;
	public ReactionEmote Emote;
	
	public static class Sender {
		public User User;
		public Member Member;

		public Sender(User User, Member Member) {
			this.User = User;
			this.Member = Member;
		}
	}

	public Msg(GuildMessageReceivedEvent event) {
		Sender = new Sender(event.getAuthor(), event.getMember());
		Guild = event.getGuild();
		Channel = event.getChannel();
		Message = event.getMessage();
		isServer = true;
		Emote = null;
	}

	public Msg(GuildMessageUpdateEvent event) {
		Sender = new Sender(event.getAuthor(), event.getMember());
		Guild = event.getGuild();
		Channel = event.getChannel();
		Message = event.getMessage();
		isServer = true;
		Emote = null;
	}

	public static Msg GMsg(GenericGuildMessageEvent event) {
		if (event instanceof GuildMessageReceivedEvent) {
			return new Msg((GuildMessageReceivedEvent) event);
		}
		if (event instanceof GuildMessageUpdateEvent) {
			return new Msg((GuildMessageUpdateEvent) event);
		}
		return null;
	}

	public Msg(PrivateMessageReceivedEvent event) {
		Sender = new Sender(event.getAuthor(), null);
		Guild = null;
		Channel = event.getChannel();
		Message = event.getMessage();
		isServer = false;
		Emote = null;

	}

	public Msg(GuildMessageReactionAddEvent event) {
		Sender = new Sender(event.getUser(), event.getMember());
		Guild = event.getGuild();
		Channel = event.getChannel();
		Message = Channel.retrieveMessageById(event.getMessageId()).complete();
		isServer = true;
		Emote = event.getReactionEmote();

	}

	public boolean isServer() {
		return isServer;
	}

	public boolean isAdmin() {
		if (isServer())
			return Sender.Member.hasPermission(Permission.ADMINISTRATOR);
		else
			return isBotOwner();
	}

	public boolean isBotOwner() {
		return isBotOwner(Sender.User);// Sender.User.getIdLong() == AppInfo.getOwner().getIdLong();
	}

	public static boolean isBotOwner(User user) {
		return user.getIdLong() == AppInfo.getOwner().getIdLong();
	}

	public boolean isReactCom() {
		return Emote != null;
	}

	public boolean isCom() {
		String MSG = Message.getContentRaw();
		if (MSG.length() <= Prefix.length())
			return false;
		return (MSG.subSequence(0, Prefix.length()).equals(Prefix));
	}

	public String getCom() {
		String MSG = Message.getContentRaw();
		if (MSG.length() <= Prefix.length())
			return "";
		return (MSG.substring(Prefix.length()).trim().split(" ", 2)[0]).toLowerCase();
	}

	public String getText() {
		String MSG = Message.getContentRaw();
		if (MSG.length() <= Prefix.length())
			return "";
		try {
			return (MSG.substring(Prefix.length()).trim().split(" ", 2)[1]).trim();
		} catch (ArrayIndexOutOfBoundsException e) {
			return "";
		}
	}

	public MessageChannel GetDM() {
		return Sender.User.openPrivateChannel().complete();
	}

	public static MessageChannel GetDM(User User) {
		if (User.getIdLong() == jda().getSelfUser().getIdLong())
			return AppInfo.getOwner().openPrivateChannel().complete();
		return User.openPrivateChannel().complete();
	}

	public static JDA jda() {
		if (SH != null) {
			return SH.getShardCache().asList().get(0);
		} else {
			return jda;
		}
	}
}
