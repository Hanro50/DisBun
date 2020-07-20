package org.han.link;

import org.han.bot.BotCon;
import org.han.mc.bungee.BPlugin;
import org.han.xlib.Debug;
import net.dv8tion.jda.api.entities.Guild;

public class GetDetails {
	static Guild Temp;
	public static Guild getGuild() throws IllegalStateException {
		if (Temp != null && Temp.getId().equals(BPlugin.Config.GetServID())) return Temp;
		try {
			Temp = BotCon.getJDA().getGuildById(BPlugin.Config.GetServID());
			if (Temp == null) {
				throw new IllegalStateException("Serv Value not enitialize properly");
			}
			return Temp;
		} catch (NumberFormatException F) {
			Debug.wrn("Serv Value not enitialize properly");
			throw new IllegalStateException("Serv Value not enitialize properly");
		}
	}
/*
	private static void INIT() {
		File F = FileObj.Fetch("", "ServerID", "txt");
		if (F.exists()) {
			try {
				ServID = FileObj.read(F)[2].split(":")[1].trim();
				return;
			} catch (ArrayIndexOutOfBoundsException e) {
				Debug.out("File format exception. rewriting it");
			} catch (FileNotFoundException e) {
				Debug.Trace(e);
			} catch (IOException e) {
				Debug.Trace(e);
			}
		}
		Write("server ID here");
	}
*/
	static public boolean Write(String value) {
		return BPlugin.Config.SetServID(value);
		
		
		
		//File F = FileObj.Fetch("", "ServerID", "txt");
		///try {
		//	FileObj.writeNF(new String[] { "//Server ID. This can be linked in discord by running the setup command",
		//			"//Don't edit the file structure of this file", "ServID:" + value }, F, "Serv settings");
		//} catch (IOException e) {
		//	// TODO Auto-generated catch block
		//	Debug.Trace(e);
		//}
	}

}
