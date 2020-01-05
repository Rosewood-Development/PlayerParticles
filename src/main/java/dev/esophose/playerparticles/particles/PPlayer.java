package dev.esophose.playerparticles.particles;

import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PPlayer {

    /**
     * The UUID of the player
     */
    private final UUID playerUUID;

    /**
     * A map of ParticleGroups this player has, the active particle group has an id of null
     */
    private Map<String, ParticleGroup> particleGroups;

    /**
     * A map of FixedParticleEffects this user has applied
     */
    private Map<Integer, FixedParticleEffect> fixedParticles;
    
    /**
     * If True, the player will not see any particles spawned by the plugin
     */
    private boolean particlesHidden;
    
    /**
     * If the player is moving
     */
    private boolean isMoving;

    /**
     * Constructs a new PPlayer
     * 
     * @param uuid The player UUID
     * @param particleGroups The ParticleGroups this PPlayer has
     * @param fixedParticles The FixedParticleEffects this PPlayer has
     * @param particlesHidden If this player has all particles hidden from view
     */
    public PPlayer(UUID uuid, Map<String, ParticleGroup> particleGroups, Map<Integer, FixedParticleEffect> fixedParticles, boolean particlesHidden) {
        this.playerUUID = uuid;
        this.particleGroups = particleGroups;
        this.fixedParticles = fixedParticles;
        
        this.particlesHidden = particlesHidden;
        this.isMoving = false;
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
     * Gets the destination for messages
     * 
     * @return The destination for messages
     */
    public CommandSender getMessageDestination() {
        return this.getPlayer();
    }
    
    /**
     * Gets if the Player can see particles spawned by the plugin or not
     * 
     * @return True if the player can see particles, otherwise false
     */
    public boolean canSeeParticles() {
        return !this.particlesHidden;
    }
    
    /**
     * Sets if the player can see particles spawned by the plugin or not
     * 
     * @param hidden True if the player can see particles, otherwise false
     */
    public void setParticlesHidden(boolean hidden) {
        this.particlesHidden = hidden;
    }

    /**
     * Get a map of ParticleGroups this user has saved
     * 
     * @return A List of ParticleGroups this player has
     */
    public Map<String, ParticleGroup> getParticleGroups() {
        return this.particleGroups;
    }
    
    /**
     * Sets the player's movement state
     * 
     * @param isMoving True if the player is moving, otherwise false if they are standing still
     */
    public void setMoving(boolean isMoving) {
        this.isMoving = isMoving;
    }
    
    /**
     * Gets if a player is moving
     * 
     * @return True if the player is moving
     */
    public boolean isMoving() {
        return this.isMoving;
    }

    /**
     * Gets a ParticleGroup this player has by its name
     * 
     * @param name The name of the ParticleGroup
     * @return The target named ParticleGroup
     */
    public ParticleGroup getParticleGroupByName(String name) {
        return this.particleGroups.get(name.toLowerCase());
    }

    /**
     * Get the effect/style/data for particles this player has set
     * 
     * @return A List of ParticlePairs this player has set
     */
    public Collection<ParticlePair> getActiveParticles() {
        return this.getActiveParticleGroup().getParticles().values();
    }

    /**
     * Get the PPlayer's active ParticleGroup
     * 
     * @return A ParticleGroup of this player's active particles
     */
    public ParticleGroup getActiveParticleGroup() {
        return this.particleGroups.get(ParticleGroup.DEFAULT_NAME);
    }

    /**
     * Get all ParticlePairs with a style matching the input
     * 
     * @param style The style to match
     * @return A List of ParticlePairs with a matching style
     */
    public Set<ParticlePair> getActiveParticlesForStyle(ParticleStyle style) {
        return this.getActiveParticles().stream().filter(x -> x.getStyle().equals(style)).collect(Collectors.toSet());
    }

    /**
     * Get a ParticlePair by its id
     * 
     * @param id The id of the ParticlePair
     * @return A ParticlePair with the given id, otherwise null
     */
    public ParticlePair getActiveParticle(int id) {
        return this.getActiveParticleGroup().getParticles().get(id);
    }

    /**
     * Get the effect/style/data for all fixed particles this has has set
     * 
     * @return A collection of FixedParticleEffects this player has set
     */
    public Collection<FixedParticleEffect> getFixedParticles() {
        return this.fixedParticles.values();
    }

    /**
     * Get a map of the effect/style/data for all fixed particles this has has set
     *
     * @return A map of FixedParticleEffects this player has set
     */
    public Map<Integer, FixedParticleEffect> getFixedParticlesMap() {
        return this.fixedParticles;
    }

    /**
     * Get a FixedParticleEffect this player owns by id
     * 
     * @param id The id
     * @return The FixedParticleEffect the player owns
     */
    public FixedParticleEffect getFixedEffectById(int id) {
        return this.fixedParticles.get(id);
    }

    /**
     * Gets a set of ids of all fixed effect this player has
     * 
     * @return A set of Integer ids this player's fixed effects have
     */
    public Set<Integer> getFixedEffectIds() {
        return this.fixedParticles.keySet();
    }

    /**
     * Adds a fixed effect
     * 
     * @param fixedEffect The fixed effect to add
     */
    public void addFixedEffect(FixedParticleEffect fixedEffect) {
        this.fixedParticles.put(fixedEffect.getId(), fixedEffect);
    }

    /**
     * Removes a fixed effect for the given pplayer with the given id
     * 
     * @param id The id of the fixed effect to remove
     */
    public void removeFixedEffect(int id) {
        this.fixedParticles.remove(id);
    }

    /**
     * Gets the next Id for a player's fixed effects
     * 
     * @return The next available fixed effect id
     */
    public int getNextFixedEffectId() {
        return ParticleUtils.getSmallestPositiveInt(this.fixedParticles.keySet().stream().mapToInt(Integer::intValue).toArray());
    }

    /**
     * Gets the next Id for a player's active particles
     * 
     * @return The next available active particle id
     */
    public int getNextActiveParticleId() {
        return ParticleUtils.getSmallestPositiveInt(this.getActiveParticles().stream().mapToInt(ParticlePair::getId).toArray());
    }
    
    /**
     * Gets the primary particle (ID 1) for the PPlayer
     * 
     * @return The particle with an ID of 1 for the PPlayer or a new one if one doesn't exist
     */
    public ParticlePair getPrimaryParticle() {
        ParticlePair primaryParticle = this.getActiveParticle(1);
        if (primaryParticle == null)
            primaryParticle = ParticlePair.getNextDefault(this);
        return primaryParticle;
    }

}
