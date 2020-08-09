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
	final static String Lang = "lang/";

	public static LangLoader Load() {
		if (!BPlugin.Config.UseExtLang()) {
			if (!FileObj.Fetchfile(Lang).exists()) {
				try {
					Debug.out("Copying internal language files into config files");
					FileObj.FileChk(Lang);
					InputStream stream = BPlugin.Self.getResourceAsStream(Lang + "1filelist.txt");

					String content = new String(ByteStreams.toByteArray(stream));
					stream.close();
					for (String file : content.split("\n")) {
						InputStream INPUT = BPlugin.Self.getResourceAsStream(Lang+file+".json");
						String[] lines = new String(ByteStreams.toByteArray(INPUT)).split("\n");
						FileObj.write(lines, FileObj.Fetch(Lang, file, "json"), "language files");  
						INPUT.close();
					}
					
					
				} catch (IOException e) {
				}

				// File f = new
				// File(BPlugin.Self.getClass().getClassLoader().getResource("lang/en_us.json").getFile());
				// Debug.out(f + "");
				// File f = new
				// File(BPlugin.Self.getClass().getClassLoader().getResource(Lang).getFile());

				/// for (File out : f.listFiles()) {
				/// File in = FileObj.Fetch(Lang, out.getName(), "json");
				// try {
				// FileObj.write(FileObj.read(f), in, "language file");
				// } catch (IOException e) {
				// }
				// }

			}

			try {
				InputStream stream = BPlugin.getproxyserv().getClass()
						.getResourceAsStream("/mojang-translations/en_us.json");
				String content;
				content = new String(ByteStreams.toByteArray(stream));
				stream.close();
				return FileObj.fromjson("{\"LangMap\":" + content.replaceAll("\n", "") + "}", LangLoader.class);
			} catch (IOException | NullPointerException e) {
				try {
					// TODO Auto-generated catch block
					Debug.err("Wait this isn't bungee, loading internal version of language file?");
					InputStream stream = BPlugin.Self.getResourceAsStream("lang/en_us.json");
					String content = new String(ByteStreams.toByteArray(stream));
					stream.close();
					return FileObj.fromjson("{\"LangMap\":" + content.replaceAll("\n", "") + "}", LangLoader.class);
				} catch (IOException | NullPointerException e1) {
					// TODO Auto-generated catch block
					Debug.err("INTERNAL LANGUAGE CLASS FAILURE");
					Debug.err("Things are about to get messy");
					Debug.err("Please provide an external language file to fix this error");
					Debug.Trace(e);
					Debug.Trace(e1);

				}

			}

		}

		try {
			return FileObj.fromjson(
					"{\"LangMap\":" + FileObj.read(FileObj.Fetch(Lang, BPlugin.Config.ExtLang(), "json"), "") + "}",
					LangLoader.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Debug.Trace(e);
		}
		Debug.err("Could not load language support");
		return new LangLoader();
	}// "advancements.toast.task"

	public String GetAdvancement_title(String AdvID) {
		return LangMap.getOrDefault("advancements." + AdvID.replace("/", ".") + ".title",
				AdvID.replaceAll("_", " ").split("/")[AdvID.replaceAll("_", " ").split("/").length - 1]);
	}

	public String GetAdvancement_Disc(String AdvID) {
		return LangMap.getOrDefault("advancements." + AdvID.replace("/", ".") + ".description", "Translation error");
	}

	public String GetAdvancement_Root_title(String type) {
		return LangMap.getOrDefault("advancements." + type + ".root.title", "Translation error");
	}

	public String GetAdvancement_Root_Disc(String type) {
		return LangMap.getOrDefault("advancements." + type + ".root.description", "Translation error");
	}

	public String GetAdvancement_toast() {
		return LangMap.getOrDefault("advancements.toast.task", "Advancement Made!");
	}

	public String GetDM_title(String DMID, boolean Checkplr, boolean item) {
		if (Checkplr && item) {
			String F = LangMap.get(DMID.toLowerCase() + ".player.item");
			if (F != null)
				return F;
		}

		if (item) {
			String F = LangMap.get(DMID.toLowerCase() + ".item");
			if (F != null)
				return F;
		}

		if (Checkplr) {
			String F = LangMap.get(DMID.toLowerCase() + ".player");
			if (F != null)
				return F;
		}
		return LangMap.getOrDefault(DMID, DMID);
	}

	public String GetEntity(String type) {
		return LangMap.getOrDefault(type.toLowerCase(), type);
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

	public String EnchantmentName(String Enchantment) {
		return LangMap.getOrDefault("enchantment.minecraft." + Enchantment, Enchantment);
	}

	public String EnchantmentLevel(int Enchlv) {
		return LangMap.getOrDefault("enchantment.level." + Enchlv, "" + Enchlv);
	}

	public String GetGameOver() {
		return LangMap.getOrDefault("deathScreen.title.hardcore", "Game over!");
	}
	
	public String EXPString() {
		return LangMap.getOrDefault("commands.experience.query.levels", "%s has %s experience levels");
	}
}
//"chat.type.advancement.task": "%s has made the advancement %s"
