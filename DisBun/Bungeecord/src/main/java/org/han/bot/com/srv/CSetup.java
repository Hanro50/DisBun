package org.han.bot.com.srv;

import org.han.bot.Print;
import org.han.bot.ComObj;
import org.han.bot.Msg;
import org.han.link.GetDetails;

public class CSetup extends ComObj{

	public CSetup() {
		super(Place.Server, Permlv.Trusted);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "setup";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		if (GetDetails.Write(m.Guild.getId())) Print.Suc(m);
		else Print.Err(m, "Sync Failed");
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Links the bot to this Discord server. \n(Required for perms and nicknames to link)";
	}

}
