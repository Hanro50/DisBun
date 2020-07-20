package org.han.xlib.types;
/*
 * Copyright 2020 Hanro
 * 
 * All subsequent copies of this code should contain this header in addition to any other license information.
 * 
 * If no other license information is provided by original author then:
 * All rights reserved by original author
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;
/**
 * Bidirectional MultiMap. For use with many-to-many relationships
 * Warning: DO NOT USE THIS IN A MULTITHREADED APP. IT'S NOT DESIGNED FOR THAT AND MIGHT BREAK IN WEIRD WAYS
 * 
 * @author hanro50
 * @version 1.0
 */
public class DiMultiMap<k1, k2> {
	@Expose
	Map<k1, List<k2>> k1tok2;
	@Expose
	Map<k2, List<k1>> k2tok1;

	public DiMultiMap() {
		k1tok2 = new HashMap<k1, List<k2>>();
		k2tok1 = new HashMap<k2, List<k1>>();
	}

	public boolean haskey1(k2 value) {
		return k2tok1.containsKey(value);
	}
	
	public k1 Getkey1First(k2 value) {
		return Getkey1(value).get(0);
	}
	
	public List<k1> Getkey1(k2 value) {
		if (k2tok1.containsKey(value)) {
			return k2tok1.get(value);
		}
		return null;
	}
	
	public Map<k1, List<k2>> GetKey1Map(){
		Map<k1, List<k2>> ret =  new HashMap<k1, List<k2>>();
		ret.putAll(k1tok2);
		return ret;
	}

	public List<k1> Getkey1default(k2 value, List<k1> def) {
		List<k1> T = Getkey1(value);
		if (T == null)
			return def;
		return T;
	}
	
	public boolean haskey2(k1 value) {
		return k1tok2.containsKey(value);
	}

	public k2 Getkey2First(k1 value) {
		return Getkey2(value).get(0);
	}
	
	public List<k2> Getkey2(k1 value) {
		if (k1tok2.containsKey(value)) {
			return k1tok2.get(value);
		}
		return null;
	}
	
	public Map<k2, List<k1>> GetKey2Map(){
		Map<k2, List<k1>> ret =  new HashMap<k2, List<k1>>();
		ret.putAll(k2tok1);
		return ret;
	}

	public List<k2> Getkey2default(k1 value, List<k2> def) {
		List<k2> T = Getkey2(value);
		if (T == null)
			return def;
		return T;
	}

	public void put(k1 key1, k2 key2) {
		add(key1,key2);
	}
	
	public void add(k1 key1, k2 key2) {
		if (!k1tok2.containsKey(key1) || k1tok2.get(key1) == null) {
			k1tok2.put(key1, new ArrayList<k2>());
		}
		if (!k1tok2.get(key1).contains(key2))
			k1tok2.get(key1).add(key2);
		if (!k2tok1.containsKey(key2) || k2tok1.get(key2) == null) {
			k2tok1.put(key2, new ArrayList<k1>());
		}
		if (!k2tok1.get(key2).contains(key1))
			k2tok1.get(key2).add(key1);
	}
	
	public void remove(k1 key1, k2 key2) {
		k1tok2.get(key1).remove(key2);
		k2tok1.get(key2).remove(key1);
	}
	

	public void removeKey1(k1 value) {
		if (k1tok2.containsKey(value)) {
			List<k2> T = k1tok2.get(value);
			k1tok2.remove(value);
			for (k2 k2 : T) {
				if (k2tok1.get(k2) != null) {
					k2tok1.get(k2).remove(value);
				}
			}
		}
	}

	public void removeKey2(k2 value) {
		if (k2tok1.containsKey(value)) {
			List<k1> T = k2tok1.get(value);
			k2tok1.remove(value);
			for (k1 k1 : T) {
				if (k1tok2.get(k1) != null) {
					k1tok2.get(k1).remove(value);
				}
			}
		}
	}

}
