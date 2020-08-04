package org.han.mc.bungee.module;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.han.link.GetDetails;
import org.han.link.LinkUp;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.ModuleLoader;
import org.han.mc.spigot.module.Placeholderdata;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PlaceHolderapiServerSide extends DisBunTimerModule {
	static Placeholderdata data;
	static PlaceHolderapiServerSide self;

	public void load(ModuleLoader Loader) {

		data = new Placeholderdata();
		Debug.rep("loading placeholder module");
		timer = new Timer();
		timer.schedule(new AutoUpdate(), 10000, Loader.UPDATETIMER(this) * 60000);
		enabled = true;
		self = this;
		SYNC();

		
	}

	public void deload() {
		data = null;
		if (timer != null)
			timer.cancel();
		timer = null;
		enabled = false;
	}

	public static void add(UUID UserID, String NickName, String NickNameClr, String toprole) {
		if (self.enabled) {
			if (!(UserID == null || NickName == null || NickNameClr == null || toprole == null))
				data.Add(self,UserID, NickName, NickNameClr, toprole);
		}
	}

	public static void SYNC() {
		if (!self.enabled) {
			return;
		}
		Debug.rep("starting to sync placeholder data");
		LinkUp.UUIDList().forEach(f -> {
			try {
				Member mem = GetDetails.getGuild().retrieveMemberById(LinkUp.GetDiscordID(f)).complete();
				if (mem != null) {
					Color color = null;
					if (mem.getRoles().size() > 0) {
						for (Role Role : mem.getRoles()) {
							if (Role.getColor() != null) {
								color = Role.getColor();
								break;
							}
						}
						if (color == null) {
							color = Color.white;
						}
						add(f, mem.getEffectiveName(), "#" + Integer.toHexString(color.getRGB()).substring(2),
								mem.getRoles().get(0).getName());
					} else {
						add(f, mem.getEffectiveName(), "#" + Integer.toHexString(Color.white.getRGB()).substring(2),
								mem.getRoles().get(0).getName());
					}
				}
			} catch (ErrorResponseException e) {
				Debug.err(e.getMessage());
				Debug.err("INVALID MEMBER DATA DETECTED");
				Debug.err("Purging invalid data from database");
				ProxiedPlayer player = BPlugin.getproxyserv().getPlayer(f);
				if (player != null && BPlugin.Config.isForceLink()) {
					player.disconnect(new ComponentBuilder(
							"Your account has been delinked as we cannot retrieve your member data from Discord")
									.color(ChatColor.RED).create());
				}
				LinkUp.Remove(f);
			}
		});

		// TODO Auto-generated method stub
		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(TextMsg.Subplaceholder);
		out.writeUTF(FileObj.tojson(data));
		Debug.rep("sending data");

		BPlugin.getservinfo().keySet().forEach(f -> {
			Debug.rep("to :" + f);
			BPlugin.getservinfo().get(f).sendData(TextMsg.Channel, out.toByteArray());
		});
		Debug.rep("Resetting data");
		data = new Placeholderdata();
	}

	static public class AutoUpdate extends TimerTask {

		@Override
		public void run() {
			SYNC();
		}

	}

	@Override
	public String HelpText() {
		// TODO Auto-generated method stub
		return "Should PlaceholderAPI be enabled?";
	}

	@Override
	public int DefaultTime() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String Name() {
		// TODO Auto-generated method stub
		return "PlaceHolderAPI";
	}

	@Override
	public void AdCon(ModuleLoader Loader) {
		// TODO Auto-generated method stub

	}

}
