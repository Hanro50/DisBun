package org.han.bot.com.srv;

import java.util.Map;

import org.han.bot.Print;
import org.han.bot.ComObj;
import org.han.bot.Msg;
import org.han.link.TextMsg;
import org.han.mc.bungee.BPlugin;

import net.md_5.bungee.api.config.ServerInfo;

public class CUnlink extends ComObj{

	public CUnlink() {
		super(Place.Server, Permlv.Trusted);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "unlink";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		Map<String, ServerInfo> INFO = BPlugin.getservinfo();
		if (INFO.containsKey(m.getText().trim())) {
			TextMsg.remLink(m.Channel.getIdLong(), m.getText().trim());
			Print.Suc(m);
		} else if (m.getText().toLowerCase().equals("all")) {
			INFO.keySet().forEach(T->{
				TextMsg.remLink(m.Channel.getIdLong(), T);
				
			});
			Print.Suc(m);
		} else {
			Print.Out(m, "Not a valid server");
		}
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "unlink the chat channel this is ran in";
	}

}
