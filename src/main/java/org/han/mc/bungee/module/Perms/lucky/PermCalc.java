package org.han.mc.bungee.module.Perms.lucky;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import org.han.bot.BotCon;
import org.han.bot.Print;
import org.han.bot.com.Msg;
import org.han.link.GetDetails;
import org.han.link.LinkUp;
import org.han.mc.bungee.BPlugin;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.context.ContextCalculator;
import net.luckperms.api.context.ContextConsumer;
import net.luckperms.api.context.ContextSet;
import net.luckperms.api.context.ImmutableContextSet;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.DisplayNameNode;
import net.luckperms.api.node.types.InheritanceNode;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class PermCalc implements ContextCalculator<ProxiedPlayer> {
	// final static String HEADER = "dbcon:";
	static String KEY = "dbcon:";
	static String LINK = KEY + "link";
	static String MEMBER = KEY + "member";
	static String ROLE = KEY + "role";
	static String SYNC = KEY + "sync";
	static ContextCalculator<net.md_5.bungee.api.connection.ProxiedPlayer> self;
	static Timer timer;
	public static boolean endabled = false;

	static ReentrantLock cachlock = new ReentrantLock();

	/**
	 * Load the lucksperms component of this plugin
	 */

	public static void load() {
		deload();
		if (BPlugin.Config.LPEnabled() && BotCon.isRunning()) {
			try {
				Debug.out("Trying to load Luckperms intergration");
				LuckPerms api = LuckPermsProvider.get();
				self = new PermCalc();
				api.getContextManager().registerCalculator(self);
				endabled = true;
				ReSyncGroup();
				Debug.out("Luckperms Sync check will run every: " + BPlugin.Config.LPRefresh() + " minutes");
				Timer timer = new Timer();
				timer.schedule(new AutoUpdate(), 60000, BPlugin.Config.LPRefresh() * 60000);
				Debug.out("Successfully loaded Luckperms intergration");

			} catch (Exception e) {
				Debug.out("Unable to load Luckperm integration");
				Debug.Trace(e);
				endabled = false;
			}
		}
	}

	public static void deload() {
		if (endabled) {
			if (timer != null) {
				timer.cancel();
			}

			LuckPerms api = LuckPermsProvider.get();
			api.getContextManager().unregisterCalculator(self);
			self = null;
			endabled = false;
		}
	}

	public static String ReSyncGroup() {
		if (!endabled)
			return "Luckperm module isn't running";
		Debug.out("Starting Luckperms resync operation");
		File[] L = FileObj.FileList("DB/", "json");
		LuckPerms api = LuckPermsProvider.get();
		Guild Srv = null;
		try {
			Srv = GetDetails.getGuild();
		} catch (IllegalStateException e) {

		}

		if (Srv == null) {
			Debug.err("Failed to resync. Resyncing now would destroy everything");
			return "Failed to resync. Resyncing now would destroy everything";
		}
		String out = "```";
		int i = 1;
		for (File file : L) {
			Debug.rep("Scanning : " + i++ + "/" + L.length);
			try {
				GroupObj f = FileObj.fromjson(FileObj.read(file, ""), GroupObj.class);
				Group T = api.getGroupManager().getGroup(f.Name);

				if (T == null) {
					Debug.err("Group: \"" + f.Name + "\" is no longer valid. Removing it...");
					out = out + "Group: \"" + f.Name + "\" is no longer valid. Removing it...\n";
					f.Dell();
				}

				Role F = Srv.getRoleById(f.ID);

				if (F == null) {
					Debug.err("Group: \"" + f.Name
							+ "\" is no has a synced role. Removing it now as it can't be removed normally...");
					out = out + "Group: \"" + f.Name
							+ "\" is no has a synced role. Removing it now as it can't be removed normally...\n";
					f.Dell();
				}

			} catch (IOException e) {
				Debug.wrn(e.getMessage());
				out = out + e.getMessage() + "\n";
			}

		}
		Debug.out("Done with Luckperms resync operation");
		return out + "```";
	}

	public static void DelGroup(Role R) throws FileNotFoundException, IOException {
		if (!endabled)
			return;
		GroupObj.Load(R.getId()).Dell();
	}

	public static void MakeGroup(Msg m, Role R) {
		if (!endabled)
			return;
		LuckPerms api = LuckPermsProvider.get();
		Group T = api.getGroupManager().getGroup(KEY + R.getName());
		if (T != null) {
			Print.Err(m, "Group already exists");
			return;
		}
		try {
			T = api.getGroupManager().createAndLoadGroup(KEY + R.getName()).get();
			GroupObj F = new GroupObj(T.getName(), R.getId());
			F.save();

			T.data().add(DisplayNameNode.builder(R.getName()).build());
			api.getGroupManager().saveGroup(T);
			Print.Suc(m);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			Debug.err("Failed to create group");
			Debug.Trace(e);
		}
	}

	public static void RoleUpdate(UUID UUID) {
		// Updateobj.Do(UUID);
	}

	static public class AutoUpdate extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Debug.rep("Syncing Discord and Luckperms groups");

			// tmp.addAll();
			List<UUID> tmp = new ArrayList<UUID>();
			for (UUID uuid : LinkUp.UUIDList()) {
				if (BPlugin.getproxyserv().getPlayer(uuid) != null) {
					tmp.add(uuid);
				}
			}

			int i = 1;

			for (UUID uuid : tmp) {
				all: {
					Debug.out("Scanning : " + i + "/" + tmp.size());
					i++;
					LuckPerms api = LuckPermsProvider.get();

					User U = api.getUserManager().getUser(uuid);
					if (U == null) {
						try {
							U = api.getUserManager().loadUser(uuid).get();
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							Debug.Trace(e);
							break all;
						}
					}

					if (U == null) {
						break all;
					}

					final User FU = U;
					Set<String> Fset = new HashSet<String>();
					FU.getNodes(NodeType.INHERITANCE).forEach(F -> {
						if (F.getGroupName().startsWith(KEY)) {
							if (Fset.contains(F.getGroupName())) {
								Fset.remove(F.getGroupName());
							} else {
								F.toBuilder().value(false).build();
							}
						}
					});

					Fset.forEach(T -> {
						InheritanceNode node = InheritanceNode.builder(T).build();
						FU.data().add(node);
					});
					api.getUserManager().saveUser(FU);

				}
			}

		}
	}

	@Override
	public void calculate(ProxiedPlayer target, ContextConsumer consumer) {
		// MutableContextSet Temp = MutableContextSet.create();
		String ID = LinkUp.GetDiscordID(target.getUniqueId());
		Guild T = GetDetails.getGuild();
		if (ID == null || T == null) {
			consumer.accept(PermCalc.LINK, "false");
			consumer.accept(PermCalc.MEMBER, "false");
			// Debug.out("player not linked");
			// PermCalc.Catch.put(UUID, Temp);
			return;

		}
		consumer.accept(PermCalc.LINK, "true");
		Member mem = null;
		try {
			// mem = T.retrieveMemberById(ID, false).complete(); // Get cached version
			mem = T.retrieveMemberById(ID).complete();
			// Debug.out("Discord takes a while to respond. Context calculations take a
			// while as a result");
			// Update it.
		} catch (ErrorResponseException E) {
		}
		if (mem == null) {
			consumer.accept(PermCalc.MEMBER, "false");
			Debug.out("player not a member");
			// PermCalc.Catch.put(UUID, Temp);
			return;
		}
		consumer.accept(MEMBER, "true");
		for (Role role : mem.getRoles()) {
			consumer.accept(PermCalc.ROLE, role.getName().toLowerCase());
		}

	}

	@Override
	public ContextSet estimatePotentialContexts() {
		Debug.out("estimatePotentialContexts");
		ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
		try {
			builder.add(LINK, "true");
			builder.add(LINK, "false");
			builder.add(MEMBER, "true");
			builder.add(MEMBER, "false");
			Guild T = GetDetails.getGuild();
			List<Role> F = T.getRoles();
			for (Role role : F) {
				builder.add(ROLE, role.getName());
			}
		} catch (IllegalStateException | NullPointerException e) {
			Debug.Trace(e);
		}

		return builder.build();
	}
}
