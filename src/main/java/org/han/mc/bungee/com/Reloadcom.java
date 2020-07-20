package org.han.mc.bungee.com;

import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class Reloadcom extends Command  {

	public Reloadcom() {
		super("DBreload", "DisBun.reload", new String[] {"dbreload","DisBun:reload", "DB:reload"});
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		if (!hasPermission(sender) && (sender instanceof ProxiedPlayer)) {
			sender.sendMessage(
					new ComponentBuilder("You do not appear to have the required permissions to execute the command")
							.color(ChatColor.RED).create());
			return;
		}
	
		BPlugin.reloadConfig();
		sender.sendMessage(
				new ComponentBuilder("Reloading config...")
						.color(ChatColor.RED).create());
	}

}
