package dev.esophose.playerparticles.particles;

import org.bukkit.Location;

public class PParticle {

    /**
     * Data that determines where the particle will spawn
     */
    private final Location location;
    private final double speed;
    private final double xOff, yOff, zOff;
    private final boolean directional;
    private final ParticleEffect overrideEffect;
    private final Object overrideData;
    private final float size;

    //#region Deprecated Constructors

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
     * @deprecated Use {@link PParticle#builder(Location)} instead
     */
    @Deprecated
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed, boolean directional, Object overrideData) {
        this(location, xOff, yOff, zOff, speed, directional, 0, null, overrideData);
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
     * @deprecated Use {@link PParticle#builder(Location)} instead
     */
    @Deprecated
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed, boolean directional, float size) {
        this(location, xOff, yOff, zOff, speed, directional, size, null, null);
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
     * @deprecated Use {@link PParticle#builder(Location)} instead
     */
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed, boolean directional) {
        this(location, xOff, yOff, zOff, speed, directional, 0, null, null);
    }
    
    /**
     * The constructor with all the fancy parameters for customization
     * 
     * @param location The location to display the particle at
     * @param xOff The offset for the x-axis
     * @param yOff The offset for the y-axis
     * @param zOff The offset for the z-axis
     * @param speed The speed the particle will move at
     * @deprecated Use {@link PParticle#builder(Location)} instead
     */
    public PParticle(Location location, double xOff, double yOff, double zOff, double speed) {
        this(location, xOff, yOff, zOff, speed, false, 0, null, null);
    }

    /**
     * The constructor used if you just want stand-still particles
     * Useful for making shapes with the styles
     * 
     * @param location The location to display the particles at
     * @deprecated Use {@link PParticle#builder(Location)} instead
     */
    public PParticle(Location location) {
        this(location, 0.0F, 0.0F, 0.0F, 0.0F, false, 0, null, null);
    }

    //#endregion

    protected PParticle(Location location, double xOff, double yOff, double zOff, double speed, boolean directional,
                        float size, ParticleEffect overrideEffect, Object overrideData) {
        this.location = location;
        this.xOff = xOff;
        this.yOff = yOff;
        this.zOff = zOff;
        this.speed = speed;
        this.directional = directional;
        this.size = size;
        this.overrideEffect = overrideEffect;
        this.overrideData = overrideData;
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
     * Gets the size of the particle
     *
     * @return The size, or 0 if not applicable
     */
    public float getSize() {
        return this.size;
    }

    /**
     * Gets the effect to override for the particle
     *
     * @return The effect to override for the particle, may be null
     */
    public ParticleEffect getOverrideEffect() {
        return this.overrideEffect;
    }

    /**
     * Gets the data to override for the particle
     *
     * @return The data to override for the particle, may be null
     */
    public Object getOverrideData() {
        return this.overrideData;
    }

    /**
     * Creates a new PParticle Builder
     *
     * @param location The location of the particle
     * @return The PParticle Builder
     */
    public static PParticle.Builder builder(Location location) {
        return new PParticle.Builder(location);
    }

    /**
     * Creates a new simple PParticle at a given Location
     *
     * @param location The location of the particle
     * @return The PParticle
     */
    public static PParticle point(Location location) {
        return builder(location).build();
    }

    public static class Builder {

        private final Location location;
        private double speed;
        private double xOff, yOff, zOff;
        private boolean directional;
        private float size;
        private ParticleEffect overrideEffect;
        private Object overrideData;

        public Builder(Location location) {
            this.location = location;
        }

        /**
         * Sets the speed of the particle
         *
         * @param speed The speed of the particle
         * @return The builder
         */
        public Builder speed(double speed) {
            this.speed = speed;
            return this;
        }

        public Builder offsets(double xOff, double yOff, double zOff) {
            this.xOff = xOff;
            this.yOff = yOff;
            this.zOff = zOff;
            return this;
        }

        /**
         * Sets the particle to be directional
         *
         * @return The builder
         */
        public Builder directional() {
            this.directional = true;
            return this;
        }

        /**
         * Sets the size of the particle
         *
         * @param size The size of the particle
         * @return The builder
         */
        public Builder size(float size) {
            this.size = size;
            return this;
        }

        /**
         * Sets the particle effect to override for the particle
         *
         * @param overrideEffect The particle effect to override for the particle
         * @return The builder
         */
        public Builder overrideEffect(ParticleEffect overrideEffect) {
            this.overrideEffect = overrideEffect;
            return this;
        }

        /**
         * Sets the data to override for the particle
         *
         * @param overrideData The data to override for the particle
         * @return The builder
         */
        public Builder overrideData(Object overrideData) {
            this.overrideData = overrideData;
            return this;
        }

        /**
         * Creates a new PParticle
         *
         * @return The PParticle
         */
        public PParticle build() {
            return new PParticle(this.location, this.xOff, this.yOff, this.zOff, this.speed, this.directional, this.size, this.overrideEffect, this.overrideData);
        }

    }

}
