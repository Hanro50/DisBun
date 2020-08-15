package org.han.disbun;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.han.xlib.AbsConfig;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;
import static org.han.common.Var.*;

public class ModuleLoader extends AbsConfig {
	final String ENABLE = "enabled";
	final String UPDATE = "updatetimer";
	public static List<DisBunModule> RegModule = new ArrayList<DisBunModule>();
	static final String Version = "Version";
	static {
		RegModule.clear();
	//	RegModule.add(new Methodchanger());
	//	RegModule.add(new PlaceHolderapiServerSide());
	//	RegModule.add(new TopicLoader());
	//	RegModule.add(new PermCalc());
	//	RegModule.add(new AdvancementHandler());
	//	RegModule.add(new DeathMessageHandler());
	}

	public ModuleLoader() {
		super(FileObj.Fetch("", "modules", "txt"));
		
		if (NEWCONFIG)
			Register("Version of this file\n Do not edit", Version, org.han.common.Var.CurVer);
		else
			Register("Version of this file\n Do not edit", Version, "0.0.0");

		for (DisBunModule disBunModule : RegModule) {
			try {
			regSettings(disBunModule, disBunModule.HelpText(), ENABLE, "true");
			if (disBunModule instanceof DisBunTimerModule) {
				regSettings(disBunModule, "Update Timer for " + disBunModule.Name(), UPDATE,
						((DisBunTimerModule) disBunModule).DefaultTime() + "");
			}
			disBunModule.AdCon(this);
			
			} catch (Exception e) {
				Debug.err(disBunModule.getClass().getName() + ":Contains an error. Please report this to the appropriate dev...");
				Debug.Trace(e);
			}
		}
	}

	public void LOAD() {
		for (DisBunModule disBunModule : RegModule) {
			if (ChkBool(disBunModule, ENABLE)) {
				if (disBunModule.enabled) {
					disBunModule.deload();
				}
				disBunModule.load(this);
				disBunModule.Loader = this;
			}
		}
	}

	public void DELOAD() {
		for (DisBunModule disBunModule : RegModule) {
			if (ChkBool(disBunModule, ENABLE)) {
				disBunModule.deload();
			}
		}
	}

	public int UPDATETIMER(DisBunTimerModule module) {
		int i = ChkInt(module, UPDATE, module.DefaultTime());
		if (i < 1) {
			if (module.DefaultTime() < 1) {
				Debug.err(module.getClass().getName() + ":Contains an error. Please report this to the appropriate dev...");
				Debug.err( "Setting value to 60 min");
				edit(module, UPDATE, 60 + "");
			} else {
				edit(module, UPDATE, module.DefaultTime() + "");
			}
			try {
				Save();
				Debug.out("Invalid value detected. Resetting");
			} catch (IOException e) {
				Debug.Trace(e);
			}
			return module.DefaultTime();
		}
		return i;
	}

	public void regSettings(DisBunModule module, String HelpText, String Setting, String DefaultValue) {
		Register(HelpText, module.Name() + "." + Setting, DefaultValue);
	}

	public String ChkStr(DisBunModule module, String Setting) {
		return get(module.Name() + "." + Setting);
	}

	public boolean ChkBool(DisBunModule module, String Setting) {
		return boolcheck(module.Name() + "." + Setting);
	}

	public int ChkInt(DisBunModule module, String Setting, int DefaultVal) {
		return Valuecheck(module.Name() + "." + Setting, DefaultVal);
	}

	public void edit(DisBunModule module, String Setting, String NewVal) {
		edit(module.Name() + "." + Setting, NewVal);
	}
	
	public void UpdateCheck() {
		if (CurVer.equalsIgnoreCase(get(Version)))
			return;
		Debug.out("Updating Module file");
		Debug.out(get(Version) + " -> " + CurVer);
		File OLDFILE = FileObj.Fetch("", "oldmodfile", "txt");
		OLDFILE.delete();
		File OLD = FileObj.Fetch("", "modules", "txt");
		OLD.renameTo(OLDFILE);
		ModuleLoader NEWCONFIG = new ModuleLoader();
		edit(Version,CurVer);
		UpdateConfig(this, NEWCONFIG);
		FileObj.Fetch("", "oldmodfile", "txt").deleteOnExit();
	}


}
