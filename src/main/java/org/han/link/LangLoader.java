package org.han.link;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.han.mc.bungee.BPlugin;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import com.google.common.io.ByteStreams;
import com.google.gson.annotations.Expose;

public class LangLoader {
	@Expose
	Map<String, String> LangMap;

	public static LangLoader Load() {
		if (!BPlugin.Config.UseExtLang()) {
			try {
				InputStream stream = BPlugin.getproxyserv().getClass()
						.getResourceAsStream("/mojang-translations/en_us.json");
				String content;
				content = new String(ByteStreams.toByteArray(stream));
				return FileObj.fromjson("{\"LangMap\":" + content.replaceAll("\n", "") + "}", LangLoader.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Debug.Trace(e);
			}

		}

		try {
			return FileObj.fromjson(
					"{\"LangMap\":" + FileObj.read(FileObj.Fetch("", BPlugin.Config.ExtLang(), "json"), "") + "}",
					LangLoader.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Debug.Trace(e);
		}
		Debug.err("Could not load language support");
		return new LangLoader();
	}

	public String GetAdvancement_title(String AdvID) {
		return LangMap.getOrDefault("advancements." + AdvID.replace("/", ".") + ".title",
				AdvID.replaceAll("_", " ").split("/")[AdvID.replaceAll("_", " ").split("/").length - 1]);
	}

	public String GetAdvancement_Disc(String AdvID) {
		return LangMap.getOrDefault("advancements." + AdvID.replace("/", ".") + ".description", "Translation error");
	}

	public String StringJoinText() {
		return LangMap.getOrDefault("multiplayer.player.joined", "%s joined the game");

	}

	public String StringLeftText() {
		return LangMap.getOrDefault("multiplayer.player.left", "%s left the game");
	}

	public String StringJoinNetworkText() {
		return LangMap.getOrDefault("bungeecord.player.joined", "%s joined the network");

	}

	public String StringLeftNetworkText() {
		return LangMap.getOrDefault("bungeecord.player.left", "%s left the network");
	}

	public String StringadvancementtaskText() {
		return LangMap.getOrDefault("chat.type.advancement.task", "%s has made the advancement %s");
	}

	public String JDAHelpText(String com, String DefaultHelpText) {
		return LangMap.getOrDefault("disbun.com."+com+".helptext", DefaultHelpText);
	}
	public String JDAcomText(String com) {
		return LangMap.getOrDefault("disbun.com."+com+".com", com);
	}
	//command.failed
	public String JDAFail() {
		return LangMap.getOrDefault("command.failed", "An unexpected error occurred trying to execute that command");
	}
	public String JDASuccess() {
		return LangMap.getOrDefault("disbun.success", "Success");
	}
	 //"options.title"
	public String JDAServStatus() {
		return LangMap.getOrDefault("multiplayer.status.request_handled", "Status request has been handled");
	}
	
	public String JDAhelp() {
		return LangMap.getOrDefault("options.title", "Options");
	}
}
//"chat.type.advancement.task": "%s has made the advancement %s"
