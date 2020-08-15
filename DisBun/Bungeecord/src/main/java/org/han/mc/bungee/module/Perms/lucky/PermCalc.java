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
import org.han.bot.Msg;
import org.han.bot.Print;
import org.han.common.PermKeys;
import org.han.disbun.DisBunTimerModule;
import org.han.disbun.ModuleLoader;
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

public class PermCalc extends DisBunTimerModule implements ContextCalculator<ProxiedPlayer> {
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
				if (f.getName().startsWith(PermKeys.KEY)) {
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
		Group T = api.getGroupManager().getGroup(PermKeys.KEY + R.getName());
		if (T != null) {
			Print.Err(m, "Group already exists");
			return;
		}
		try {
			T = api.getGroupManager().createAndLoadGroup(PermKeys.KEY + R.getName()).get();
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
					return ((InheritanceNode) t).getGroupName().startsWith(PermKeys.KEY);
				}
				return false;
			});
			for (Group Group : api.getGroupManager().getLoadedGroups()) {
				Debug.rep("scanning:" + Group.getName());
				if (Group.getName().startsWith(PermKeys.KEY)) {
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
					consumer.accept(PermKeys.LINK, "false");
					consumer.accept(PermKeys.MEMBER, "false");
					return;

				}
				consumer.accept(PermKeys.LINK, "true");
				Member mem = null;
				try {
					mem = T.retrieveMemberById(ID).complete();
				} catch (ErrorResponseException E) {
				}
				if (mem == null) {
					consumer.accept(PermKeys.MEMBER, "false");
					Debug.rep("player not a member");
					return;
				}
				consumer.accept(PermKeys.MEMBER, "true");
				consumer.accept(PermKeys.BOOSTER,mem.getTimeBoosted()!=null?"true":"false");
				for (Role role : mem.getRoles()) {
					consumer.accept(PermKeys.ROLE, role.getName().toLowerCase());
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
			builder.add(PermKeys.LINK, "true");
			builder.add(PermKeys.LINK, "false");
			builder.add(PermKeys.MEMBER, "true");
			builder.add(PermKeys.MEMBER, "false");
			builder.add(PermKeys.BOOSTER, "true");
			builder.add(PermKeys.BOOSTER, "false");
			if (BotCon.isRunning()) {
				Guild T = GetDetails.getGuild();
				List<Role> F = T.getRoles();
				for (Role role : F) {
					builder.add(PermKeys.ROLE, role.getName());
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
