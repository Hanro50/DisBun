package org.han.bot;

import com.google.gson.annotations.Expose;

public class RoleObj {
	@Expose
	public String RoleName;
	@Expose
	public String Color;

	public RoleObj(String RoleName, String color) {
		this.RoleName = RoleName;
		this.Color = color;
	}

}
