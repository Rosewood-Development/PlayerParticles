package com.esophose.playerparticles.particles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.ParticleUtils;

public class PPlayer {

    /**
     * The UUID of the player
     */
    private final UUID playerUUID;

    /**
     * A List of ParticleGroups this player has, the active particle group has an id of null
     */
    private List<ParticleGroup> particleGroups;

    /**
     * A List of FixedParticleEffects this user has applied
     */
    private List<FixedParticleEffect> fixedParticles;

    /**
     * Constructs a new PPlayer
     * 
     * @param uuid The player UUID
     * @param particleGroups The ParticleGroups this PPlayer has
     * @param fixedParticles The FixedParticleEffects this PPlayer has
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
     * Get a List of ParticleGroups this user has saved
     * 
     * @return A List of ParticleGroups this player has
     */
    public List<ParticleGroup> getParticleGroups() {
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
     * @return A List of ParticlePairs this player has set
     */
    public List<ParticlePair> getActiveParticles() {
        return this.getActiveParticleGroup().getParticles();
    }

    /**
     * Get the PPlayer's active ParticleGroup
     * 
     * @return A ParticleGroup of this player's active particles
     */
    public ParticleGroup getActiveParticleGroup() {
        for (ParticleGroup group : this.particleGroups)
            if (group.getName().equals(ParticleGroup.DEFAULT_NAME)) 
                return group;
        throw new IllegalStateException("Active particle group does not exist for player with UUID: " + this.getUniqueId());
    }

    /**
     * Get all ParticlePairs with a style matching the input
     * 
     * @param style The style to match
     * @return A List of ParticlePairs with a matching style
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
     * @return A List of FixedParticleEffects this player has set
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

    /**
     * Gets a list of ids of all fixed effect this player has
     * 
     * @return A List of Integer ids this player's fixed effects have
     */
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
        for (FixedParticleEffect fixedEffect : this.fixedParticles) {
            if (fixedEffect.getId() == id) {
                this.fixedParticles.remove(fixedEffect);
                break;
            }
        }
    }

    /**
     * Gets the next Id for a player's fixed effects
     * 
     * @return The next available fixed effect id
     */
    public int getNextFixedEffectId() {
        List<Integer> fixedEffectIds = this.getFixedEffectIds();
        int[] ids = new int[fixedEffectIds.size()];
        for (int i = 0; i < fixedEffectIds.size(); i++)
            ids[i] = fixedEffectIds.get(i);
        return ParticleUtils.getSmallestPositiveInt(ids);
    }

    /**
     * Gets the next Id for a player's active particles
     * 
     * @return The next available active particle id
     */
    public int getNextActiveParticleId() {
        List<ParticlePair> activeParticles = this.getActiveParticles();
        int[] ids = new int[activeParticles.size()];
        for (int i = 0; i < activeParticles.size(); i++)
            ids[i] = activeParticles.get(i).getId();
        return ParticleUtils.getSmallestPositiveInt(ids);
    }

}
