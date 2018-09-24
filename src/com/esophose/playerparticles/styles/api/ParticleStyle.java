/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.styles.api;

import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;

public interface ParticleStyle {

    /**
     * Gets all the particles to display based on the style's logic
     * 
     * @param pplayer The PPlayer to display the particles for
     * @param location The central location of the particles
     * @return A List<PParticle> of PParticles to spawn
     */
    public List<PParticle> getParticles(ParticlePair particle, Location location);

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
