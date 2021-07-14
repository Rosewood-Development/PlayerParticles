package dev.esophose.playerparticles.particles;

import org.bukkit.Location;

public class PParticle {

    /**
     * Data that determines where the particle will spawn
     */
    private Location location;
    private double speed;
    private double xOff, yOff, zOff;
    private boolean directional;
    private Object overrideData;
    private float size;

    /**
     * The constructor with all the fancy parameters and override data for customization
     *
     * @param location The location to display the particle at
     * @param xOff The offset for the x-axis
     * @param yOff The offset for the y-axis
     * @param zOff The offset for the z-axis
     * @param speed The speed the particle will move at
     * @param directional If the particle should use the x, y, and z offsets as directions instead
     * @param overrideData If not null, will override the player's set data on spawn
     */
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed, boolean directional, Object overrideData) {
        this.location = location;
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
        this.speed = speed;
        this.directional = directional;
        this.overrideData = overrideData;
    }
    /**
     * The constructor with all the fancy parameters and override data for customization
     *
     * @param location The location to display the particle at
     * @param xOff The offset for the x-axis
     * @param yOff The offset for the y-axis
     * @param zOff The offset for the z-axis
     * @param speed The speed the particle will move at
     * @param directional If the particle should use the x, y, and z offsets as directions instead
     * @param size Size of the redstone particle
     */
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed, boolean directional, float size) {
        this.location = location;
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
        this.speed = speed;
        this.directional = directional;
        this.size = size;
    }

    /**
     * The constructor with all the fancy parameters for customization
     * 
     * @param location The location to display the particle at
     * @param xOff The offset for the x-axis
     * @param yOff The offset for the y-axis
     * @param zOff The offset for the z-axis
     * @param speed The speed the particle will move at
     * @param directional If the particle should use the x, y, and z offsets as directions instead
     */
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed, boolean directional) {
        this(location, xOff, yOff, zOff, speed, directional, null);
    }
    
    /**
     * The constructor with all the fancy parameters for customization
     * 
     * @param location The location to display the particle at
     * @param xOff The offset for the x-axis
     * @param yOff The offset for the y-axis
     * @param zOff The offset for the z-axis
     * @param speed The speed the particle will move at
     */
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed) {
        this(location, xOff, yOff, zOff, speed, false, null);
    }

    /**
     * The constructor used if you just want stand-still particles
     * Useful for making shapes with the styles
     * 
     * @param location The location to display the particles at
     */
    public PParticle(Location location) {
        this(location, 0.0F, 0.0F, 0.0F, 0.0F, false, null);
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

        x += this.xOff * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());
        y += this.yOff * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());
        z += this.zOff * 1.75D * (Math.random() > 0.5 ? Math.random() : -Math.random());

        return new Location(this.location.getWorld(), x, y, z);
    }

    /**
     * Gets the speed of the particle
     * 
     * @return The particle's speed
     */
    public double getSpeed() {
        return this.speed;
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
    public double getXOff() {
        return this.xOff;
    }

    /**
     * Gets the size of the particle
     *
     * @return The size
     */
    public float getSize() {
        return this.size;
    }

    /**
     * Gets the offset on the y-axis for the particle
     * 
     * @return The y-axis offset
     */
    public double getYOff() {
        return this.yOff;
    }

    /**
     * Gets the offset on the z-axis for the particle
     * 
     * @return The z-axis offset
     */
    public double getZOff() {
        return this.zOff;
    }

    /**
     * Gets the data to override for the particle
     *
     * @return The data to override for the particle, may be null
     */
    public Object getOverrideData() {
        return this.overrideData;
    }

}
