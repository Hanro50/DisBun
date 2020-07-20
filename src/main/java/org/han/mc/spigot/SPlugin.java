package org.han.mc.spigot;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.han.link.TextMsg;
import org.han.xlib.Debug;
import org.han.xlib.FileObj;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class SPlugin extends JavaPlugin implements PluginMessageListener {
	static SConfig Config;

	@Override
	public void onEnable() {
		
		FileObj.Root = this.getFile().getAbsolutePath();
		FileObj.FileChkroot(this.getFile().getAbsolutePath());
		FileObj.ClassPath = this.getDataFolder().getAbsolutePath() +"/";
		FileObj.FileChk("");
		
		//FileObj.Init(this.getFile().getAbsoluteFile());
		Debug.Override = getLogger();
		FileObj.FileChkroot("");
		FileObj.FileChk("");
		Config = new SConfig();
		getLogger().info("Starting Spigot Component of DBcon");
		checkIfBungee();
		if (!getServer().getPluginManager().isPluginEnabled(this))
			return;
		getServer().getMessenger().registerIncomingPluginChannel(this, TextMsg.Channel, this);
		getServer().getMessenger().registerOutgoingPluginChannel(this,  TextMsg.Channel);
		getLogger().info("Started Spigot Component of DBcon succesfully.");

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(TextMsg.SubconfigPing);
		getServer().sendPluginMessage(this, TextMsg.Channel, out.toByteArray());

	}

	@Override
	public void onDisable() {
		getLogger().info("Stopping Spigot Component of DBcon");
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		// TODO Auto-generated method stub
		if (!channel.equalsIgnoreCase(TextMsg.Channel)) {
			return;
		}
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		String subChannel = in.readUTF();
		if (subChannel.equalsIgnoreCase(TextMsg.SubChannel)) {
			MsgHandling.Message(in, getServer());
			// String plr = in.readUTF();
			// String msg = in.readUTF();
			// getServer().broadcastMessage("§9[§1Discord§9] " + plr + ": " + msg);
		} else if (subChannel.equalsIgnoreCase(TextMsg.Subconfig)) {
			MsgHandling.ConfigSync(in);
			Debug.rep("Sync pulse received from head server");
			
		}
	}

	private void checkIfBungee() {
		// we check if the server is Spigot/Paper (because of the spigot.yml file)
		//if (!getServer().getVersion().replaceAll("-", " ").toLowerCase().contains("spigot")
			//	|| !getServer().getVersion().replaceAll("-", " ").toLowerCase().contains("paper")) {
			//getLogger().severe("Unsupported server version detected");
			///getLogger().severe("Version: " + getServer().getVersion());
			//getLogger().severe("Things might break");
			// getServer().getPluginManager().disablePlugin(this);
			// return;
		//}
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
