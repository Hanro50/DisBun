package org.han.mc.spigot;
import java.io.IOException;

import org.han.xlib.AbsConfig;
import org.han.xlib.FileObj;

public class SConfig extends AbsConfig {
	static final String Override = "Override";
	static final String ChatFilter = "ChatFilter";
	static final String DebugMode = "DebugMode";
	public SConfig() {
		super(FileObj.Fetch("", "Config", "txt"));
		Register("Override global settings\n", Override, "false");
	

		Register("Chat filter settings (This will autosync if Override is set to false)" //
				+ "\n %DisplayName% : for the display name of the person" //
				+ "\n %RealName% : for the real name of the person" //
				+ "\n %DiscordRole% : The top Discord role" //
				+ "\n %DiscordColour% : Top Discord role colour (1.16+ recommended)" //
				+ "\n &x for the colour codes", //
				ChatFilter, "&9[&%DiscordRole%&9] %DiscordColour% %DisplayName% &f: ");
		
		Register("Enable Debug Messages\n", DebugMode, "false");
		
	}

	public boolean Override() {
		return get(Override).equalsIgnoreCase("true");
	}
	
	public String getFilter() {
		return get(ChatFilter);
	}

	public void SyncFilter(String newfilter) {
		if (!Override()) {
			edit(ChatFilter, newfilter);
			try {
				Save();
			} catch (IOException e) {

			}
		}
	}
	
	public boolean DebugMode() {
		return boolcheck(DebugMode);
	}
	// public void

}
