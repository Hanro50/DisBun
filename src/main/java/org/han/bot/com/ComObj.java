package org.han.bot.com;

import org.han.mc.bungee.module.Perms.lucky.PermCalc;

/**
 * Hanro Discord Libraries. Command Object. Forms the bases for handling
 * commands in JDA.
 * 
 * @author hanro50
 *
 */
public abstract class ComObj {
	public void Adcon(ComLink Handler) {}

	/**
	 * The place where this command can be used. </br>
	 * <b>Any</b>: Any place, Server or within private messages. </br>
	 * <b>Server</b>: Restrict a command so it may only be used in guilds. </br>
	 * <b>Private</b>: Restrict a command so it may only be used in private
	 * messages. </br>
	 * 
	 * @author hanro50
	 * @implNote This shouldn't change...unless JDA/the discord API changes.
	 */
	public static enum Place {
		Any {
			@Override
			public boolean chk(Msg m) {
				return true;
			};
		},

		Server {
			@Override
			public boolean chk(Msg m) {
				return m.isServer();
			};
		},
		Private {
			@Override
			public boolean chk(Msg m) {
				return !m.isServer();
			};
		};

		public boolean chk(Msg m) {
			return false;
		};
	}

	/**
	 * Runs a function to see if a set user has the required permissions to do a set
	 * action. Commands users aren't suppose to be able to run should be hidden.
	 * 
	 * @author hanro50
	 * @implNote This method is edited a lot depending on the project in question
	 */
	public static enum Permlv {
		all {
		},
		// Mod{public boolean chk (Msg m) {return PermManager.HasMod(m);};},
		Admin {
			public boolean chk(Msg m) {
				return m.isAdmin();
			};
		},
		Trusted {
			public boolean chk(Msg m) {
				return Operator.isOped(m.Sender.User);
			};
		},
		BotOwn {
			public boolean chk(Msg m) {
				return m.isBotOwner();
			};
		};

		public boolean chk(Msg m) {
			return true;
		};
	}

	/**
	 * Whether a set command should be visible when when the help command is ran.
	 * This will hide the command even if the user can technically run it. However,
	 * the user will still need to be able to run it to see it regardless
	 * 
	 * @author hanro50
	 * @implNote This method is edited a lot depending on the project in question
	 */
	public enum Visible {
		yes,
		LuckPerm {
			public boolean chk(Msg m) {
				return PermCalc.self.enabled;
			}
		},
		no {
			public boolean chk(Msg m) {
				return false;
			}
		};

		public boolean chk(Msg m) {
			return true;
		};
	}

	public final Place place;
	public final Permlv permlv;
	public final Visible visible;

	public ComObj(Place Place, Permlv Permlv) {

		this.place = Place;
		this.permlv = Permlv;
		this.visible = Visible.yes;
	}

	public ComObj(Place Place, Permlv Permlv, Visible Visible) {

		this.place = Place;
		this.permlv = Permlv;
		this.visible = Visible;
	}

	/**
	 * The command. This is used by the command handler and will be the line of text
	 * used to execute this command within discord
	 */
	abstract protected String com();

	/**
	 * The code that will be ran when the command is executed
	 * 
	 * @param m The message object
	 */
	abstract public void Run(Msg m);

	/**
	 * Used by potential help commands
	 * 
	 * @return The help text of this command
	 */
	abstract public String Help();

	/**
	 * Used by the command handler.
	 * 
	 * @see {@link #com() com()}
	 * @return A formated version of the text returned from the internal
	 *         {@link #com() com()} command
	 */
	public String getCom() {
		return com().toLowerCase().trim().replaceAll(" ", "_");
	}
	

}
