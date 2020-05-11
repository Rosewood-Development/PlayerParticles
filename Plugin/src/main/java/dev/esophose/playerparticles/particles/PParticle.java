package dev.esophose.playerparticles.particles;

import org.bukkit.Location;

public class PParticle {

    /**
     * Data that determines where the particle will spawn
     */
    private Location location;
    private boolean directional;
    private float offsetX, offsetY, offsetZ;
    private float speed;

    /**
     * The constructor with all the fancy parameters for customization
     * 
     * @param location The location to display the particle at
     * @param offsetX The offset for the x-axis
     * @param offsetY The offset for the y-axis
     * @param offsetZ The offset for the z-axis
     * @param speed The speed the particle will move at
     * @param directional If the particle should use the x, y, and z offsets as directions instead
     */
    public PParticle(Location location, float offsetX, float offsetY, float offsetZ, float speed, boolean directional) {
        this.location = location;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.speed = speed;
        this.directional = directional;
    }
    
    /**
     * The constructor with all the fancy parameters for customization
     * 
     * @param location The location to display the particle at
     * @param offsetX The offset for the x-axis
     * @param offsetY The offset for the y-axis
     * @param offsetZ The offset for the z-axis
     * @param speed The speed the particle will move at
     */
    public PParticle(Location location, float offsetX, float offsetY, float offsetZ, float speed) {
        this(location, offsetX, offsetY, offsetZ, speed, false);
    }

    /**
     * The constructor used if you just want stand-still particles
     * Useful for making shapes with the styles
     * 
     * @param location The location to display the particles at
     */
    public PParticle(Location location) {
        this(location, 0.0F, 0.0F, 0.0F, 0.0F, false);
    }

    /**
     * Gets the location that the particle will be displayed at
     * Offsets must be applied manually if this is a colorable effect
     * 
     * @param colorable Whether or not this effect is colorable and we need to manually
     *            adjust for the offsets or not
     * @return The location, either as normal or modified with the offsets
     */
    public Location getLocation(boolean colorable) {
        if (!colorable)
            return this.location;

        double x = this.location.getX();
        double y = this.location.getY();
        double z = this.location.getZ();

        x += this.offsetX * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());
        y += this.offsetY * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());
        z += this.offsetZ * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());

        return new Location(this.location.getWorld(), x, y, z);
    }

    /**
     * Gets if the particle is directional
     * 
     * @return If the particle is directional
     */
    public boolean isDirectional() {
        return this.directional;
    }

    /**
     * Gets the offset on the x-axis for the particle
     * 
     * @return The x-axis offset
     */
    public float getOffsetX() {
        return this.offsetX;
    }

    /**
     * Gets the offset on the y-axis for the particle
     * 
     * @return The y-axis offset
     */
    public float getOffsetY() {
        return this.offsetY;
    }

    /**
     * Gets the offset on the z-axis for the particle
     * 
     * @return The z-axis offset
     */
    public float getOffsetZ() {
        return this.offsetZ;
    }

    /**
     * Gets the speed of the particle
     *
     * @return The particle's speed
     */
    public float getSpeed() {
        return this.speed;
    }

}
