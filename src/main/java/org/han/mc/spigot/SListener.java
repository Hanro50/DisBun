package org.han.mc.spigot;

import java.util.UUID;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.inventory.PlayerInventory;
import org.han.link.Channels;
import org.han.mc.spigot.module.Advancement;
import org.han.mc.spigot.module.DeathMessage;
import org.han.xlib.Debug;
import org.han.xlib.types.vectors.vec;
import org.han.xlib.types.vectors.vector3;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class SListener implements Listener {
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

		// String[] Fout = FileObj.read(F);
		// for (String string : Fout) {
		// Debug.out(string);
		// }

	}

	@EventHandler
	void PlayerDeathEvent(org.bukkit.event.entity.PlayerDeathEvent e) {
		DeathMessage A = null;
		Debug.out(e.getDeathMessage());
		Debug.out("player:" + e.getEntity().getUniqueId());
		// e.getEntity().getUniqueId();
		vector3<Double> DP = vec.D3(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(),
				e.getEntity().getLocation().getY());
		Debug.out("Deathpose:" + DP.tostring());
		// Debug.out("XP dropped :" + ((float) e.getEntity().getLevel() +
		// e.getEntity().getExp()));
		float XPLoast = ((float) e.getEntity().getLevel() + e.getEntity().getExp());
		UUID player = null;
		if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent LastD = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
			String Mob = LastD.getDamager().getType().getKey().getKey();
			String Weapon = null;
			// Debug.out("Entity killer (Entity):" +
			// LastD.getDamager().getType().getKey().getKey());
			// Debug.out("Entity killer (Entity):" + LastD.getDamager().getName());
			if (LastD.getDamager().getCustomName() != null) {
				// Debug.out("Entity killer (Entity):" + LastD.getDamager().getCustomName());
				Mob = LastD.getDamager().getCustomName();
			}
			// Debug.out("Entity killer (Method):" + LastD.getCause().toString());
			if (LastD.getDamager() instanceof HumanEntity) {
				Debug.out("test");
				PlayerInventory inv = ((HumanEntity) LastD.getDamager()).getInventory();
				if (inv.getItem(inv.getHeldItemSlot()).getItemMeta().hasDisplayName()) {
					Weapon = inv.getItem(inv.getHeldItemSlot()).getItemMeta().getDisplayName();
				}
			}

			if (LastD.getDamager() instanceof Player) {
				// Debug.out(((Player) LastD.getDamager()).getUniqueId() + "");
				player = LastD.getDamager().getUniqueId();

			}
			A = new DeathMessage(DP, SPlugin.DMTranslate(LastD.getCause()), Mob, e.getEntity().getUniqueId(), player,
					XPLoast, Weapon);
		} else {
			if (e.getEntity().getKiller() != null) {
				player = e.getEntity().getKiller().getUniqueId();
			}

			A = new DeathMessage(DP, SPlugin.DMTranslate(e.getEntity().getLastDamageCause().getCause()), null,
					e.getEntity().getUniqueId(), player, XPLoast, null);
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		if (A != null) {
			out.writeUTF(Channels.DeathMessage);
			out.writeUTF(A.encode());
			e.getEntity().sendPluginMessage(SPlugin.self, Channels.Main, out.toByteArray());
		}

		// Debug.out("damage cause
		// :"+e.getEntity().getLastDamageCause().getEventName());
		// Debug.out("damage cause
		// :"+e.getEntity().getLastDamageCause().getCause().name());
		// Debug.out("damage cause
		// :"+e.getEntity().getLastDamageCause().getCause().toString());
		// Debug.out("damage cause
		// :"+e.getEntity().getLastDamageCause().getEntityType().getKey().getKey());
		// Debug.out("damage cause :"+e.getEventName());
		/// Debug.out("damage cause :"+e.getDeathMessage());
		// try {
		// Debug.out("Entity killer (player):" +
		// e.getEntity().getKiller().getEntityId());
		// } catch (NullPointerException e1) {
		// }
		// Entity entityKiller = e.getEntity().get;
		// Debug.out("Entity killer (mob):" + entityKiller.getType().getKey().getKey());
		
	}
	
}


