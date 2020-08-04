package org.han.bot.com.any;

import org.han.bot.Print;
import org.han.bot.com.ComObj;
import org.han.bot.com.Msg;
import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.plugin.PluginDescription;

public class CInfo extends ComObj {

	public CInfo() {
		super(Place.Any, Permlv.all);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "info";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		PluginDescription desc = BPlugin.Self.getDescription();
		Print.Out(m,
				" %U ```Plugin information:" + "\n\tName:" + desc.getName() + "\n\tVersion:" + desc.getVersion()
						+ "\n\tAuthor:" + desc.getAuthor()+"\n\tLink:https://github.com/Hanro50/DisBun/" + "\n\nServer information:\n\tName:"
						+ BPlugin.getproxyserv().getName() + "\n\tVersion:(" + BPlugin.getproxyserv().getVersion()
						+ ")\n\tOnline:" + BPlugin.getproxyserv().getOnlineCount()
						+ "\n\nBot information:\n\tBot Owner:" + Msg.AppInfo.getOwner().getName() + "```");
	}
//```Plugin information:" +"\nName:" +desc.getName() +"\nVersion:" +desc.getVersion() +  "\nAuthor:" + desc.getAuthor() +"\n\n Bot information:\nBot Owner:"

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Shows some miscellaneous information about this bot";
	}
}
