package org.han.mc.spigot;

import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.han.xlib.Debug;

public class NameSpaceMappings {
	public static String OLDenchantmentnames(String f) {
		if (f.equals("PROTECTION_ENVIRONMENTAL"))
			return "protection";
		if (f.equals("PROTECTION_FIRE"))
			return "fire_protection";
		if (f.equals("PROTECTION_FALL"))
			return "feather_falling";
		if (f.equals("PROTECTION_EXPLOSIONS"))
			return "blast_protection";
		if (f.equals("PROTECTION_PROJECTILE"))
			return "projectile_protection";
		if (f.equals("OXYGEN"))
			return "respiration";
		if (f.equals("WATER_WORKER"))
			return "aqua_affinity";
		if (f.equals("THORNS"))
			return "thorns";
		if (f.equals("DEPTH_STRIDER"))
			return "depth_strider";
		if (f.equals("FROST_WALKER"))
			return "frost_walker";
		if (f.equals("BINDING_CURSE"))
			return "binding_curse";
		if (f.equals("DAMAGE_ALL"))
			return "sharpness";
		if (f.equals("DAMAGE_UNDEAD"))
			return "smite";
		if (f.equals("DAMAGE_ARTHROPODS"))
			return "bane_of_arthropods";
		if (f.equals("KNOCKBACK"))
			return "knockback";
		if (f.equals("FIRE_ASPECT"))
			return "fire_aspect";
		if (f.equals("LOOT_BONUS_MOBS"))
			return "looting";
		if (f.equals("SWEEPING_EDGE"))
			return "sweeping";
		if (f.equals("DIG_SPEED"))
			return "efficiency";
		if (f.equals("SILK_TOUCH"))
			return "silk_touch";
		if (f.equals("DURABILITY"))
			return "unbreaking";
		if (f.equals("LOOT_BONUS_BLOCKS"))
			return "fortune";
		if (f.equals("ARROW_DAMAGE"))
			return "power";
		if (f.equals("ARROW_KNOCKBACK"))
			return "punch";
		if (f.equals("ARROW_FIRE"))
			return "flame";
		if (f.equals("ARROW_INFINITE"))
			return "infinity";
		if (f.equals("LUCK"))
			return "luck_of_the_sea";
		if (f.equals("LURE"))
			return "lure";
		if (f.equals("LOYALTY"))
			return "loyalty";
		if (f.equals("IMPALING"))
			return "impaling";
		if (f.equals("RIPTIDE"))
			return "riptide";
		if (f.equals("CHANNELING"))
			return "channeling";
		if (f.equals("MULTISHOT"))
			return "multishot";
		if (f.equals("QUICK_CHARGE"))
			return "quick_charge";
		if (f.equals("PIERCING"))
			return "piercing";
		if (f.equals("MENDING"))
			return "mending";
		if (f.equals("VANISHING_CURSE"))
			return "vanishing_curse";
		if (f.equals("SOUL_SPEED"))
			return "soul_speed";

		Debug.err("No Mapping found for " +f);
		Debug.err("PLEASE REPORT THIS ERROR");
		return f;
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
			return "death.attack.fallingBlock"; //"death.attack.anvil"
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
