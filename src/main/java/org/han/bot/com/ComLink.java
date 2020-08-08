package org.han.bot.com;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.han.bot.Print;
import org.han.bot.com.any.*;
import org.han.bot.com.srv.*;
import org.han.mc.bungee.BPlugin;
import org.han.xlib.AbsConfig;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

public class ComLink extends AbsConfig {
	static ComLink Self;
	final static String Version = "Version";
	public final static String Langcom = ".com";
	public final static String helptxt = ".helptxt";
	public final static String Success = "print.success";
	public final static String Error = "print.error";

	public static String getsuccess() {
		if (Self != null)
			return Self.get(Success);
		return " %U Success";
	}

	public static String geterror() {
		if (Self != null)
			return Self.get(Success);
		return " %U Error";
	}

	private ComLink() {
		super(FileObj.Fetch("", "Discord_Config", "txt"));
		if (NEWCONFIG)
			Register("Version of this file\n Do not edit", Version, BPlugin.Self.getDescription().getVersion());
		else
			Register("Version of this file\n Do not edit", Version, "0.0.0");
		// TODO Auto-generated constructor stub

		Register("Success text ( %U Will be replaced with a user mention)", Success, " %U Success");
		Register("Error text ( %U Will be replaced with a user mention)", Error, " %U Error");

		Coms.keySet().forEach(f -> {
			Register("Enable the " + f + " command", f + ".enable", "true");
			Register("Chang the command for the " + f + " command", f + Langcom, Coms.get(f).getCom());
			Register("Chang the helptext for the " + f + " command", f + helptxt, Coms.get(f).Help());
			Coms.get(f).Adcon(this);
			remap.put(get(f + Langcom), f);
		});

	}

	public static void UpdateCheck() {
		if (BPlugin.Self.getDescription().getVersion().equalsIgnoreCase(Self.get(Version)))
			return;
		Debug.out("Updating Discord_Config file");
		Debug.out(Self.get(Version) + " -> " + BPlugin.Self.getDescription().getVersion());
		File OLDFILE = FileObj.Fetch("", "oldDiscord_Config", "txt");
		OLDFILE.delete();
		File OLD = FileObj.Fetch("", "Discord_Config", "txt");
		OLD.renameTo(OLDFILE);
		ComLink NEWCONFIG = new ComLink();
		Self.edit(Version, BPlugin.Self.getDescription().getVersion());
		UpdateConfig(Self, NEWCONFIG);
		FileObj.Fetch("", "oldDiscord_Config", "txt").deleteOnExit();
	}

	// Register("Enable the " + com + " command", "com." + com, "true");
	private static Map<String, ComObj> Coms = new ConcurrentHashMap<String, ComObj>();

	static {

		RegCom(new CToggleDebug());
		RegCom(new CDeTrust());
		RegCom(new CHelp());
		RegCom(new CTrust());
		RegCom(new CInfo());

		RegCom(new CAccDelink());
		RegCom(new CDellGroup());
		RegCom(new CLink());
		RegCom(new CList());
		RegCom(new CMakeGroup());
		RegCom(new CReload());
		RegCom(new CSetup());
		RegCom(new CSyncGroup());
		RegCom(new CUnlink());
		Self = new ComLink();
	}

	public static final Map<String, ComObj> GetComMap() {
		return Coms;
	}

	private Map<String, String> remap = new HashMap<String, String>();

	public static void RegCom(ComObj Com) {
		String com = Com.getCom().trim().toLowerCase();
		if (Coms.containsKey(com)) {
			int i = 1;
			do {
				com = Com.getCom().trim().toLowerCase() + "_" + i;
				i++;
			} while (Coms.containsKey(com));
			Debug.err("Unique names should be used->Renaming command \"" + Com.getCom() + "\" to \"" + com + "\"");
		}
		Coms.put(com, Com);

	}

	public static boolean iSEnable(String com) {
		return Self.boolcheck("com." + com);
	}

	public static void DoCom(Msg m) {
		if (!m.isCom())
			return;
		String com = Self.remap.get(m.getCom());
		if (Coms.containsKey(com)) {

			if (!iSEnable(m.getCom())) {
				Print.Err(m, "This command has been disabled by the operator");
				return;
			}
			ComObj CC = Coms.get(com);
			if (CC.place.chk(m)) {
				if (CC.permlv.chk(m))
					Coms.get(m.getCom()).Run(m);
				else
					Print.Err(m, "insufficient Permissions");
			}
		}
	}

	public void regSettings(ComObj module, String HelpText, String Setting, String DefaultValue) {
		Register(HelpText, module.com() + "." + Setting, DefaultValue);
	}

	public String ChkStr(ComObj module, String Setting) {
		return get(module.com() + "." + Setting);
	}

	public boolean ChkBool(ComObj module, String Setting) {
		return boolcheck(module.com() + "." + Setting);
	}

	public int ChkInt(ComObj module, String Setting, int DefaultVal) {
		return Valuecheck(module.com() + "." + Setting, DefaultVal);
	}

	public void edit(ComObj module, String Setting, String NewVal) {
		edit(module.com() + "." + Setting, NewVal);
	}

}
