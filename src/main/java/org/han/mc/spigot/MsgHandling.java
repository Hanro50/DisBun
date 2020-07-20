package org.han.mc.spigot;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import com.google.common.io.ByteArrayDataInput;
import com.google.gson.annotations.Expose;

import net.md_5.bungee.api.ChatColor;

public class MsgHandling {
	public static void Message(ByteArrayDataInput Message, Server Srv) {
		MsgStruct F = new MsgStruct(Message);
		Playerdata T = Playerdata.Load(F.PlayerID, Srv, F.Discordname);

		String Output = SPlugin.Config.getFilter();
		if (Output.contains("%DisplayName%"))
			Output = Output.replaceAll("%DisplayName%", T.DisplayName);
		if (Output.contains("%RealName%"))
			Output = Output.replaceAll("%RealName%", T.Name);
		if (Output.contains("%DiscordRole%")) {
			Output = Output.replaceAll("%DiscordRole%", F.role.RoleName);
		}

		if (Output.contains("%DiscordColour%") || Output.contains("%DiscordColor%")) {
			try {
			Output = Output.replaceAll("%DiscordColour%", ChatColor.of(F.role.Color).toString());
			Output = Output.replaceAll("%DiscordColor%", ChatColor.of(F.role.Color).toString());
			} catch (StringIndexOutOfBoundsException e) {
				Debug.Trace(e);
				Debug.out(F.role.Color + "");
				
			}
		}
		Output = Output.replaceAll("\\&", "§");
		Output = Output + F.Message;
		Debug.rep("filter: " + SPlugin.Config.getFilter());
		Debug.rep("Sending: " + Output);

		// .replaceAll("%DisplayName%", T.DisplayName)
		/// .replaceAll("%RealName%", T.Name).replaceAll("%DiscordName%",
		/// F.Discordname).replaceAll("\\&", "§") + F.Message;
		Srv.broadcastMessage(Output);

		// if (F.PlayerID != null) {
		// Player p = Srv.getOfflinePlayer(F.PlayerID).getPlayer();
		// if (p == null) {
		// File pdata =FileObj.Fetch("DB", F.PlayerID.toString(), "txt");
		// if (pdata.exists()) {

		// }

		// }

		// Srv.broadcastMessage(SPlugin.Config.getFilter().replaceAll("%DisplayName%",
		// Srv.getOfflinePlayer(F.PlayerID).getPlayer(). ) );

		// }

		// Srv.broadcastMessage("§9[§1Discord§9] " + plr + ": " + msg);

	}

	public static void ConfigSync(ByteArrayDataInput Message) {
		if (!SPlugin.Config.Override()) {
			SPlugin.Config.SyncFilter(Message.readUTF());
		}
	}

	static class Playerdata {
		@Expose
		String DisplayName;
		@Expose
		String Name;
		@Expose
		UUID uuid;

		Playerdata(String DisplayName, String Name, UUID uuid) {
			this.DisplayName = DisplayName;
			this.Name = Name;
			this.uuid = uuid;
		}

		void Save() {
			String save = FileObj.tojson(this);
			FileObj.FileChk("db/");
			try {
				FileObj.writeNF(new String[] { save }, FileObj.Fetch("db/", uuid.toString(), "plr"), "player data");
			} catch (IOException e) {
				Debug.err("failed to save data");
				Debug.Trace(e);
			}
		}

		public static Playerdata Load(UUID uuid, Server Srv, String DiscordName) {
			String Name = null;
			if (uuid != null) {
				Player p = Srv.getOfflinePlayer(uuid).getPlayer();
				if (p != null) {
					Playerdata T = new Playerdata(p.getDisplayName(), p.getName(), p.getUniqueId());
					T.Save();
					return T;
				}
				File F = FileObj.Fetch("db/", uuid.toString(), "plr");
				if (F.exists()) {
					try {
						Playerdata T = FileObj.fromjson(FileObj.read(F, ""), Playerdata.class);
						return T;
					} catch (IOException e) {
						Debug.err("Failed to read player data");
						F.delete();
					}
				}
				Name = Srv.getOfflinePlayer(uuid).getName();
			}
			if (Name == null) {
				Name = DiscordName;
			}
			return new Playerdata(Name, Name, uuid);

			// Playerdata T = new Playerdata(Srv.getOfflinePlayer(uuid).get

		}

	}

}
