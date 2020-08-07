package org.han.mc.spigot.module;

import java.util.UUID;

import org.han.xlib.FileObj;

import com.google.gson.annotations.Expose;

public class Advancement {
	@Expose public UUID PlayerID;
	@Expose public String AchievementID;
	@Expose public long Creationdate;
	public Advancement(UUID PlayerID,String AchievementID) {
		this.PlayerID = PlayerID;
		this.AchievementID = AchievementID;
		this.Creationdate = System.nanoTime();
	}
	public String encode() {
		return FileObj.tojson(this);
	}
	
	public static Advancement decode(String Encodedval) {
		return FileObj.fromjson(Encodedval, Advancement.class);
	}

}
