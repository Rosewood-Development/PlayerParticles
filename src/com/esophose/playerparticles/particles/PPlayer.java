/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.styles.api.ParticleStyle;

public class PPlayer {

    /**
     * The UUID of the player
     */
    private final UUID playerUUID;

    /**
     * A List<ParticlePair> of all particles the user has applied
     */
    private List<ParticlePair> particles;

    /**
     * Constructs a new PPlayer
     * 
     * @param uuid The player UUID
	 * @param particlePairs The ParticlePairs this PPlayer has
     */
    public PPlayer(UUID uuid, List<ParticlePair> particlePairs) {
        this.playerUUID = uuid;
        this.particles = particlePairs;
    }

    /**
     * Gets the player's UUID
     * 
     * @return The player's UUID
     */
    public UUID getUniqueId() {
        return this.playerUUID;
    }

    /**
     * Gets the Player from their UUID
     * 
     * @return The player if they are online, null if they are offline
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerUUID);
    }
    
    /**
     * Get the effect/style/data for particles this player has set
     * 
     * @return 
     */
    public List<ParticlePair> getParticles() {
    	return this.particles;
    }
    
    /**
     * Get all ParticlePairs with a style matching the input
     * 
     * @param style The style to match
     * @return A List<ParticlePair> with a matching style
     */
    public List<ParticlePair> getParticlesForStyle(ParticleStyle style) {
    	List<ParticlePair> matches = new ArrayList<ParticlePair>();
    	for (ParticlePair pair : this.particles)
    		if (pair.getStyle().equals(style))
    			matches.add(pair);
    	return matches;
    }
    
    /**
     * Get a ParticlePair by its id
     * 
     * @param id The id of the ParticlePair
     * @return A ParticlePair with the given id, otherwise null
     */
    public ParticlePair getParticle(int id) {
    	for (ParticlePair particle : this.particles)
    		if (particle.getId() == id)
    			return particle;
    	return null;
    }

    /**
     * Gets a default PPlayer
     * Used for when a new PPlayer is being created
     * 
     * @param playerUUID The player's UUID
     * @return A default PPlayer
     */
    public static PPlayer getNewPPlayer(UUID playerUUID) {
        return new PPlayer(playerUUID, new ArrayList<ParticlePair>());
    }

}
