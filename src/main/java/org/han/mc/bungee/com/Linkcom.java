package org.han.mc.bungee.com;

import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Linkcom extends Command {

	public static final String permission = "DisBun.link";
	public Linkcom() {
		super("link", permission, new String[] { "DisBun:link", "DB:Link", "DiscordLink" });
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		if (BPlugin.Config.isPermcheck() && !hasPermission(sender)) {
			sender.sendMessage(
					new ComponentBuilder("You do not appear to have the required permissions to execute the command")
							.color(ChatColor.RED).create());
			return;
		}
		
		
		try {
			ProxiedPlayer player = (ProxiedPlayer) sender;
			String send = BPlugin.RelinkText(player, "").trim();

			sender.sendMessage(new ComponentBuilder(send).create());
		} catch (ClassCastException e) {
			sender.sendMessage(
					new ComponentBuilder("You do not appear to be a player. This command is meant solely for players")
							.color(ChatColor.RED).create());
		}

		// new ComponentBuilder(ChatColor.BOLD + "" + ChatColor.RED + "DM the bot the
		// following message:<"
		// + ChatColor.WHITE + LinkPrep.Primer(player.getUniqueId()) + ChatColor.RED +
		// ">").create());
	}
}
