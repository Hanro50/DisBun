package org.han.bot.com.srv;

import java.util.List;

import org.han.bot.Print;
import org.han.bot.com.ComObj;
import org.han.bot.com.Msg;
import org.han.mc.bungee.module.Perms.lucky.PermCalc;

import net.dv8tion.jda.api.entities.Role;

public class CMakeGroup extends ComObj {

	public CMakeGroup() {
		super(Place.Server, Permlv.Trusted, Visible.LuckPerm);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String com() {
		// TODO Auto-generated method stub
		return "mkgroup";
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
			Print.Out(m, "Only the first role will be made into a group");

		}
		
		PermCalc.MakeGroup(m, T.get(0) );
	}

	@Override
	public String Help() {
		// TODO Auto-generated method stub
		return "Ping a role to make it a role within Luckperms";
	}

	

}
