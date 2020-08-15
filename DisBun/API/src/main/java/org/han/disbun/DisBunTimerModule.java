package org.han.disbun;

import java.util.Timer;

public abstract class DisBunTimerModule extends DisBunModule {
	public abstract int DefaultTime();
	public Timer timer = null;

}
