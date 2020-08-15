package org.han.bot.com.any;

import org.han.bot.ComLink;
import org.han.bot.ComObj;
import org.han.bot.Msg;
import org.han.bot.Print;
import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.plugin.PluginDescription;

public class CInfo extends ComObj {
	final String ServInfo = "ShowServInfo";
	final String BotInfo = "ShowBotInfo";
	ComLink Handler;

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
						+ "\n\tAuthor:" + desc.getAuthor() + "\n\tLink:https://github.com/Hanro50/DisBun/"
						+ (Handler.ChkBool(this, ServInfo)
								? "\n\nServer information:\n\tName:" + BPlugin.getproxyserv().getName()
										+ "\n\tVersion:(" + BPlugin.getproxyserv().getVersion() + ")" + "\n\tOnline:"
										+ BPlugin.getproxyserv().getOnlineCount()
								: "")
						+ (Handler.ChkBool(this, ServInfo)
								? "\n\nBot information:\n\tBot Owner:" + Msg.AppInfo.getOwner().getName()
								: "")
						+ "```");
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Shows some miscellaneous information about this bot";
	}

	public void Adcon(ComLink Handler) {
		Handler.regSettings(this, "Should server information be shown?", ServInfo, "true");
		Handler.regSettings(this, "Should bot information be shown?", BotInfo, "true");

		this.Handler = Handler;
	}
}
