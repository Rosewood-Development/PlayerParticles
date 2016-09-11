/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.styles.api;

import org.bukkit.Location;

import com.esophose.playerparticles.PPlayer;

public interface ParticleStyle {

	public PParticle[] getParticles(PPlayer pplayer, Location location);

	public void updateTimers();

	public String getName();

}
