package dev.esophose.playerparticles.particles.data;

import org.bukkit.Color;

/**
 * Represents the color of a particle effect
 */
public interface ParticleColor {

    /**
     * @return The offsetX value
     */
    float getValueX();

    /**
     * @return The offsetY value
     */
    float getValueY();

    /**
     * @return The offsetZ value
     */
    float getValueZ();

    /**
     * @return A Color representing this ParticleColor, may contain transparency if supported
     */
    Color toSpigot();

}
