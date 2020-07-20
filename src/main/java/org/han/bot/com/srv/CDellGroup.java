package org.han.bot.com.srv;

import java.io.IOException;
import java.util.List;

import org.han.bot.Print;
import org.han.bot.com.ComObj;
import org.han.bot.com.Msg;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;

import net.dv8tion.jda.api.entities.Role;

public class CDellGroup extends ComObj {

	public CDellGroup() {
		super(Place.Server, Permlv.Trusted, Visible.LuckPerm);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "delgroup";
	}

	@Override
	public void Run(Msg m) {
		// TODO Auto-generated method stub
		if (!PermCalc.endabled) {
			Print.Err(m, "LuckPerms component is not running");
			return;
		}
		
		List<Role> T = m.Message.getMentionedRoles();
		if (T.size() <1) {
			Print.Err(m, "Mention atleast one role");
			return;
		}
		
		if (T.size() !=1) {
			Print.Out(m, "Only the first role group will deleted");

		}
		
		try {
			PermCalc.DelGroup(T.get(0));
			Print.Suc(m);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Print.Err(m, "Could not delete group");
		} 
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Ping a role linked to a role within Luckperms to delete it within Luckperms";
	}

}
