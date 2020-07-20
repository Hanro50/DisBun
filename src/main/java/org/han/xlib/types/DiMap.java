package org.han.xlib.types;
import java.util.ArrayList;
/*
 * Copyright 2020 Hanro
 * 
 * All subsequent copies of this code should contain this header in addition to any other license information.
 * 
 * If no other license information is provided by original author then:
 * All rights reserved by original author
 */
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.Expose;

/**
 * Bidirectional Map. For use with one-to-one relationships
 * Warning: DO NOT USE THIS IN A MULTITHREADED APP. IT'S NOT DESIGNED FOR THAT AND MIGHT BREAK IN WEIRD WAYS
 * 
 * @author hanro50
 * @version 1.0
 */
public class DiMap<k1, k2> {
	@Expose
	Map<k1, k2> k1tok2;
	@Expose
	Map<k2, k1> k2tok1;
	/**
	 * Create the map
	 */
	public List<k2> getlistk2(){
		List<k2> T = new ArrayList<k2>();
		T.addAll(k2tok1.keySet());
		return T;
	}
	
	public List<k1> getlistk1(){
		List<k1> T = new ArrayList<k1>();
		T.addAll(k1tok2.keySet());
		return T;
	}
	
	public DiMap() {
		k1tok2 = new HashMap<k1, k2>();
		k2tok1 = new HashMap<k2, k1>();
	}

	public boolean haskey1(k2 value) {
		return k2tok1.containsKey(value);
	}

	public k1 Getkey1(k2 value) {
		if (k2tok1.containsKey(value)) {
			return k2tok1.get(value);
		}
		return null;
	}

	public Map<k1, k2> GetKey1Map() {
		Map<k1, k2> ret = new HashMap<k1, k2>();
		ret.putAll(k1tok2);
		return ret;
	}

	public k1 Getkey1default(k2 value, k1 def) {
		k1 T = Getkey1(value);
		if (T == null)
			return def;
		return T;
	}

	public boolean haskey2(k1 value) {
		return k1tok2.containsKey(value);
	}

	public k2 Getkey2(k1 value) {
		if (k1tok2.containsKey(value)) {
			return k1tok2.get(value);
		}
		return null;
	}

	public Map<k2, k1> GetKey2Map() {
		Map<k2, k1> ret = new HashMap<k2, k1>();
		ret.putAll(k2tok1);
		return ret;
	}

	public k2 Getkey2default(k1 value, k2 def) {
		k2 T = Getkey2(value);
		if (T == null)
			return def;
		return T;
	}

	public void removeKey1(k1 value) {
		if (k1tok2.containsKey(value)) {
			k2 T = k1tok2.get(value);
			k1tok2.remove(value);
			k2tok1.remove(T);
		}
	}

	public void removeKey2(k2 value) {
		if (k2tok1.containsKey(value)) {
			k1 T = k2tok1.get(value);
			k2tok1.remove(value);
			k1tok2.remove(T);
		}
	}
	
	public void put(k1 key1, k2 key2) {
		add(key1,key2);
	}
	
	public void add(k1 key1, k2 key2) {
		if (k1tok2.containsKey(key1)) {
			removeKey1(key1);
		}
		if (k2tok1.containsKey(key2)) {
			removeKey2(key2);
		}
		
		k1tok2.put(key1,key2);
		k2tok1.put(key2,key1);
	}
}
