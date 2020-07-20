package org.han.mc.bungee;

import java.util.Set;

import org.han.xlib.AbsConfig;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;



public class BServerConfig extends AbsConfig {
	static final String ForceSyncServkey = "ForceLink";
	static final String ServerTopic = "ServerTopicFormat";
	public BServerConfig(Set<String> Input) {
		super(FileObj.Fetch("", "servConfig", "txt"));
		
		Input.forEach(ServName->{
			Register("Force link for "+ServName+" specifically?\n" //
					+ "This assumes globally you're not forced to link accounts"//
					, ServName+"."+ForceSyncServkey, "false");
			Register("Server topic format\n %ServName% for the server name\n %PlayerCount% for the player count",
					ServName+"."+ServerTopic, "%PlayerCount% player(s) are currently playing on %ServName%");
		});
		
		Debug.out("Done with server specific config");
	}
	
	public boolean isforcedlink(String ServName) {
		return boolcheck(ServName+"."+ForceSyncServkey);
	}
	
	public String ServerTopic(String ServName) {
		return ServName+"."+ServerTopic;
	}

	

}
