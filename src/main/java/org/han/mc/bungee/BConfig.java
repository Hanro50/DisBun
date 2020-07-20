package org.han.mc.bungee;

import java.io.IOException;

import org.han.xlib.AbsConfig;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

public class BConfig extends AbsConfig {

	static final String BotToken = "Bot Token";
	static final String ServID = "DS_Serv";
	static final String DB = "Debug";
	static final String LP = "Luckperms";
	static final String LPUT = "LPUpdateTimer";

	static final String GChatFilterType = "ChatFilterType";
	static final String GChatFilter = "ChatFilter";

	static final String ForceLink = "ForceLink";
	static final String Permcheck = "Permcheck";
	static final String ShowInviteLink = "ShowInvite";

	static final String DarwinAward = "DarwinAward";

	static final String Utopicglobal = "TopicType";
	static final String ServerTopic = "ServerTopicFormat";
	static final String ServerTopicUpdate = "ServerTopicUpdate";
	
	static final String Networkjoin = "Networkjoin";
	static final String Serverjoin = "Serverjoin";

//§ "§9[§1Discord§9] §f: "
	public BConfig() {
		super(FileObj.Fetch("", "config", "txt"));

		Register("The Bot Token for the bot", BotToken, "Insert token here");
		Register("The main Linked Discord Server\nThis can be added automatically within discord", ServID, "ServerID");
		Register("Debug Mode", DB, "false");
		Register("Luckperm:\nLuck perms module", LP, "true");
		Register("Luckperm:\nUpdate sync in minutes", LPUT, "5");

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
		Register("Should a user be forced to link up thier account before connecting?", ForceLink, "false");

		Register("Should the perms be checked for the link and unlink minecraft commands?\n" //
				+ "usefull if you don't want to bother with perms", Permcheck, "false");

		Register("Should the bot generate an invite on bootup?\n" //
				+ "useful for security. If someone can see edit this config\n"//
				+ "then they already know the bot token and can generate a new one on will without much effort.",
				ShowInviteLink, "true");
		//
		Register("Toggles the Darwin award easter egg.\n I realise this joke isn't for everyone", DarwinAward, "true");

		Register("Should topic updates be ser globaly, locally via the server specific configs or disabled outright",
				Utopicglobal, "global");

		Register("Server topic format\n %ServName% for the server name\n %PlayerCount% for the player count",
				ServerTopic, "%PlayerCount% player(s) are currently playing on %ServName%");

		Register("Topic Update timer in minutes", ServerTopicUpdate, "11");
		
		Register("Show a network join", Networkjoin, "true"); 
		
		Register("Show a server join", Serverjoin, "true"); 

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

	public boolean LPEnabled() {
		return boolcheck(LP);
	}

	public int LPRefresh() {
		try {
			return Integer.valueOf(get(LPUT).trim());
		} catch (NumberFormatException e) {
			Debug.err("Config option isn't a valid value. Please select a integer value for option: \"" + LPUT
					+ "\" in this plugin's app config options");
			Debug.err("Returning default value of 5");
			edit(LPUT, "5");
			try {
				Save();
			} catch (IOException e1) {
			}
			return 5;
		}
	}

	public boolean isGlobal() {
		return get(GChatFilterType).trim().equalsIgnoreCase("Global");
	}

	public String GChatFilter() {
		return get(GChatFilter);
	}

	public boolean isForceLink() {
		return boolcheck(ForceLink);
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

	public boolean isTUDisabled() {
		return get(Utopicglobal).trim().equalsIgnoreCase("Disabled");
	}

	public boolean isTUGlobal() {
		return get(Utopicglobal).trim().equalsIgnoreCase("global");
	}

	public String gettopicformat() {
		return get(ServerTopic);
	}
	
	public int ServerTopicUpdate() {
		try {
			return Integer.valueOf(get(ServerTopicUpdate).trim());
		} catch (NumberFormatException e) {
			Debug.err("Config option isn't a valid value. Please select a integer value for option: \""
					+ ServerTopicUpdate + "\" in this plugin's app config options");
			Debug.err("Returning default value of 5");
			edit(ServerTopicUpdate, "11");
			try {
				Save();
			} catch (IOException e1) {
			}
			return 11;
		}
	}
	
	public boolean isNetworkjoin() {
		return boolcheck(Networkjoin);
	}
	
	public boolean isServerjoin() {
		return boolcheck(Serverjoin);
	}

}
