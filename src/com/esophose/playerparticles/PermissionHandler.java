/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.esophose.playerparticles.libraries.particles.ParticleEffect.ParticleType;

public class PermissionHandler {

	public static boolean hasPermission(Player player, ParticleType effect) {
		if(player.hasPermission("playerparticles.*") || player.hasPermission("playerparticles.particles.*")) return true;
		if(effect.equals(ParticleType.RED_DUST) && player.hasPermission("playerparticles.reddust")) {
			return true;
		}else{
			if(effect.equals(ParticleType.RED_DUST)) return false;
		}
		if(effect.equals(ParticleType.RAINBOW) && player.hasPermission("playerparticles.rainbow")) {
			return true; 
		}else{
			if(effect.equals(ParticleType.RAINBOW)) return false;
		}
		if(player.hasPermission("playerparticles." + effect.getName().toLowerCase().replace("_", ""))) return true;
		return false;
	}
	
	public static boolean hasStylePermission(Player player, ParticleStyle style) {
		if(player.hasPermission("playerparticles.*") || player.hasPermission("playerparticles.styles.*") || style == ParticleStyle.NONE) return true;
		if(player.hasPermission("playerparticles.style." + style.toString().toLowerCase().replace("_", ""))) return true;
		return false;
	}
	
	public static List<String> getParticlesUserHasPermissionFor(Player p) {
		List<String> list = new ArrayList<String>();
		if(p.hasPermission("playerparticles.*") || p.hasPermission("playerparticles.particles.*")) {
			for(ParticleType pt : ParticleType.values()) {
				list.add(pt.toString().toLowerCase().replace("_", ""));
			}
		}else{
			for(ParticleType pt : ParticleType.values()) {
				if(p.hasPermission("playerparticles." + pt.toString().toLowerCase().replace("_", ""))) list.add(pt.toString().toLowerCase().replace("_", ""));
			}
		}
		return list;
	}
	
	public static List<String> getStylesUserHasPermissionFor(Player p) {
		List<String> list = new ArrayList<String>();
		if(p.hasPermission("playerparticles.*") || p.hasPermission("playerparticles.styles.*")) {
			for(ParticleStyle ps : ParticleStyle.values()) {
				list.add(ps.toString().toLowerCase());
			}
		}else{
			for(ParticleStyle pt : ParticleStyle.values()) {
				if(p.hasPermission("playerparticles.style." + pt.toString().toLowerCase())) list.add(pt.toString().toLowerCase());
			}
		}
		return list;
	}
	
}
