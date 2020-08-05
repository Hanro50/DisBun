package org.han.mc.bungee.module;

import java.awt.Color;
import java.util.List;

import org.han.link.GetDetails;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.ModuleLoader;
import org.han.mc.spigot.module.Advancement;
import org.han.xlib.Debug;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.md_5.bungee.api.config.ServerInfo;

public class AdvancementHandler extends DisBunModule {
	static AdvancementHandler self;

	@Override
	public String HelpText() {
		// TODO Auto-generated method stub
		return "This takes in achievements and displays them";
	}

	@Override
	public String Name() {
		// TODO Auto-generated method stub
		return "Advancements";
	}

	public static void DisplayAdvancement(Advancement base, ServerInfo serv) {
		if (self != null)
			self.Display(base, serv);
	}

	private void Display(Advancement base, ServerInfo serv) {
		if (enabled) {
			List<Long> chnidlist = TextMsg.GetChn(serv.getName());
			String ICON = null;
			String PlayerName = null;

			String L = LinkUp.GetDiscordID(base.PlayerID);
			if (L != null) {
				Member f = GetDetails.getGuild().retrieveMemberById(L).complete();
				if (f != null) {
					ICON = f.getUser().getAvatarUrl();
					PlayerName = f.getEffectiveName();
				}
			}
			if (ICON == null) {
				ICON = "https://crafatar.com/avatars/" + base.PlayerID.toString();
			}

			if (PlayerName == null) {
				PlayerName = BPlugin.getproxyserv().getPlayer(base.PlayerID).getDisplayName();
			}

			String Type = "";
			if (base.AchievementID.split("/").length >= 1) {
				Type = "(" + base.AchievementID.split("/")[0] + ")";
			}
			String Title = BPlugin.Langsys.GetAdvancement_title(base.AchievementID);
			String Disc = BPlugin.Langsys.GetAdvancement_Disc(base.AchievementID);
			Color Clr = new Color(128, 128, 128);
			if (Type.equals("(recipes)")) {
				Debug.err(
						"Recipy advancements aren't being blocked by the spigot portion of this plugin again...please report");
				return;
			} else if (Type.equals("(story)"))
				Clr = Color.green;
			else if (Type.equals("(nether)"))
				Clr = Color.red;
			else if (Type.equals("(end)"))
				Clr = new Color(139, 0, 139);
			else if (Type.equals("(husbandry)"))
				Clr = Color.yellow;
			else if (Type.equals("(adventure)"))
				Clr = Color.orange;

			for (long f : chnidlist) {
				try {
					TextChannel CHL = GetDetails.getGuild().getTextChannelById(f);
					if (CHL != null) {
						try {
							MessageEmbed E = new EmbedBuilder().setAuthor(PlayerName, null, ICON)
									.setTitle(String.format(BPlugin.Langsys.StringadvancementtaskText(), "",
											"[" + Title + "]")) //
									.setColor(Clr).setDescription(Disc).setFooter(Type).build();
							CHL.sendMessage(E).queue();
						} catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
							Debug.Trace(e);
							Debug.err("correcting error");
							TextMsg.remChannel(f);
						}
					}
				} catch (NullPointerException e) {

				}
			}

		}
	}//

	@Override
	public void load(ModuleLoader Loader) {
		// TODO Auto-generated method stub
		enabled = true;
		self = this;
	}

	@Override
	public void deload() {
		// TODO Auto-generated method stub
		enabled = false;
	}

	@Override
	public void AdCon(ModuleLoader Loader) {
		// TODO Auto-generated method stub

	}

}
