/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.esophose.playerparticles.library.ParticleEffect;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class PermissionManager {

	/**
	 * Checks if a player has permission to use an effect
	 * Always returns true for 'none'
	 * 
	 * @param player The player to check the permission for
	 * @param effect The effect to check
	 * @return True if the player has permission to use the effect
	 */
	public static boolean hasEffectPermission(Player player, ParticleEffect effect) {
		if (player.hasPermission("playerparticles.*") || player.hasPermission("playerparticles.effect.*")) return true;
		if (player.hasPermission("playerparticles.effect." + effect.getName().toLowerCase().replace("_", ""))) return true;
		if (effect == ParticleEffect.NONE) return true;
		return false;
	}

	/**
	 * Checks if a player has permission to use a style
	 * Always returns true for 'none' so they can be reset
	 * 
	 * @param player The player to check the permission for
	 * @param effect The style to check
	 * @return True if the player has permission to use the style
	 */
	public static boolean hasStylePermission(Player player, ParticleStyle style) {
		if (player.hasPermission("playerparticles.*") || player.hasPermission("playerparticles.style.*")) return true;
		if (player.hasPermission("playerparticles.style." + style.getName().toLowerCase().replace("_", ""))) return true;
		if (style == DefaultStyles.NONE) return true;
		return false;
	}

	/**
	 * Gets a List<String> of all effect names a player has permission for
	 * 
	 * @param p The player to get effect names for
	 * @return A List<String> of all effect names the given player has permission for
	 */
	public static List<String> getParticlesUserHasPermissionFor(Player p) {
		List<String> list = new ArrayList<String>();
		for (ParticleEffect pe : ParticleEffect.getSupportedEffects()) {
			if (hasEffectPermission(p, pe)) list.add(pe.getName().toLowerCase().replace("_", ""));
		}
		return list;
	}

	/**
	 * Gets a List<String> of all style names a player has permission for
	 * 
	 * @param p The player to get style names for
	 * @return A List<String> of all style names the given player has permission for
	 */
	public static List<String> getStylesUserHasPermissionFor(Player p) {
		List<String> list = new ArrayList<String>();
		for (ParticleStyle ps : ParticleStyleManager.getStyles()) {
			if (hasStylePermission(p, ps)) list.add(ps.getName().toLowerCase().replace("_", ""));
		}
		return list;
	}

	public static boolean canExecuteForOthers(Player player) {
		return player.hasPermission("playerparticles.other");
	}

}
