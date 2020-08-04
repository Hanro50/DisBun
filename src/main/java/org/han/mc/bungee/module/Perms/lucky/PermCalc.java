package org.han.mc.bungee.module.Perms.lucky;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.locks.ReentrantLock;

import org.han.bot.BotCon;
import org.han.bot.Print;
import org.han.bot.com.Msg;
import org.han.link.GetDetails;
import org.han.link.LinkUp;
import org.han.mc.bungee.BPlugin;
import org.han.mc.bungee.ModuleLoader;
import org.han.mc.bungee.module.DisBunTimerModule;
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

public class PermCalc extends DisBunTimerModule implements ContextCalculator<ProxiedPlayer> {
	// final static String HEADER = "dbcon:";
	static String KEY = "disbun:";
	static String LINK = KEY + "link";
	static String MEMBER = KEY + "member";
	static String ROLE = KEY + "role";
	static String SYNC = KEY + "sync";
	public static PermCalc self;

	static ReentrantLock cachlock = new ReentrantLock();

	/**
	 * Load the lucksperms component of this plugin
	 */

	public void load(ModuleLoader Loader) {

		try {
			Debug.rep("Trying to load Luckperms intergration");
			LuckPerms api = LuckPermsProvider.get();
			self = new PermCalc();
			api.getContextManager().registerCalculator(self);
			enabled = true;
			ReSyncGroup();
			Debug.rep("Luckperms Sync check will run every: " + Loader.UPDATETIMER(this) + " minutes");
			Timer timer = new Timer();
			timer.schedule(new AutoUpdate(), 60000, Loader.UPDATETIMER(this) * 60000);
			Debug.rep("Successfully loaded Luckperms intergration");

		} catch (Exception e) {
			Debug.out("Unable to load Luckperm integration");
			Debug.Trace(e);
			enabled = false;
		}
	}

	public void deload() {
		if (enabled) {
			if (timer != null) {
				timer.cancel();
			}

			LuckPerms api = LuckPermsProvider.get();
			api.getContextManager().unregisterCalculator(self);
			self = null;
			enabled = false;
		}
	}

	public String ReSyncGroup() {
		if (!enabled)
			return "Luckperm module isn't running";
		Debug.rep("Starting Luckperms resync operation");
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
		if (L == null || L.length < 1) {
			api.getGroupManager().getLoadedGroups().forEach(f -> {
				if (f.getName().startsWith(KEY)) {
					api.getGroupManager().deleteGroup(f);
					api.getGroupManager().saveGroup(f);
				}
			});

			Debug.rep("Nothing to sync");
			return "```Nothing to sync```";
		}
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
		Debug.rep("Done with Luckperms resync operation");
		return out + "```";
	}

	public void DelGroup(Role R) throws FileNotFoundException, IOException {
		if (!enabled)
			return;
		GroupObj.Load(R.getId()).Dell();
	}

