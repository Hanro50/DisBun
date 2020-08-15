package org.han.mc.spigot;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.han.common.Channels;
import org.han.mc.spigot.listeners.Advancementlistener;
import org.han.mc.spigot.listeners.DeathMessageListener;
import org.han.mc.spigot.module.PlaceHolderapiClientSide;
import org.han.spigot.Placeholderdata;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class SPlugin extends JavaPlugin implements PluginMessageListener {
	static SConfig Config;
	public static SPlugin self;
	List<String> AchievementTxt = new ArrayList<String>();

	@Override
	public void onEnable() {

		self = this;
		FileObj.Root = this.getFile().getAbsolutePath();
		FileObj.FileChkroot(this.getFile().getAbsolutePath());
		FileObj.ClassPath = this.getDataFolder().getAbsolutePath() + "/";
		FileObj.FileChk("");
		Debug.boot(new org.han.common.LogDebug(getLogger()),true);
		FileObj.FileChkroot("");
		FileObj.FileChk("");
		Config = new SConfig();
		Debug.Debugmode = Config.DebugMode();
		getLogger().info("Starting Spigot Component of DBcon");
		checkIfBungee();
		if (!getServer().getPluginManager().isPluginEnabled(this))
			return;
		getServer().getMessenger().registerIncomingPluginChannel(this, Channels.Main, this);
		getServer().getMessenger().registerOutgoingPluginChannel(this, Channels.Main);
		getLogger().info("Started Spigot Component of DBcon succesfully.");
		getServer().getPluginManager().registerEvents(new Advancementlistener(), this);
		getServer().getPluginManager().registerEvents(new DeathMessageListener(), this);
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

	

}
