package org.han.bot.com.any;

import java.util.List;

import org.han.bot.ComObj;
import org.han.bot.Msg;
import org.han.bot.Operator;
import org.han.bot.Print;

import net.dv8tion.jda.api.entities.User;

public class CDeTrust  extends ComObj{

	public CDeTrust() {
		super(Place.Any, Permlv.BotOwn);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "detrust";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		List<User> T = m.Message.getMentionedUsers();
		if (T.size() <1) {
			Print.Err(m, "Mention atleast one person");
			return;
		}
		
		if (T.size() !=1) {
			Print.Out(m, "Only the first mention will removed from the trusted list");
		}
		
		Operator.rem(m, T.get(0));
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Remove a user from the trusted list";
	}

}
