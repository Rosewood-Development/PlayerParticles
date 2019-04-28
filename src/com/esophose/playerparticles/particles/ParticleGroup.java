package com.esophose.playerparticles.particles;

import java.util.ArrayList;
import java.util.List;

public class ParticleGroup {
    
    public static final String DEFAULT_NAME = "active";

    private String name;
    private List<ParticlePair> particles;

    public ParticleGroup(String name, List<ParticlePair> particles) {
        this.name = name;
        this.particles = particles;
    }
    
    /**
     * Get the player-given name of this ParticleGroup
     * This will be null if it's the player's active ParticleGroup
     * 
     * @return The name of this group
     */
    public String getName() {
        return this.name;
    }

    /**
     * Get the List of ParticlePairs in this group
     * 
     * @return The particles in this group
     */
    public List<ParticlePair> getParticles() {
        return this.particles;
    }

    /**
     * Gets an empty ParticleGroup
     * 
     * @return The default empty active ParticleGroup
     */
    public static ParticleGroup getDefaultGroup() {
        return new ParticleGroup(DEFAULT_NAME, new ArrayList<>());
    }

}
