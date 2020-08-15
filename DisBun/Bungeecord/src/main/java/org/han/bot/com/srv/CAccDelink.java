package org.han.bot.com.srv;

import java.util.List;
import java.util.UUID;

import org.han.bot.Print;
import org.han.bot.ComObj;
import org.han.bot.Msg;
import org.han.link.LinkUp;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.com.UnLinkcom;
import org.han.mc.bungee.module.PlaceHolderapiServerSide;

import net.dv8tion.jda.api.entities.Member;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class CAccDelink extends ComObj {

	public CAccDelink() {
		super(Place.Server, Permlv.all);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "accdelink";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		List<Member> memlist = m.Message.getMentionedMembers();
		if (memlist.size() == 0) {
			UUID uuid = LinkUp.GetUUID(m.Sender.User.getId());
			if (uuid == null) {
				Print.Err(m, "account does not appear to be linked");
				return;
			}

			ProxiedPlayer player = BPlugin.getproxyserv().getPlayer(uuid);
			if (BPlugin.Config.isPermcheck() && player == null && Permlv.Trusted.chk(m) == false) {
				Print.Err(m, "Please do this check ingame as currently," //
						+ " we cannot check if you have the required permissions to do this action." //
						+ "Alternatively, log into the game first and then try again or become/ask an admin"); //
				return;
			}

			if (player != null) {
				if (!player.hasPermission(UnLinkcom.permission)) {
					Print.Err(m, "You don't have permission ingame or here to do that.");
					return;
				}
				if (UnLinkcom.DarwinDelink(player)) {
					Print.Out(m, "You just got kicked...by yourself...");
					PlaceHolderapiServerSide.SYNC();
					return;
				}
				Print.Suc(m);
				PlaceHolderapiServerSide.SYNC();
				return;
			}

			LinkUp.Remove(uuid);
			Print.Suc(m);
			PlaceHolderapiServerSide.SYNC();
			return;

		}

		if (Permlv.Trusted.chk(m) == false) {
			Print.Err(m, "You lack the required permissions to delink someone else");
			return;
		}

		if (memlist.size() > 1) {
			Print.Out(m, "Only the first mentioned mention will be delinked");
		}

		UUID uuid = LinkUp.GetUUID(memlist.get(0).getId());
		if (uuid == null) {
			Print.Err(m, "account does not appear to be linked");
			return;
		}
		ProxiedPlayer player = BPlugin.getproxyserv().getPlayer(uuid);
		LinkUp.Remove(uuid);
		if (player != null) {
			if (LinkUp.isforcedlink(player.getServer().getInfo().getName())) {
				player.disconnect(new ComponentBuilder(
						"Your discord and minecraft accounts where delinked forcefully by :" + m.Sender.User.getName())
								.color(ChatColor.GREEN).create());
			}
		}
		Print.Suc(m);
		PlaceHolderapiServerSide.SYNC();
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Delinks the account of the person sending the message or any pinged individ";
	}

}
