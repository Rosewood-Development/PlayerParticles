/**
 * Copyright Esophose 2017
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.util;

import java.util.ArrayList;

import org.bukkit.Material;

public class ParticleUtils {

	/**
	 * Finds a block/item as a material from a string
	 * There must be some better way to do this that reliably gets the correct material
	 * 
	 * @param input The material name as a string
	 * @return The material from the string
	 */
	@SuppressWarnings("deprecation")
	public static Material closestMatch(String input) {
        ArrayList<Material> matchList = new ArrayList<Material>();
        for (Material material : Material.values())
            if (material.name().replaceAll("_", " ").toLowerCase().equals(input.toLowerCase()) || String.valueOf(material.getId()).equals(input))
                return material;
            else if (material.name().replaceAll("_", " ").toLowerCase().contains(input.toLowerCase()))
                matchList.add(material);
        
        if (matchList.size() == 1) return matchList.get(0);
        else return null;
    }
	
	/**
	 * Gets the smallest positive integer from an array
	 * 
	 * @param n The array containing non-available integers
	 * @return The smallest positive integer not in the given array
	 */
	public static int getSmallestPositiveInt(int[] n) {
		for (int i = 0; i < n.length; ++i) {
			while (n[i] != i + 1) {
				if (n[i] <= 0 || n[i] > n.length || n[i] == n[n[i] - 1]) break;
				int temp = n[i];
				n[i] = n[temp - 1];
				n[temp - 1] = temp;
			}
		}
		for (int i = 0; i < n.length; ++i)
			if (n[i] != i + 1) return i + 1;
		return n.length + 1;
	}

}
