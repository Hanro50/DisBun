package org.han.bot.com.srv;

import org.han.bot.Print;
import org.han.bot.com.ComObj;
import org.han.bot.com.Msg;
import org.han.mc.bungee.BPlugin;

public class CReload extends ComObj{

	public CReload() {
		super(Place.Server, Permlv.Trusted);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "reload";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		BPlugin.reloadConfig();
		Print.Suc(m); 
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "reloads the configuration files of the bot";
	}

}
