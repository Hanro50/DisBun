package org.han.bot.com.srv;

import java.util.Map;

import org.han.bot.Print;
import org.han.bot.com.ComObj;
import org.han.bot.com.Msg;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.config.ServerInfo;

public class CLink extends ComObj {

	public CLink() {
		super(Place.Server, Permlv.Trusted);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "link";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		Map<String, ServerInfo> INFO = BPlugin.getservinfo();
		if (INFO.containsKey(m.getText().trim())) {
			TextMsg.SaveLink(m.Channel.getIdLong(), m.getText().trim());
			Print.Suc(m);
		} else if (m.getText().toLowerCase().equals("global")) {
			INFO.keySet().forEach(T -> {
				TextMsg.SaveLink(m.Channel.getIdLong(), T);
			});
			Print.Suc(m);
		} else {
			Print.Out(m, "Not a valid server");
		}

	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Link a chat channel";
	}

}
