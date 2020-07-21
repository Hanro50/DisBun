package org.han.mc.spigot.module;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.han.mc.bungee.BPlugin;

import com.google.gson.annotations.Expose;

public class Placeholderdata {
	public static boolean enabled = false;
	static class userObj {
		@Expose
		String NickName;
		@Expose
		String NickNameClr;
		@Expose
		String TopRole;
		userObj(String NickName, String NickNameClr, String TopRole) {

			this.NickName = NickName;
			this.NickNameClr = NickNameClr;
			this.TopRole = TopRole;
		}
	}

	@Expose
	Map<UUID, userObj> internaldata = new HashMap<UUID, userObj>();

	public void Add(UUID UserID, String NickName, String NickNameClr, String TopRole) {
		if (BPlugin.Config != null && BPlugin.Config.isPlaceholders()) {
			userObj UserObj = new userObj(NickName, NickNameClr,TopRole);
			internaldata.remove(UserID);
			internaldata.put(UserID, UserObj);
		}
	}

	
}
