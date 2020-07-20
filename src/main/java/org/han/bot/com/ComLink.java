package org.han.bot.com;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.han.bot.Print;
import org.han.bot.com.any.*;
import org.han.bot.com.srv.*;
import org.han.xlib.Debug;

public class ComLink {
	private static Map<String, ComObj> Coms = new ConcurrentHashMap<String, ComObj>();

	static {
		RegCom(new CToggleDebug());
		RegCom(new CDeTrust());
		RegCom(new CHelp());
		RegCom(new CTrust());

		RegCom(new CAccDelink());
		RegCom(new CDellGroup());
		RegCom(new CLink());
		RegCom(new CList());
		RegCom(new CMakeGroup());
		RegCom(new CReload());
		RegCom(new CSetup());
		RegCom(new CSyncGroup());
		RegCom(new CUnlink());

	}

	public static final Map<String, ComObj> GetComMap() {
		return Coms;
	}

	public static void RegCom(ComObj Com) {
		String com = Com.getCom().trim().toLowerCase();
		if (Coms.containsKey(com)) {
			int i = 1;
			do {
				com = Com.getCom().trim().toLowerCase() + "_" + i;
				i++;
			} while (Coms.containsKey(com));
			Debug.err("Unique names should be used->Renaming command \"" + Com.getCom() + "\" to \"" + com + "\"");
		}
		Coms.put(com, Com);
	}

	public static void DoCom(Msg m) {
		if (!m.isCom())
			return;
		if (Coms.containsKey(m.getCom())) {
			ComObj CC = Coms.get(m.getCom());
			if (CC.place.chk(m)) {
				if (CC.permlv.chk(m))
					Coms.get(m.getCom()).Run(m);
				else
					Print.Err(m, "insufficient Permissions");
			}
		}
	}

}
