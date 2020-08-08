package org.han.mc.bungee.module;

import java.awt.Color;
import java.util.List;

import org.han.link.GetDetails;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.ModuleLoader;
import org.han.mc.spigot.module.DeathMessage;
import org.han.xlib.Debug;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.utils.MarkdownSanitizer;
import net.md_5.bungee.api.config.ServerInfo;

public class DeathMessageHandler extends DisBunModule {
	static DeathMessageHandler self;
	static final String AttackerWeaponDetails = "Attacker.weapon.details";
	static final String VictemDetails = "Victem.details";

	@Override
	public String HelpText() {
		// TODO Auto-generated method stub
		return "Enables the death messages to be shown in Discord";
	}

	@Override
	public String Name() {
		// TODO Auto-generated method stub
		return "death_messages";
	}

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
		Loader.regSettings(this, "Should the weapon details be showed in the death message if applicable",
				AttackerWeaponDetails, "true");
		Loader.regSettings(this, "Should details about the victem be shown in the death message", VictemDetails,
				"true");
	}

	public static void DisplayDeathMessage(DeathMessage base, ServerInfo serv) {
		if (self != null)
			self.Display(base, serv);
	}

	private void Display(DeathMessage base, ServerInfo serv) {
		if (enabled) {
			String BaseStr = BPlugin.Langsys.GetDM_title(base.DeathMsg, (base.Attacker != null || base.MobName != null),
					(base.Weapon != null));
			if (LastHash == base.hashCode()) {
				Debug.err("Running the same message twice? This ain't right...");
				return;
			}
			LastHash = base.hashCode();
			String vic = LinkUp.GetDiscordID(base.Victem);
			String vicName = null;
			String AtkName = null;
			Member Vic = null;
			if (vic != null) {
				Vic = GetDetails.getGuild().retrieveMemberById(vic).complete();
				if (Vic != null) {
					vicName = Vic.getEffectiveName();
				}
			}
			if (vicName == null) {
				vicName = BPlugin.getproxyserv().getPlayer(base.Victem).getDisplayName();
			}
			if (base.Attacker != null) {
				String Atk = LinkUp.GetDiscordID(base.Attacker);

				if (Atk != null) {
					Member mem = GetDetails.getGuild().retrieveMemberById(Atk).complete();
					if (mem != null) {
						AtkName = mem.getEffectiveName();
					}
				}
				if (AtkName == null) {
					AtkName = BPlugin.getproxyserv().getPlayer(base.Attacker).getDisplayName();
				}
			} else if (base.MobName != null) {
				AtkName = BPlugin.Langsys.GetEntity(base.MobName);
			} else {
				AtkName = "Translation error";
			}
			List<Long> chnidlist = TextMsg.GetChn(serv.getName());
			String ICON = (Vic == null ? "https://crafatar.com/avatars/" + base.Victem.toString()
					: Vic.getUser().getAvatarUrl());
			Color Clr = new Color(0, 127, 255);

			String Weapon = base.Weapon == null ? "Translation error" : base.Weapon;
			vicName = MarkdownSanitizer.escape(vicName);
			BaseStr = MarkdownSanitizer.escape(BaseStr);
			AtkName = MarkdownSanitizer.escape(AtkName);
			Weapon = MarkdownSanitizer.escape(Weapon);
			// Description section;
			String Disc = "";

			if (Loader.ChkBool(self, VictemDetails)) {
				Disc = Disc + String.format("**[_%s_]** \n```", vicName);
				if (base.XP > 0) {
					Disc = Disc + String.format("\tEXP: %.2f", base.XP);
				}
				Disc = Disc + "\n\tPOS:" + String.format("(x:%.2f,y:%.2f,z:%.2f)", base.Deathpos.x(), base.Deathpos.y(),
						base.Deathpos.z());
				Disc = Disc + "```\n";
			}
			if (Loader.ChkBool(self, AttackerWeaponDetails)) {
				if (base.Enchantments != null) {
					Disc = Disc + "**[_" + Weapon + "_]** \n```";
					for (String Ench : base.Enchantments.keySet()) {
						Disc = Disc + "\t" + BPlugin.Langsys.EnchantmentName(Ench) + " "
								+ BPlugin.Langsys.EnchantmentLevel(base.Enchantments.get(Ench)) + "\n";
					}
					Disc = Disc + "```";
				}
			}

			for (long f : chnidlist) {
				try {
					TextChannel CHL = GetDetails.getGuild().getTextChannelById(f);
					if (CHL != null) {
						try {
							MessageEmbed E = new EmbedBuilder().setAuthor(BPlugin.Langsys.GetGameOver(), null, ICON)
									.setTitle(String.format(BaseStr, vicName, AtkName, "**[_" + Weapon + "_]**"))
									.setColor(Clr).setDescription(Disc).build();
							CHL.sendMessage(E).queue();
						} catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
							Debug.Trace(e);
							Debug.err("correcting error");
							TextMsg.remChannel(f);
						}
					}
				} catch (NullPointerException e) {
					Debug.Trace(e);
				}
			}

		}
	}

}
