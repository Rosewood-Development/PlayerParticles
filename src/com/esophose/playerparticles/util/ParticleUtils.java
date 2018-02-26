/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.util;

import org.bukkit.Material;

public class ParticleUtils {

	/**
	 * Finds a block/item as a material from a string
	 * 
	 * @param input The material name as a string
	 * @return The material from the string
	 */
	public static Material closestMatch(String input) {
		if (input == null) return null;
		for (Material material : Material.values()) // First check for exact matches
		    if (material.name().equalsIgnoreCase(input) ||
	        	material.toString().equalsIgnoreCase(input)) return material;
        for (Material material : Material.values()) // Then check for partial matches
            if (material.name().toLowerCase().contains(input.toLowerCase()) || 
        		material.toString().toLowerCase().contains(input.toLowerCase()) ||
        		material.name().replaceAll("_", "").toLowerCase().contains(input.toLowerCase()) || 
        		material.toString().replaceAll("_", "").toLowerCase().contains(input.toLowerCase())) 
            	return material;
        return null;
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
