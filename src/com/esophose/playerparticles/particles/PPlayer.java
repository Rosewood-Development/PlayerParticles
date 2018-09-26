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
     * A List<ParticleGroup> of all particle groups this player has, the active particle group has an id of null
     */
    private List<ParticleGroup> particleGroups;
    
    /**
     * A List<FixedParticleEffect> of all fixed particles this user has applied
     */
    private List<FixedParticleEffect> fixedParticles;

    /**
     * Constructs a new PPlayer
     * 
     * @param uuid The player UUID
	 * @param particlePairs The ParticlePairs this PPlayer has
     * @param fixedParticles2 The fixed ParticlePairs this PPlayer has
     */
    public PPlayer(UUID uuid, List<ParticleGroup> particleGroups, List<FixedParticleEffect> fixedParticles) {
        this.playerUUID = uuid;
        this.particleGroups = particleGroups;
        this.fixedParticles = fixedParticles;
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
     * Get a List<ParticleGroup> of all particles this user has saved
     * 
     * @return A list of all particle groups this player has
     */
    public List<ParticleGroup> getParticles() {
    	return this.particleGroups;
    }
    
    /**
     * Gets a ParticleGroup this player has by its name
     * 
     * @param name The name of the ParticleGroup
     * @return The target named ParticleGroup
     */
    public ParticleGroup getParticlesByName(String name) {
    	for (ParticleGroup group : this.particleGroups)
    		if (group.getName().equalsIgnoreCase(name))
    			return group;
    	return null;
    }
    
    /**
     * Get the effect/style/data for particles this player has set
     * 
     * @return A List<ParticlePair> of all particles this player has set
     */
    public List<ParticlePair> getActiveParticles() {
    	for (ParticleGroup group : this.particleGroups)
    		if (group.getName() == null)
    			return group.getParticles();
    	return null; // This should never return null, there will always be at least one ParticleGroup
    }
    
    /**
     * Get all ParticlePairs with a style matching the input
     * 
     * @param style The style to match
     * @return A List<ParticlePair> with a matching style
     */
    public List<ParticlePair> getActiveParticlesForStyle(ParticleStyle style) {
    	List<ParticlePair> matches = new ArrayList<ParticlePair>();
    	for (ParticlePair pair : this.getActiveParticles())
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
    public ParticlePair getActiveParticle(int id) {
    	for (ParticlePair particle : this.getActiveParticles())
    		if (particle.getId() == id)
    			return particle;
    	return null;
    }
    
    /**
     * Get the effect/style/data for all fixed particles this has has set
     * 
     * @return A List<FixedParticleEffect> of all fixed particles this player has set
     */
    public List<FixedParticleEffect> getFixedParticles() {
    	return this.fixedParticles;
    }
    
    /**
     * Get a FixedParticleEffect this player owns by id
     * 
     * @param id The id
     * @return The FixedParticleEffect the player owns
     */
    public FixedParticleEffect getFixedEffectById(int id) {
    	for (FixedParticleEffect fixedEffect : this.fixedParticles)
    		if (fixedEffect.getId() == id)
    			return fixedEffect;
    	return null;
    }
    
    public List<Integer> getFixedEffectIds() {
    	List<Integer> ids = new ArrayList<Integer>();
    	for (FixedParticleEffect fixedEffect : this.fixedParticles)
    		ids.add(fixedEffect.getId());
    	return ids;
    }

    /**
     * Adds a fixed effect
     * 
     * @param fixedEffect The fixed effect to add
     */
    public void addFixedEffect(FixedParticleEffect fixedEffect) {
        this.fixedParticles.add(fixedEffect);
    }

    /**
     * Removes a fixed effect for the given pplayer with the given id
     * 
     * @param id The id of the fixed effect to remove
     */
    public void removeFixedEffect(int id) {
        for (int i = this.fixedParticles.size() - 1; i >= 0; i--) 
            if (this.fixedParticles.get(i).getId() == id) 
            	this.fixedParticles.remove(i);
    }

}
