package org.han.mc.bungee.module.Perms.lucky;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import com.google.gson.annotations.Expose;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.group.Group;

public class GroupObj {
	@Expose
	String Name;
	@Expose
	String ID;
	GroupObj(String Name, String ID) {
		this.Name = Name;
		this.ID = ID;

	}

	static GroupObj Load(String ID) throws FileNotFoundException, IOException {
		File F = FileObj.Fetch("DB/", ID, "json");
		if (!F.exists())
			throw new FileNotFoundException("Not a valid linked role");
		return FileObj.fromjson(FileObj.read(F, ""), GroupObj.class);
	}

	void Dell() throws FileNotFoundException {
		FileObj.erase(FileObj.Fetch("DB/", ID, "json"));
		LuckPerms api = LuckPermsProvider.get();
		Debug.out("Deleting group: \"" + Name + "\"");
		Group T = api.getGroupManager().getGroup(Name);
		if (T != null) {
			api.getGroupManager().deleteGroup(T);
		}
	}

	void save() {
		try {
			String self = FileObj.tojson(this);
			FileObj.FileChk("DB/");
			File F = FileObj.Fetch("DB/", ID, "json");
			FileObj.writeNF(new String[] { self }, F, "group object");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Debug.err("Failed to save group object");
			Debug.Trace(e);
		}
	}

	

}
