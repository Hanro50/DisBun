package org.han.link;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;
import org.han.xlib.types.DiMap;

import com.google.gson.annotations.Expose;

public class LinkUp {
	public static String GetDiscordID(UUID UUID) {
		return DATA.Load().LinkData.Getkey1(UUID);
	}

	public static List<UUID> UUIDList() {
		return DATA.Load().LinkData.getlistk2();
	}

	public static UUID GetUUID(String DiscordID) {
		return DATA.Load().LinkData.Getkey2(DiscordID);
	}

	public static void LinkAcc(String DiscordID, UUID UUID) {
		DATA T = DATA.Load();
		T.LinkData.add(DiscordID, UUID);
		T.Save();
		PermCalc.RoleUpdate(UUID);
	}

	public static void Remove(UUID UUID) {
		DATA T = DATA.Load();
		T.LinkData.removeKey2(UUID);
		T.Save();
	}

	static class DATA {
		@Expose
		DiMap<String, UUID> LinkData;

		public DATA() {
			LinkData = new DiMap<String, UUID>();
		}

		static DATA Self;

		static DATA Load() {
			if (Self != null)
				return Self;
			File F = FileObj.Fetch("", "PlayerData", "json");
			if (F.exists()) {
				try {
					DATA Temp = FileObj.fromjson(FileObj.read(F, ""), DATA.class);
					Self = Temp;
					return Self;
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Debug.Trace(e);
					Debug.out("Regenerating link file.");
				}
			}

			Self = new DATA();
			return Self;

		}

		void Save() {
			try {
				String self = FileObj.tojson(this);
				File F = FileObj.Fetch("", "PlayerData", "json");
				FileObj.writeNF(new String[] { self }, F, "PlayerData");
			} catch (IOException e) {
				Debug.Trace(e);
			}
		}
	}

	public static boolean isforcedlink(String ServName) {
		if (BPlugin.Config.isForceLink())
			return true;
		return BPlugin.Config.isForceLink(ServName);

	}

}
