package org.han.mc.spigot.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.han.common.Channels;
import org.han.mc.spigot.SPlugin;
import org.han.mc.spigot.module.Advancement;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class Advancementlistener implements Listener {
	@EventHandler
	void PlayerAdvancementDoneEvent(PlayerAdvancementDoneEvent e) {
		if (e.getAdvancement().getKey().getKey().split("/").length < 1
				|| e.getAdvancement().getKey().getKey().split("/")[0].equalsIgnoreCase("recipes")) {
			return;
		}
		Advancement A = new Advancement(e.getPlayer().getUniqueId(), e.getAdvancement().getKey().getKey());

		ByteArrayDataOutput out = ByteStreams.newDataOutput();
		out.writeUTF(Channels.Advancement);
		out.writeUTF(A.encode());
		e.getPlayer().sendPluginMessage(SPlugin.self, Channels.Main, out.toByteArray());
	}

}
