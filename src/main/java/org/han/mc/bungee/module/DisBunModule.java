package org.han.mc.bungee.module;

import org.han.mc.bungee.ModuleLoader;

public abstract class DisBunModule {
	public abstract String HelpText();
	public abstract String Name();
	public abstract void load(ModuleLoader  Loader);
	public abstract void deload();
	public boolean enabled =false;
	int LastHash;
	/**
	 * Fired to add options to the loader module
	 */
	public abstract void AdCon(ModuleLoader  Loader);
}
