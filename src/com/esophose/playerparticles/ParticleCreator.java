/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.awt.Color;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.library.ParticleEffect;
import com.esophose.playerparticles.library.ParticleEffect.NoteColor;
import com.esophose.playerparticles.library.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.library.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class ParticleCreator extends BukkitRunnable implements Listener {
	
	/**
	 * How far away particles will spawn from players
	 */
	public static final int PARTICLE_RANGE = 512;

	/**
	 * The list containing all the player effect info
	 */
	public static ArrayList<PPlayer> particlePlayers = new ArrayList<PPlayer>();
	
	/**
	 * Rainbow particle effect hue and note color used for rainbow colorable effects
	 * These should be moved to a more appropriate place later
	 */
	private static int hue = 0;
	private static int note = 0;

	/**
	 * Adds the player to the array when they join
	 * 
	 * @param e The event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		ConfigManager.getInstance().getPPlayer(e.getPlayer().getUniqueId());
	}

	/**
	 * Removes the player from the array when they log off
	 * 
	 * @param e The event
	 */
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		particlePlayers.remove(ConfigManager.getInstance().getPPlayer(e.getPlayer().getUniqueId()));
	}

	/**
	 * Clears the list then adds everybody on the server
	 * Used for when the server reloads and we can't rely on players rejoining
	 */
	public static void refreshPPlayers() {
		particlePlayers.clear();
		for (Player player : Bukkit.getOnlinePlayers()) {
			ConfigManager.getInstance().getPPlayer(player.getUniqueId());
		}
	}

	/**
	 * Overrides an existing PPlayer with the same UUID
	 * 
	 * @param pplayer The PPlayer to override
	 */
	public static void updateIfContains(PPlayer pplayer) {
		for (PPlayer pp : particlePlayers) {
			if (pp.getUniqueId() == pplayer.getUniqueId()) {
				particlePlayers.remove(pp);
				particlePlayers.add(pplayer);
				break;
			}
		}
	}

	/**
	 * Gets a particle type from a string, used for getting ParticleType's from the saved data
	 * 
	 * @param particle The name of the particle to check for
	 * @return The ParticleType with the given name, will be null if name was not found
	 */
	public static ParticleEffect particleFromString(String particle) {
		for (ParticleEffect effect : ParticleEffect.getSupportedEffects()) {
			if (effect.getName().toLowerCase().replace("_", "").equals(particle.toLowerCase())) return effect;
		}
		return null;
	}

	/**
	 * The main loop to display all the particles
	 * Does not display particles if the world is disabled or if the player is in spectator mode
	 */
	public void run() {
		ParticleStyleManager.updateTimers();
		
		hue++;
		hue %= 360;
		
		if (hue % 10 == 0) { // Only increment note by 2 notes per second
			note++;
			note %= 24;
		}
		
		for (PPlayer pplayer : particlePlayers) {
			Player player = Bukkit.getPlayer(pplayer.getUniqueId());
			
			if (!PermissionManager.hasEffectPermission(player, pplayer.getParticleEffect())) continue;
			if (ConfigManager.getInstance().isWorldDisabled(player.getWorld().getName())) continue;
			if (player.getGameMode() == GameMode.SPECTATOR) continue;
			
			Location loc = player.getLocation();
			loc.setY(loc.getY() + 1);
			displayParticles(pplayer, loc);
		}
	}

	/**
	 * Displays particles at the given player location with their settings
	 * 
	 * @param pplayer The PPlayer to use for getting particle settings
	 * @param location The location to display at
	 */
	private void displayParticles(PPlayer pplayer, Location location) {
		if (!ParticleStyleManager.isCustomHandled(pplayer.getParticleStyle())) {
			ParticleEffect effect = pplayer.getParticleEffect();
			if (effect == ParticleEffect.NONE) return;
			for (PParticle particle : pplayer.getParticleStyle().getParticles(pplayer, location)) {
				if (effect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
					effect.display(pplayer.getParticleSpawnData(), particle.getXOff(), particle.getYOff(), particle.getZOff(), particle.getSpeed(), 1, particle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), PARTICLE_RANGE);
				} else if (effect.hasProperty(ParticleProperty.COLORABLE)) {
					effect.display(pplayer.getParticleSpawnColor(), particle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), PARTICLE_RANGE);
				} else {
					effect.display(particle.getXOff(), particle.getYOff(), particle.getZOff(), particle.getSpeed(), 1, particle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), PARTICLE_RANGE);
				}
			}
		}
	}

	/**
	 * An alternative method used for custom handled styles
	 * 
	 * @param pplayer The PPlayer to use for getting particle settings
	 * @param location The location to display at
	 */
	public static void displayParticles(PPlayer pplayer, PParticle[] particles) {
		ParticleEffect effect = pplayer.getParticleEffect();
		if (effect == ParticleEffect.NONE) return;
		for (PParticle particle : particles) {
			if (effect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
				effect.display(pplayer.getParticleSpawnData(), particle.getXOff(), particle.getYOff(), particle.getZOff(), particle.getSpeed(), 1, particle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), PARTICLE_RANGE);
			} else if (effect.hasProperty(ParticleProperty.COLORABLE)) {
				effect.display(pplayer.getParticleSpawnColor(), particle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), PARTICLE_RANGE);
			} else {
				effect.display(particle.getXOff(), particle.getYOff(), particle.getZOff(), particle.getSpeed(), 1, particle.getLocation(effect.hasProperty(ParticleProperty.COLORABLE)), PARTICLE_RANGE);
			}
		}
	}
	
	public static OrdinaryColor getRainbowParticleColor() {
		Color rgb = Color.getHSBColor(hue / 360F, 1.0F, 1.0F);
		return new OrdinaryColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
	}
	
	public static NoteColor getRainbowNoteParticleColor() {
		return new NoteColor(note);
	}

}
