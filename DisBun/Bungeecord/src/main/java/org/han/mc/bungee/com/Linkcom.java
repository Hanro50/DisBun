package org.han.mc.bungee.com;

import org.han.common.PermKeys;
import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Linkcom extends Command {

	public static final String permission = "DisBun.link";
	public Linkcom() {
		super("link", permission, new String[] { "DisBun:link", PermKeys.KEY+ BPlugin.Config.Linkcom(), "DiscordLink" });
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		
		try {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			String send = BPlugin.RelinkText(player, "").trim();

			sender.sendMessage(new ComponentBuilder(send).create());
		} catch (ClassCastException e) {
			sender.sendMessage(
					new ComponentBuilder("You do not appear to be a player. This command is meant solely for players")
							.color(ChatColor.RED).create());
		}
	}
}
