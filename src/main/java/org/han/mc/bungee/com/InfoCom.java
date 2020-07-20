package org.han.mc.bungee.com;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class InfoCom extends Command  {

	public InfoCom() {
		super("DBInfo", "DisBun.info", new String[] {"dbinfo","DisBun:info", "DB:info"});
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub
		if (!hasPermission(sender) && (sender instanceof ProxiedPlayer)) {
			
			//new ComponentBuilder("You do not appear to have the required permissions to execute the command")
			//.color(ChatColor.RED).create();
			
			
			
			sender.sendMessage(
					new ComponentBuilder("You do not appear to have the required permissions to execute the command")
							.color(ChatColor.RED).create());
			return;
		}
		TextComponent text = new TextComponent(ChatColor.GREEN+"DisBun by Hanro50, click this to learn more");
		text.setClickEvent( new ClickEvent( ClickEvent.Action.OPEN_URL, "https://github.com/Hanro50/DisBun/" ) );
		sender.sendMessage(text);
	}

}
