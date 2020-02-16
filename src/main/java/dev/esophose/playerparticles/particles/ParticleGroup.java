package dev.esophose.playerparticles.particles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.PermissionManager;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ParticleGroup {
    
    public static final String DEFAULT_NAME = "active";

    private String name;
    private Map<Integer, ParticlePair> particles;

    public ParticleGroup(String name, Map<Integer, ParticlePair> particles) {
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
     * Get the map of ParticlePairs in this group
     * 
     * @return The particles in this group
     */
    public Map<Integer, ParticlePair> getParticles() {
        return this.particles;
    }

    /**
     * Gets an empty ParticleGroup
     * 
     * @return The default empty active ParticleGroup
     */
    public static ParticleGroup getDefaultGroup() {
        return new ParticleGroup(DEFAULT_NAME, new ConcurrentHashMap<>());
    }

    /**
     * Checks if a player has permission to use this particle group
     *
     * @param player The player to check
     * @return True if the player has permission
     */
    public boolean canPlayerUse(PPlayer player) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        // Make sure the player has permission for the number of particles in this group
        if (permissionManager.getMaxParticlesAllowed(player) < this.particles.size())
            return false;

        // Make sure the player has permission for all effects/styles in the group
        for (ParticlePair particle : this.particles.values()) {
            if (!permissionManager.hasEffectPermission(player, particle.getEffect()))
                return false;

            if (!permissionManager.hasStylePermission(player, particle.getStyle()))
                return false;
        }

        return true;
    }

}
