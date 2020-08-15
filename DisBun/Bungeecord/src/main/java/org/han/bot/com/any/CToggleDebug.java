package org.han.bot.com.any;

import org.han.bot.Print;
import org.han.bot.ComObj;
import org.han.bot.Msg;
import org.han.mc.bungee.BPlugin;
import org.han.xlib.Debug;

public class CToggleDebug extends ComObj {

	public CToggleDebug() {
		super(Place.Any, Permlv.BotOwn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "switch_debug";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		boolean F =BPlugin.Config.DebugMode();
		if (BPlugin.Config.ToggleDebugMode(!F)) {
			Print.Out(m, F?"Toggled debug off":"Toggled debug on");
			Debug.Debugmode = !F;
			return;
		}
		Print.Err(m, "Failed to toggle debug state");
		
		
		
		
		
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Toggle Debug on or off";
	}

}
