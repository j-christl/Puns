package me.inplex.puns.gfx;

import java.util.ConcurrentModificationException;
import java.util.HashMap;

import me.inplex.puns.entity.Drop;

public class DropStage {

	private static HashMap<Drop, Integer> map = new HashMap<Drop, Integer>();

	public static void update() {
		try {
			HashMap<Drop, Integer> _map = map;
			for (Drop d : _map.keySet()) {
				if (_map.get(d) > 100 || _map.get(d) < -100) {
					map.remove(d);
				}
			}
		} catch (ConcurrentModificationException e) {
		}
	}

	public static void addDrop(Drop d) {
		map.put(d, 0);
	}

	public static void removeDrop(Drop d) {
		map.remove(d);
	}

	public static int getTime(Drop d) {
		return map.get(d);
	}
	
	public static HashMap<Drop, Integer> getMap() {
		return map;
	}

}