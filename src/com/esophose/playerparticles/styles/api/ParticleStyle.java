/**
 * Copyright Esophose 2017
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.styles.api;

import org.bukkit.Location;

import com.esophose.playerparticles.PPlayer;

public interface ParticleStyle {

	/**
	 * Gets all the particles to display based on the style's logic
	 * 
	 * @param pplayer The PPlayer to display the particles for
	 * @param location The central location of the particles
	 * @return A list of all PParticles' to spawn
	 */
	public PParticle[] getParticles(PPlayer pplayer, Location location);

	/**
	 * Used to update timers for animations, called once per particle tick
	 */
	public void updateTimers();

	/**
	 * The name of the style
	 * 
	 * @return The style's name
	 */
	public String getName();
	
	/**
	 * Gets if the style can be used in a FixedParticleEffect
	 * 
	 * @return If the style can be used in a FixedParticleEffect
	 */
	public boolean canBeFixed();

}
