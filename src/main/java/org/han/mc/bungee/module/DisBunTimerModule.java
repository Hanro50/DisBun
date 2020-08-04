package org.han.mc.bungee.module;

import java.util.Timer;

public abstract class DisBunTimerModule extends DisBunModule {
	public abstract int DefaultTime();
	public Timer timer = null;

}
