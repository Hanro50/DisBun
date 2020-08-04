package org.han.bot.com.any;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.han.bot.Print;
import org.han.bot.com.ComLink;
import org.han.bot.com.ComObj;
import org.han.bot.com.Msg;

public class CHelp extends ComObj {

	public CHelp() {
		super(Place.Any, Permlv.all);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "help";
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "The help command";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub

		boolean all = m.getText().contains("all");
		List<String> keys = new ArrayList<String>();
		Comparator<String> cmp = (String.CASE_INSENSITIVE_ORDER).reversed().reversed();
		keys.addAll(ComLink.GetComMap().keySet());
		keys.sort(cmp);
		String res = " %U Help commands: **```";
		for (String key : keys) {
			if (ComLink.iSEnable(key)) {
				ComObj comobj = ComLink.GetComMap().get(key);

				if ((all || (comobj.permlv.chk(m) && (comobj.visible.chk(m)))) && comobj.place.chk(m)) {
					res = res + String.format("%-12s", key) + " : " + comobj.Help() + "\n";

				}
			}
		}
		res = res + "```**";
		Print.Out(m, res);
	}

}
