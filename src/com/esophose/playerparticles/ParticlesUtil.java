package com.esophose.playerparticles;

import java.util.ArrayList;

import org.bukkit.Material;

public class ParticlesUtil {

	// TODO: Find a more reliable way of doing this that works better
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
