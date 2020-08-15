package org.han.bot.com.srv;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.han.bot.Print;
import org.han.bot.ComLink;
import org.han.bot.ComObj;
import org.han.bot.Msg;
import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.config.ServerInfo;

public class CList extends ComObj {
 static final String Statusheader = "header";
 ComLink Handler;
	public CList() {
		super(Place.Server, Permlv.all);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "status";
	}
	
	@Override
	public void Adcon(ComLink Handler) {
		Handler.regSettings(this, "The header text for the list command  (%U Will be replaced with a user mention)", Statusheader, "List of servers %U ");
		this.Handler = Handler;
	}
	
	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		Map<String, ServerInfo> INFO = BPlugin.getservinfo();
		List<String> keys = new ArrayList<String>();
		Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
		keys.addAll(INFO.keySet());
		keys.sort(cmp);
		String res = Handler.ChkStr(this, Statusheader)+": **```";
		int i = 0;
		for (String key : keys) {
			ServerInfo SERVINFO = INFO.get(key);
			if (!SERVINFO.isRestricted()) {
				res = res + String.format("%-12s", key) + " : " + SERVINFO.getPlayers().size() +" Players online" + "\n";
				i +=SERVINFO.getPlayers().size();
			}
		}
		res = res + String.format("%-12s", "global") + " : " + i +" Players online" + "\n";
		
		res = res + "```**";
		Print.Out(m,res);
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "list available servers";
	}

}
