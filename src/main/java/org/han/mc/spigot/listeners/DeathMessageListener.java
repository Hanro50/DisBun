package org.han.mc.spigot.listeners;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.projectiles.ProjectileSource;
import org.han.link.Channels;
import org.han.mc.spigot.NameSpaceMappings;
import org.han.mc.spigot.SPlugin;
import org.han.mc.spigot.module.DeathMessage;
import org.han.xlib.Debug;
import org.han.xlib.types.vectors.vec;
import org.han.xlib.types.vectors.vector3;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class DeathMessageListener implements Listener {

	@EventHandler
	void PlayerDeathEvent(org.bukkit.event.entity.PlayerDeathEvent e) {

		Map<String, Integer> Enchantments = null;
		DeathMessage A = null;
		vector3<Double> DP = vec.D3(e.getEntity().getLocation().getX(), e.getEntity().getLocation().getY(),
				e.getEntity().getLocation().getY());

		float XPLoast = ((float) e.getEntity().getLevel() + e.getEntity().getExp());
		UUID player = null;

		if (e.getEntity().getLastDamageCause() instanceof EntityDamageByEntityEvent) {
			EntityDamageByEntityEvent LastD = (EntityDamageByEntityEvent) e.getEntity().getLastDamageCause();
			String Weapon = null;
			Entity Damager = LastD.getDamager();

			// TNT is annoying
			if (Damager instanceof TNTPrimed) {
				Damager = ((TNTPrimed) Damager).getSource();
			}

			if (Damager instanceof Projectile) {
				ProjectileSource shooter = ((org.bukkit.entity.Projectile) Damager).getShooter();
				if (shooter instanceof LivingEntity) {
					Damager = (LivingEntity) shooter;
				} else {
					Debug.out(shooter.toString());
				}
			}
			String Mob = null;
			//TNT can sometimes do weird stuff
			if (Damager != null) {
				try {
					Mob = "entity.minecraft." + LastD.getDamager().getType().getKey().getKey();
				} catch (NoSuchMethodError e1) {
					@SuppressWarnings("deprecation")
					String mobT = "entity.minecraft." + LastD.getDamager().getType().getName();
					Mob = mobT;
				}

				if (Damager.getCustomName() != null) {
					Mob = Damager.getCustomName();
				}
				if (Damager instanceof LivingEntity) {
					ItemMeta Meta = null;
					EntityEquipment inv = ((LivingEntity) Damager).getEquipment();
					try {
						Meta = inv.getItemInMainHand().getItemMeta();
					} catch (NoSuchMethodError e1) {
						@SuppressWarnings("deprecation")
						ItemMeta MetaT = inv.getItemInHand().getItemMeta();
						Meta = MetaT;
					}
					// }
					try {
						if (Meta.hasDisplayName()) {
							Weapon = Meta.getDisplayName();
							if (Meta.hasEnchants()) {
								Enchantments = new HashMap<String, Integer>();
								for (Enchantment f : Meta.getEnchants().keySet()) {
									try {
										Enchantments.put(f.getKey().getKey(), Meta.getEnchants().get(f));
									} catch (NoSuchMethodError e1) {
										@SuppressWarnings("deprecation")
										String NSK = NameSpaceMappings.OLDenchantmentnames(f.getName());
										Enchantments.put(NSK, Meta.getEnchants().get(f));
									}
								}
							}
						}
					} catch (NullPointerException err) {
						Debug.wrn("Nullpointer? Assuming item has no meta data then");
					}
				}

				if (Damager instanceof Player) {
					player = Damager.getUniqueId();
				}
			}

			A = new DeathMessage(DP, NameSpaceMappings.DMTranslate(LastD.getCause()), Mob, e.getEntity().getUniqueId(),
					player, XPLoast, Weapon, Enchantments);
			Debug.rep("DC:" + LastD.getCause().name());
			if (DP != null)
				Debug.rep("DP:" + DP.tostring());
			Debug.rep("C:" + NameSpaceMappings.DMTranslate(LastD.getCause()));
			if (Mob != null)
				Debug.rep("M:" + Mob);
			Debug.rep("P1:" + e.getEntity().getUniqueId().toString());
			if (player != null)
				Debug.rep("P2:" + player.toString());
			Debug.rep("XP:" + XPLoast);
			if (Weapon != null)
				Debug.rep("W:" + Weapon);
			if (Enchantments != null)
				Debug.rep("E:" + Enchantments.toString());

		} else {
			if (e.getEntity().getKiller() != null) {
				player = e.getEntity().getKiller().getUniqueId();
			}

			A = new DeathMessage(DP, NameSpaceMappings.DMTranslate(e.getEntity().getLastDamageCause().getCause()), null,
					e.getEntity().getUniqueId(), player, XPLoast, null, null);
		}
		ByteArrayDataOutput out = ByteStreams.newDataOutput();

		if (A != null) {
			out.writeUTF(Channels.DeathMessage);
			out.writeUTF(A.encode());
			e.getEntity().sendPluginMessage(SPlugin.self, Channels.Main, out.toByteArray());
		}

	}
}
