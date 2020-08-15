package org.han.spigot;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.han.disbun.DisBunTimerModule;

import com.google.gson.annotations.Expose;

public class Placeholderdata {
	public static boolean enabled = false;

	public static class userObj {
		@Expose
		public String NickName;
		@Expose
		public String NickNameClr;
		@Expose
		public String TopRole;

		userObj(String NickName, String NickNameClr, String TopRole) {

			this.NickName = NickName;
			this.NickNameClr = NickNameClr;
			this.TopRole = TopRole;
		}
	}

	@Expose
	public Map<UUID, userObj> internaldata = new HashMap<UUID, userObj>();

	public void Add(DisBunTimerModule mod, UUID UserID, String NickName, String NickNameClr, String TopRole) {
		if (mod.enabled) {
			userObj UserObj = new userObj(NickName, NickNameClr, TopRole);
			internaldata.remove(UserID);
			internaldata.put(UserID, UserObj);
		}
	}

}
