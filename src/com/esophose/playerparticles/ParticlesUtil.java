/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.util.ArrayList;

import org.bukkit.Material;

public class ParticlesUtil {

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
        for (Material mat : Material.values())
            if (mat.name().replace("_", " ").toLowerCase().equals(input.toLowerCase()) || String.valueOf(mat.getId()).equals(input))
                return mat;
            else if (mat.name().replace("_", " ").toLowerCase().contains(input.toLowerCase()))
                matchList.add(mat);
        
        if (matchList.size() == 1) return matchList.get(0);
        else return null;
    }

}
