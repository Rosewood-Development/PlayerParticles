package dev.esophose.playerparticles.particles;

import java.util.UUID;
import org.bukkit.Location;

public class FixedParticleEffect {

    /**
     * The UUID of the player who owns this effect
     */
    private UUID pplayerUUID;

    /**
     * The ID of this effect, unique to the owner's UUID
     */
    private int id;

    /**
     * The location for this effect to be displayed
     */
    private Location location;

    /**
     * The effect and style this effect uses
     */
    private ParticlePair particlePair;

    /**
     * Constructs a new FixedParticleEffect
     * FixedParticleEffects can NOT use event styles
     * 
     * @param pplayerUUID The UUID of the player who owns the effect
     * @param id The id this effect has, unique to the owner pplayer
     * @param location The location to display the effect at
     * @param particlePair The ParticlePair that represents this FixedParticleEffect's appearance
     */
    public FixedParticleEffect(UUID pplayerUUID, int id, Location location, ParticlePair particlePair) {
        this.pplayerUUID = pplayerUUID;
        this.id = id;
        this.particlePair = particlePair;
        this.location = location.clone();
    }

    /**
     * Gets the owner of the effect's UUID
     * 
     * @return The owner of the effect's UUID
     */
    public UUID getOwnerUniqueId() {
        return this.pplayerUUID;
    }

    /**
     * Gets the id unique to the owner's UUID
     * 
     * @return This effect's id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the ParticlePair, which contains all spawn information about this fixed effect
     * 
     * @return The ParticlePair that represents this FixedParticleEffect's appearance
     */
    public ParticlePair getParticlePair() {
        return this.particlePair;
    }

    /**
     * Gets the location this effect will be displayed at
     * 
     * @return The effect's location
     */
    public Location getLocation() {
        return this.location.clone();
    }
    
    /**
     * Updates the coordinates of the FixedParticleEffect
     * 
     * @param x The new X coordinate
     * @param y The new Y coordinate
     * @param z The new Z coordinate
     */
    public void setCoordinates(double x, double y, double z) {
        this.location.setX(x);
        this.location.setY(y);
        this.location.setZ(z);
    }

}
