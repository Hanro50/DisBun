package org.han.link;

import java.util.UUID;

import org.han.bot.com.Msg;
import org.han.xlib.types.DiMap;

public class LinkPrep {
	private static DiMap<String, UUID> Linker = new DiMap<String, UUID>();

	public static synchronized String Primer(UUID PlayerID) {
		int ID = 0;
		
		if (Linker.haskey1(PlayerID)) {
			return Linker.Getkey1(PlayerID);
		}
		
		do {
			ID = (int) (Math.random() * 90000D + 10000D);
		} while (Linker.haskey2(String.valueOf(ID)) || ID < 10000);
		String ret = String.valueOf(ID);
		Linker.add(ret, PlayerID);
		return ret;
	}

	public static synchronized boolean Excuter(Msg m) {
		if (Linker.haskey2(m.Message.getContentRaw())) {
			LinkUp.LinkAcc(m.Sender.User.getId(), Linker.Getkey2(m.Message.getContentRaw()));
			Linker.removeKey1(m.Message.getContentRaw());
			return true;
		}
		return false;
	}
	
}
