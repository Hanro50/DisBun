package org.han.bot.com.srv;

import org.han.bot.Print;
import org.han.bot.com.ComObj;
import org.han.bot.com.Msg;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;

public class CSyncGroup extends ComObj {
	static String lastreturn = "```no last log since last reboot```";
	public CSyncGroup() {
		super(Place.Any, Permlv.Trusted, Visible.LuckPerm);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "resync";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		if (PermCalc.endabled == false) {
			Print.Err(m, "Luckperm module not loaded");
		}
		else if (m.getText().toLowerCase().equals("now")) {
			lastreturn =   PermCalc.ReSyncGroup();
			Print.Out(m,"Run this command again with \"return\" to get output log");
		}
		else if (m.getText().toLowerCase().equals("return")) {
			Print.Out(m, " %U Console output:"+lastreturn);
		}
		else {
			Print.Err(m, "Please run this command with either the \"now\" or \"return\" parameters");
		}
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "sync Luckperms with server roles";
	}

}
