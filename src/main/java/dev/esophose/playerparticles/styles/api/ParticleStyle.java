package dev.esophose.playerparticles.styles.api;

import java.util.List;

import org.bukkit.Location;

import dev.esophose.playerparticles.particles.ParticlePair;

public interface ParticleStyle {

    /**
     * Gets all the particles to display based on the style's logic
     * 
     * @param particle The ParticlePair that contains the particle's data
     * @param location The central location of the particles
     * @return A List of PParticles to spawn
     */
    List<PParticle> getParticles(ParticlePair particle, Location location);

    /**
     * Used to update timers for animations, called once per particle tick
     */
    void updateTimers();

    /**
     * The name of the style
     * 
     * @return The style's name
     */
    String getName();

    /**
     * Gets if the style can be used in a FixedParticleEffect
     * 
     * @return If the style can be used in a FixedParticleEffect
     */
    boolean canBeFixed();
    
    /**
     * Gets if the style can be replaced with DefaultStyles.FEET when the player is moving
     * 
     * @return True if it can be, otherwise False
     */
    boolean canToggleWithMovement();
    
    /**
     * The Y-axis offset to be applied when using '/pp fixed create looking'
     * 
     * @return How far to move the style up or down to get it centered on the block properly
     */
    double getFixedEffectOffset();
    
    /**
     * Gets the ParticleStyle with the name given, returns null if not found
     * 
     * @param styleName The name of the style to search for
     * @return The ParticleStyle with a matching name
     */
    static ParticleStyle fromName(String styleName) {
        for (ParticleStyle style : ParticleStyleManager.getStyles())
            if (style.getName().equalsIgnoreCase(styleName)) 
                return style;
        return null;
    }

}
