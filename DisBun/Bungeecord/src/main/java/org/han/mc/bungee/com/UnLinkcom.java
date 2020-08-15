package org.han.mc.bungee.com;

import java.util.List;

import org.han.bot.BotCon;
import org.han.bot.Msg;
import org.han.bot.Print;
import org.han.common.PermKeys;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.module.PlaceHolderapiServerSide;
//import org.han.mc.bungee.BPlugin;
import org.han.xlib.Debug;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class UnLinkcom extends Command {
	public static final String permission = "DisBun.unlink";

	public UnLinkcom() {
		super("unlink", permission, new String[] { "DisBun:unlink", PermKeys.KEY+ BPlugin.Config.Unlinkcom(), "DiscordUnlink" });
		// TODO Auto-generated constructor stub
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		// TODO Auto-generated method stub

		try {
			DarwinDelink((ProxiedPlayer) sender);
			PlaceHolderapiServerSide.SYNC();

		} catch (ClassCastException e) {
			sender.sendMessage(
					new ComponentBuilder("You do not appear to be a player. This command is meant solely for players")
							.color(ChatColor.RED).create());
		}
	}

	public static boolean DarwinDelink(ProxiedPlayer player) {
		LinkUp.Remove(player.getUniqueId());
		player.sendMessage(new ComponentBuilder("You should no longer be linked").color(ChatColor.RED).create());
		if (LinkUp.isforcedlink(player.getServer().getInfo().getName())) {
			Debug.out(player.getName() + (BPlugin.Config.isDarwin() ? " Achievement get: [Darwin award]"
					: " Kicked themselves by delinking their minecraft and Discord accounts"));
			player.disconnect(new ComponentBuilder(
					ChatColor.GREEN + "...I don't think you thought this thru..." + BPlugin.RelinkText(player))
							.create());
			if (BPlugin.Config.isDarwin()) {
				List<Long> T = TextMsg.GetChn(player.getServer().getInfo().getName());
				String output = player.getDisplayName() + " has made the advancement %F[Darwin Award]%F";
				T.forEach(L -> {
					Print.Out(BotCon.getJDA().getTextChannelById(L), Msg.AppInfo.getOwner(),
							output.replaceAll("%F", "**"));
				});
				player.getServer().getInfo().getPlayers().forEach(L -> {
					L.sendMessage(
							new ComponentBuilder(ChatColor.WHITE + output.replace("%F", ChatColor.GREEN.toString())
									.replace("%F", ChatColor.WHITE.toString())).color(ChatColor.BOLD).create());
				});
			}
			
			return true;
		} else {
			return false;
		}
	}

}
