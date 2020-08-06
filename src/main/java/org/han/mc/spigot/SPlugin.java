package org.han.mc.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.han.link.Channels;
import org.han.mc.spigot.module.PlaceHolderapiClientSide;
import org.han.mc.spigot.module.Placeholderdata;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class SPlugin extends JavaPlugin implements PluginMessageListener {
	static SConfig Config;
	static SPlugin self;
	List<String> AchievementTxt = new ArrayList<String>();

	@Override
	public void onEnable() {
		self = this;
		FileObj.Root = this.getFile().getAbsolutePath();
		FileObj.FileChkroot(this.getFile().getAbsolutePath());
		FileObj.ClassPath = this.getDataFolder().getAbsolutePath() + "/";
		FileObj.FileChk("");

		// FileObj.Init(this.getFile().getAbsoluteFile());
		Debug.Override = getLogger();
		FileObj.FileChkroot("");
		FileObj.FileChk("");
		Config = new SConfig();
		Debug.Debug = Config.DebugMode();
		getLogger().info("Starting Spigot Component of DBcon");
		checkIfBungee();
		if (!getServer().getPluginManager().isPluginEnabled(this))
			return;
		getServer().getMessenger().registerIncomingPluginChannel(this, Channels.Main, this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, Channels.Main);
		getLogger().info("Started Spigot Component of DBcon succesfully.");
		getServer().getPluginManager().registerEvents(new SListener(), this);

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(Channels.configPing);
		getServer().sendPluginMessage(this, Channels.Main, out.toByteArray());

		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
			if (new PlaceHolderapiClientSide(this).register()) {
				Placeholderdata.enabled = true;
				Debug.out("loaded PlaceholderAPI module");
			} else {
				Debug.out("PlaceholderAPI module did not load");
			}

		}

	}

	@Override
	public void onDisable() {
		getLogger().info("Stopping Spigot Component of DBcon");
		HandlerList.unregisterAll(this);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		// TODO Auto-generated method stub
		if (!channel.equalsIgnoreCase(Channels.Main)) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subChannel = in.readUTF();
		if (subChannel.equalsIgnoreCase(Channels.Out)) {
			MsgHandling.Message(in, getServer());
		} else if (subChannel.equalsIgnoreCase(Channels.configsend)) {
			MsgHandling.ConfigSync(in);
			Debug.rep("Sync pulse received from head server");
		} else if (subChannel.equalsIgnoreCase(Channels.placeholderdata)) {
			Debug.rep("PlaceholderAPI Sync pulse received from head server");
			if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null && Placeholderdata.enabled) {

				PlaceHolderapiClientSide.data = FileObj.fromjson(in.readUTF(), Placeholderdata.class);
			}

		}
	}

	private void checkIfBungee() {
		File file = new File(getDataFolder().getParentFile().getParent(), "spigot.yml");
		FileConfiguration configuration = YamlConfiguration.loadConfiguration(file);
		if (!configuration.getBoolean("settings.bungeecord")) {
			getLogger().severe("This server is not BungeeCord.");
			getLogger().severe(
					"If the server is already hooked to BungeeCord, please enable it into your spigot.yml aswell.");
			getLogger().severe("Plugin disabled!");
			getServer().getPluginManager().disablePlugin(this);
		}
	}

	public static String DMTranslate(DamageCause DM) {
		switch (DM) {
		case BLOCK_EXPLOSION: // BLOCK_EXPLOSION("death.attack.explosion"),
			return "death.attack.explosion";
		case CONTACT: // CONTACT("death.attack.generic.player"),
			return "death.attack.cactus";
		case CRAMMING: // CRAMMING("death.attack.cramming"),
			return "death.attack.cramming";
		default:
		case CUSTOM: // CUSTOM("%1$s died"),
			return "death.attack.generic";
		case DRAGON_BREATH: // DRAGON_BREATH("death.attack.dragonBreath"),
			return "death.attack.dragonBreath";
		case DROWNING: // DROWNING("death.attack.drown"),
			return "death.attack.drown";
		case DRYOUT:
			return "death.attack.drown";
		case ENTITY_ATTACK: // ENTITY_ATTACK("death.attack.mob"),
			return "death.attack.mob";
		case ENTITY_EXPLOSION: // ENTITY_EXPLOSION("death.attack.explosion.player"),
			return "death.attack.explosion";
		case ENTITY_SWEEP_ATTACK:
			return "death.attack.player";
		case FALL: // FALL("death.attack.fall"),
			return "death.attack.fall";
		case FALLING_BLOCK: // FALLING_BLOCK("death.attack.fallingBlock"),
			return "death.attack.anvil";
		case FIRE: // FIRE("death.attack.inFire"),
			return "death.attack.inFire";
		case FIRE_TICK: // FIRE_TICK("death.attack.onFire"),
			return "death.attack.onFire";
		case FLY_INTO_WALL: // FLY_INTO_WALL("death.attack.flyIntoWall"),
			return "death.attack.flyIntoWall";
		case HOT_FLOOR: // HOT_FLOOR("death.attack.hotFloor"),
			return "death.attack.hotFloor";
		case LAVA: // LAVA("death.attack.lava"),
			return "death.attack.lava";
		case LIGHTNING: // LIGHTNING("death.attack.lightningBolt"),
			return "death.attack.lightningBolt";
		case MAGIC: // MAGIC("death.attack.magic"),
			return "death.attack.magic";
		case MELTING: // MELTING("death.attack.generic"),
			return "death.attack.generic";
		case POISON: // POISON("death.attack.magic"),
			return "death.attack.magic";
		case PROJECTILE: // PROJECTILE("death.attack.arrow"),
			return "death.attack.arrow";
		case STARVATION: // STARVATION("death.attack.starve"),
			return "death.attack.starve";
		case SUFFOCATION: // SUFFOCATION("death.attack.inWall"),
			return "death.attack.inWall";
		case SUICIDE: // SUICIDE("death.attack.even_more_magic"),
			return "death.attack.even_more_magic";
		case THORNS: // THORNS("death.attack.thorns"),
			return "death.attack.thorns";
		case VOID: // VOID("death.attack.outOfWorld"),
			return "death.attack.outOfWorld";
		case WITHER: // WITHER("death.attack.wither"),
			return "death.attack.wither";
		}

		// return "death.attack.generic";
	}

}
