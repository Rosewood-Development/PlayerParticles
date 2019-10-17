package dev.esophose.playerparticles.particles;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

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
     * FixedParticleEffects can NOT use custom handled styles
     * 
     * @param pplayerUUID The UUID of the player who owns the effect
     * @param id The id this effect has, unique to the owner pplayer
     * @param worldName The world name this effect will be displayed in
     * @param xPos The X position in the world
     * @param yPos The Y position in the world
     * @param zPos The Z position in the world
     * @param particlePair The ParticlePair that represents this FixedParticleEffect's appearance
     */
    public FixedParticleEffect(UUID pplayerUUID, int id, String worldName, double xPos, double yPos, double zPos, ParticlePair particlePair) {
        this.pplayerUUID = pplayerUUID;
        this.id = id;
        this.particlePair = particlePair;

        World world = Bukkit.getWorld(worldName);
        if (world == null) { // Default to the first world in case it doesn't exist
            world = Bukkit.getWorlds().get(0); // All servers will have at least one world
        }

        this.location = new Location(world, xPos, yPos, zPos);
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
