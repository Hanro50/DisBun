package org.han.mc.bungee.com;

import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.PluginDescription;

public class InfoCom extends Command {

	public InfoCom() {
		super("DBInfo", null, new String[] { "dbinfo", "DisBun:info", "DB:info" });
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		PluginDescription desc = BPlugin.Self.getDescription();
		TextComponent header = new TextComponent(ChatColor.GREEN+"Plugin information:" + ChatColor.AQUA+//
				"\nName:" + desc.getName() + ChatColor.DARK_AQUA+//
				"\nVersion:" + desc.getVersion() + ChatColor.AQUA+//
				"\nAuthor:" + desc.getAuthor());

		TextComponent Link = new TextComponent(ChatColor.GREEN + "\nClick this to learn more");
		Link.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Hanro50/DisBun/"));

		sender.sendMessage(header, Link);
	}

}
