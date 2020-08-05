package org.han.mc.bungee;

import java.io.File;
import java.io.IOException;

import org.han.xlib.AbsConfig;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

public class BConfig extends AbsConfig {
	static final String Version = "Version";
	static final String BotToken = "Bot Token";
	static final String ServID = "DS_Serv";
	static final String DB = "Debug";
//	static final String LP = "Luckperms";
//	static final String LPUT = "LPUpdateTimer";

	static final String GChatFilterType = "ChatFilterType";
	static final String GChatFilter = "ChatFilter";

	static final String ForceLink = "ForceLink";
	static final String Permcheck = "Permcheck";
	static final String ShowInviteLink = "ShowInvite";

	static final String DarwinAward = "DarwinAward";

//	static final String Utopicglobal = "TopicType";
//	static final String ServerTopic = "ServerTopicFormat";
//	static final String ServerTopicUpdate = "ServerTopicUpdate";

	static final String Networkjoin = "Networkjoin";
	static final String Serverjoin = "Serverjoin";

	
	static final String UseExtLang = "UseExtLang";
	static final String ExtLang = "ExtLang";
//	static final String Placeholdersenabled = "enabled_Placeholders";
//	static final String placeholdersUpdate = "placeholdersUpdate";

//	static final String MethodChangerenabled = "MethodChangerenabled";
//	static final String MethodChangerUpdate = "MethodChangerUpdate";

//§ "§9[§1Discord§9] §f: "
	public BConfig() {
		super(FileObj.Fetch("", "config", "txt"));
		if (NEWCONFIG)
			Register("Version of this file\n Do not edit", Version, BPlugin.Self.getDescription().getVersion());
		else
			Register("Version of this file\n Do not edit", Version, "0.0.0");

		Register("The Bot Token for the bot", BotToken, "Insert token here");
		Register("The main linked Discord Server\nThis can be added automatically within discord", ServID, "ServerID");
		Register("Debug Mode", DB, "false");
		// Register("LuckPerms:\nLuckPerms module", LP, "true");
		// Register("LuckPerms:\nUpdate sync in minutes", LPUT, "5");

		Register("Chat Filter Type " //
				+ "\n \"Local\" : Use the settings on the servers themselves" //
				+ "\n \"Global\" : Use the settings in this config file", //
				GChatFilterType, "Global");

		Register("Global chat filter " //
				+ "\n %DisplayName% : for the display name of the person" //
				+ "\n %RealName% : for the real name of the person" //
				+ "\n %DiscordRole% : The top Discord role" //
				+ "\n %DiscordColour% : Top Discord role colour (1.16+ recommended)" //
				+ "\n &x for the colour codes", //

				GChatFilter, "&9[&1%DiscordRole%&9] %DiscordColour% %DisplayName% &f: ");
		
		Register("Should a user be forced to link up their account before connecting?", ForceLink, "false");
		BPlugin.getservinfo().keySet().forEach(ServName -> {
			Register("Force link for " + ServName + " specifically?\n" //
					+ "This assumes globally you're not forced to link accounts"//
			, ServName + "." + ForceLink, "false");
		});

		Register(
				"Should the plugin as a whole check the perms for the link and unlink minecraft commands when displaying kick messages?\n" //
				, Permcheck, "false");

		Register("Should the bot generate an invite on bootup?\n" //
				+ "useful for security. If someone can see edit this config\n"//
				+ "then they already know the bot token and can generate a new one on will without much effort.",
				ShowInviteLink, "true");
		//
		Register("Toggles the Darwin award easter egg.\n I realise this joke isn't for everyone", DarwinAward, "true");

		// Register("Should topic updates be set globally, locally via the server
		// specific configs or disabled outright",
		// Utopicglobal, "global");

		// Register("Server topic format\n %ServName% for the server name\n
		// %PlayerCount% for the player count",
		// ServerTopic, "%PlayerCount% player(s) are currently playing on %ServName%");

//		Register("Topic Update timer in minutes", ServerTopicUpdate, "11");

		Register("Show a network join", Networkjoin, "true");

		Register("Show a server join", Serverjoin, "true");

		// Register("Should PlaceholderAPI be enabled?", Placeholdersenabled, "true");

		// Register("How often should the client's data be refreshed?",
		// placeholdersUpdate, "5");

		// Register("If the method changer should be used", MethodChangerenabled,
		// "true");

		// Register("How often should the method the bot is showcasing be refreshed?",
		// MethodChangerUpdate, "5");
		
		Register("Should this plugin use an external lang file (if a bungee update breaks this part of the plugin. This is a potential fix)",
				UseExtLang, "false");
		Register( "external language file(put this file in the plugin config file)", ExtLang,
				"af.json");

	}
	public boolean UseExtLang() {
		return boolcheck(UseExtLang);
	}
	
	public String ExtLang() {
		return get(ExtLang);
	}
	

	public String GetToken() {
		return get(BotToken);
	}

	public String GetServID() {
		return get(ServID);
	}

