package org.han.mc.spigot.module;

import java.util.Map;
import java.util.UUID;

import org.han.xlib.FileObj;
import org.han.xlib.types.vectors.vector3;

import com.google.gson.annotations.Expose;

public class DeathMessage {
	@Expose
	public vector3<Double> Deathpos;
	@Expose
	public String DeathMsg;
	@Expose
	public String MobName;
	@Expose
	public UUID Victem;
	@Expose
	public UUID Attacker;
	@Expose 
	public float XP;
	@Expose 
	public String Weapon;
	@Expose 
	public Map<String,Integer> Enchantments;
	@Expose 
	public long Creationdate;

	
	
	public DeathMessage() {

	}

	public DeathMessage(vector3<Double> pos, String DM, String MobName, UUID Victem, UUID Attacker,float XP, String Weapon,Map<String,Integer> Enchantments) {
		this.Deathpos = pos;
		this.DeathMsg = (DM);
		this.MobName = MobName;
		this.Victem = Victem;
		this.Attacker = Attacker;
		this.XP = XP;
		this.Weapon = Weapon;
		this.Creationdate = System.nanoTime();
		this.Enchantments = Enchantments;
	}

	public String encode() {
		return FileObj.tojson(this);
	}

	public static DeathMessage decode(String Encodedval) {
		return FileObj.fromjson(Encodedval, DeathMessage.class);

		// DamageCause.CONTACT
	}



}