	public void MakeGroup(Msg m, Role R) {
		if (!enabled)
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

	public void UpdatePerms(ProxiedPlayer player) {
		if (!enabled)
			return;
		try {
			String ID = LinkUp.GetDiscordID(player.getUniqueId());
			if (ID == null)
				return;
			Member mem = GetDetails.getGuild().retrieveMemberById(ID).complete();
			if (mem == null)
				return;

			LuckPerms api = LuckPermsProvider.get();
			List<String> roles = new ArrayList<String>();

			for (Role MemRole : mem.getRoles()) {
				try {
					roles.add(GroupObj.Load(MemRole.getId()).Name);
				} catch (IOException e) {
				}
			}
			// User user = api.getUserManager().getUser(player.getUniqueId());
			api.getGroupManager().loadAllGroups().get();
			User user = api.getPlayerAdapter(ProxiedPlayer.class).getUser(player);
			user.data().clear(t -> {
				if (t instanceof InheritanceNode) {
					return ((InheritanceNode) t).getGroupName().startsWith(KEY);
				}
				return false;
			});
			for (Group Group : api.getGroupManager().getLoadedGroups()) {
				Debug.rep("scanning:" + Group.getName());
				if (Group.getName().startsWith(KEY)) {
					Debug.rep("checking:" + Group.getName());
					if (player.hasPermission("group." + Group)) {
						Debug.rep("user has perm");

						if (!roles.contains(Group.getName())) {
							if (user.getNodes(NodeType.INHERITANCE).contains(InheritanceNode.builder(Group).build())) {
								user.data().add(InheritanceNode.builder(Group).value(false).build());
								Debug.rep("removing:" + Group);
							}
						} else {
							if (!user.getNodes(NodeType.INHERITANCE).contains(InheritanceNode.builder(Group).build())) {
								// user.getNodes(NodeType.INHERITANCE).add();
								user.data().add(InheritanceNode.builder(Group).value(true).build());
								Debug.rep("adding:" + Group.getName());
							}
						}
					} else {
						Debug.rep("user does not have perm");
						if (roles.contains(Group.getName())) {
							if (!user.getNodes(NodeType.INHERITANCE).contains(InheritanceNode.builder(Group).build())) {
								// user.getNodes(NodeType.INHERITANCE).add();
								user.data().add(InheritanceNode.builder(Group).value(true).build());
								Debug.rep("adding:" + Group.getName());
							}
						} else {
							if (user.getNodes(NodeType.INHERITANCE).contains(InheritanceNode.builder(Group).build())) {
								user.data().add(InheritanceNode.builder(Group).value(false).build());
								// .add(InheritanceNode.builder(Group).expiry(1, TimeUnit.NANOSECONDS).build());
								// user.getNodes(NodeType.INHERITANCE).remove(InheritanceNode.builder(Group).build());
								Debug.rep("removing:" + Group.getName());
							}
						}
					}
					api.getUserManager().saveUser(user).get();
				}
			}

		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			Debug.Trace(e);
		}

		// player.hasPermission(permission);
	}

	public class AutoUpdate extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			Debug.rep("Syncing Discord and Luckperms groups");
			BPlugin.getproxyserv().getPlayers().forEach(player -> {
				if (LinkUp.GetDiscordID(player.getUniqueId()) != null) {
					UpdatePerms(player);
				}
			});

			// tmp.addAll();
			// List<UUID> tmp = new ArrayList<UUID>();
			// for (UUID uuid : LinkUp.UUIDList()) {
			// if (BPlugin.getproxyserv().getPlayer(uuid) != null) {
			// tmp.add(uuid);
			// }
			// }

			// int i = 1;
			/*
			 * for (UUID uuid : tmp) { all: { try { Debug.rep("Scanning : " + i + "/" +
			 * tmp.size()); i++; LuckPerms api = LuckPermsProvider.get();
			 * 
			 * User U = api.getUserManager().getUser(uuid); if (U == null) { try { U =
			 * api.getUserManager().loadUser(uuid).get(); } catch (InterruptedException |
			 * ExecutionException e) { // TODO Auto-generated catch block Debug.Trace(e);
			 * break all; } }
			 * 
			 * if (U == null) { Debug.wrn("nullpointer avoided"); break all; }
			 * 
			 * api.getGroupManager().loadAllGroups().get();
			 * 
			 * api.getGroupManager().getLoadedGroups().forEach(Group ->{ if
			 * (Group.getName().startsWith(KEY)) {
			 * 
			 * } });
			 * 
			 * 
			 * 
			 * /* final User FinalUser = U; Set<String> Fset = new HashSet<String>();
			 * FinalUser.getNodes(NodeType.INHERITANCE).forEach(F -> { if
			 * (F.getGroupName().startsWith(KEY)) {
			 * 
			 * if (Fset.contains(F.getGroupName())) {
			 * 
			 * F.toBuilder().value(true).build(); Fset.remove(F.getGroupName()); } else {
			 * F.toBuilder().value(false).build(); } } });
			 * 
			 * Fset.forEach(T -> { InheritanceNode node =
			 * InheritanceNode.builder(T).value(true).build(); FinalUser.data().add(node);
			 * });
			 *//*
				 * 
				 * //api.getUserManager().saveUser(FinalUser).get(); } catch
				 * (InterruptedException e) { // TODO Auto-generated catch block
				 * Debug.wrn("Sync Interrupted"); } catch (ExecutionException e) { // TODO
				 * Auto-generated catch block Debug.Trace(e); }
				 * 
				 * } }
				 */
		}

	}

	@Override
	public void calculate(ProxiedPlayer target, ContextConsumer consumer) {
		// MutableContextSet Temp = MutableContextSet.create();
		if (BotCon.isRunning()) {
			try {
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
					Debug.rep("player not a member");
					// PermCalc.Catch.put(UUID, Temp);
					return;
				}
				consumer.accept(MEMBER, "true");
				for (Role role : mem.getRoles()) {
					consumer.accept(PermCalc.ROLE, role.getName().toLowerCase());
				}
			} catch (RejectedExecutionException e) {
				Debug.err("This should not be executing...why is this being executed luckperms?!");
			}
		}
	}

	@Override
	public ContextSet estimatePotentialContexts() {

		Debug.rep("estimating Potential Contexts");
		ImmutableContextSet.Builder builder = ImmutableContextSet.builder();
		try {
			builder.add(LINK, "true");
			builder.add(LINK, "false");
			builder.add(MEMBER, "true");
			builder.add(MEMBER, "false");
			if (BotCon.isRunning()) {
				Guild T = GetDetails.getGuild();
				List<Role> F = T.getRoles();
				for (Role role : F) {
					builder.add(ROLE, role.getName());
				}
			}
		} catch (IllegalStateException | NullPointerException e) {
			Debug.Trace(e);
		}

		return builder.build();
	}

	@Override
	public String HelpText() {
		// TODO Auto-generated method stub
		return "LuckPerms:\nLuckPerms module";
	}

	@Override
	public int DefaultTime() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public String Name() {
		// TODO Auto-generated method stub
		return "LuckyPerms";
	}

	@Override
	public void AdCon(ModuleLoader Loader) {
		// TODO Auto-generated method stub

	}
}