	public boolean SetServID(String newID) {
		edit(ServID, newID);
		try {
			Save();
			return true;
		} catch (IOException e1) {
			Debug.out("Failed to save new server id to config");
			return false;
		}
	}

	public boolean DebugMode() {
		return boolcheck(DB);
	}

	public boolean ToggleDebugMode(boolean Switch) {
		edit(DB, Switch ? "true" : "false");
		try {
			Save();
			return true;
		} catch (IOException e1) {
			Debug.out("Failed to save Debug toggle to config");
			return false;
		}
	}

	/*
	 * @Deprecated public boolean LPEnabled() { return boolcheck(LP); }
	 * 
	 * @Deprecated public int LPRefresh() { try { return
	 * Integer.valueOf(get(LPUT).trim()); } catch (NumberFormatException e) { Debug.
	 * err("Config option isn't a valid value. Please select a integer value for option: \""
	 * + LPUT + "\" in this plugin's app config options");
	 * Debug.err("Returning default value of 5"); edit(LPUT, "5"); try { Save(); }
	 * catch (IOException e1) { } return 5; } }
	 */
	public boolean isGlobal() {
		return get(GChatFilterType).trim().equalsIgnoreCase("Global");
	}

	public String GChatFilter() {
		return get(GChatFilter);
	}

	public boolean isForceLink() {
		return boolcheck(ForceLink);
	}
	
	public boolean isForceLink(String ServName) {
		return boolcheck(ServName+"."+ForceLink);
	}

	public boolean isPermcheck() {
		return boolcheck(Permcheck);
	}

	public boolean isShowInvite() {
		return boolcheck(ShowInviteLink);
	}

	public boolean isDarwin() {
		return boolcheck(DarwinAward);
	}

	/*
	 * @Deprecated public boolean isTUDisabled() { return
	 * get(Utopicglobal).trim().equalsIgnoreCase("Disabled"); }
	 * 
	 * @Deprecated public boolean isTUGlobal() { return
	 * get(Utopicglobal).trim().equalsIgnoreCase("global"); }
	 * 
	 * @Deprecated public String gettopicformat() { return get(ServerTopic); }
	 * 
	 * @Deprecated public int ServerTopicUpdate() { try { return
	 * Integer.valueOf(get(ServerTopicUpdate).trim()); } catch
	 * (NumberFormatException e) { Debug.
	 * err("Config option isn't a valid value. Please select a integer value for option: \""
	 * + ServerTopicUpdate + "\" in this plugin's app config options");
	 * Debug.err("Returning default value of 5"); edit(ServerTopicUpdate, "11"); try
	 * { Save(); } catch (IOException e1) { } return 11; } }
	 */
	public boolean isNetworkjoin() {
		return boolcheck(Networkjoin);
	}

	public boolean isServerjoin() {
		return boolcheck(Serverjoin);
	}
	/*
	 * @Deprecated public boolean isPlaceholders() { return
	 * boolcheck(Placeholdersenabled); }
	 * 
	 * @Deprecated public int placeholdersUpdate() { try { return
	 * Integer.valueOf(get(placeholdersUpdate).trim()); } catch
	 * (NumberFormatException e) { Debug.
	 * err("Config option isn't a valid value. Please select a integer value for option: \""
	 * + placeholdersUpdate + "\" in this plugin's app config options");
	 * Debug.err("Returning default value of 5"); edit(placeholdersUpdate, "5"); try
	 * { Save(); } catch (IOException e1) { } return 5; } }
	 * 
	 * @Deprecated public boolean MethodChangerenabled() { return
	 * boolcheck(MethodChangerenabled); }
	 * 
	 * @Deprecated public int MethodChangerUpdate() { try { return
	 * Integer.valueOf(get(MethodChangerUpdate).trim()); } catch
	 * (NumberFormatException e) { Debug.
	 * err("Config option isn't a valid value. Please select a integer value for option: \""
	 * + MethodChangerUpdate + "\" in this plugin's app config options");
	 * Debug.err("Returning default value of 5"); edit(MethodChangerUpdate, "5");
	 * try { Save(); } catch (IOException e1) { } return 5; } }
	 * 
	 */

	// if (f.equalsIgnoreCase("null"))
	// return null;
	// return f;

	void UpdateCheck() {
		if (BPlugin.Self.getDescription().getVersion().equalsIgnoreCase(get(Version)))
			return;
		Debug.out("Updating confing file");
		Debug.out(get(Version) + " -> " + BPlugin.Self.getDescription().getVersion());
		File OLDFILE = FileObj.Fetch("", "oldconfig", "txt");
		OLDFILE.delete();
		File OLD = FileObj.Fetch("", "config", "txt");
		OLD.renameTo(OLDFILE);
		BConfig NEWCONFIG = new BConfig();
		edit(BConfig.Version, BPlugin.Self.getDescription().getVersion());
		UpdateConfig(this, NEWCONFIG);
		FileObj.Fetch("", "oldconfig", "txt").deleteOnExit();
	}

}
