package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.PlayerParticles;
import java.util.List;
import org.bukkit.Location;

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
     * @return true if the style is enabled, false otherwise
     */
    default boolean isEnabled() {
        return true;
    }

    /**
     * @return The style's internal name that will always remain constant
     */
    String getInternalName();

    /**
     * @return The name that the style will display to the users as
     */
    default String getName() {
        return this.getInternalName();
    }

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
    default boolean canToggleWithMovement() {
        return true;
    }
    
    /**
     * The Y-axis offset to be applied when using '/pp fixed create looking'
     * 
     * @return How far to move the style up or down to get it centered on the block properly
     */
    default double getFixedEffectOffset() {
        return 0;
    }

    /**
     * @return true if the particle should be seen from the fixed effect distance instead of the player distance, or false otherwise
     */
    default boolean hasLongRangeVisibility() {
        return false;
    }

    /**
     * Gets the ParticleStyle with the name given, returns null if not found
     * 
     * @param styleName The name of the style to search for
     * @return The ParticleStyle with a matching name
     */
    static ParticleStyle fromName(String styleName) {
        for (ParticleStyle style : PlayerParticles.getInstance().getManager(ParticleStyleManager.class).getStyles())
            if (style.getName().equalsIgnoreCase(styleName)) 
                return style;
        return null;
    }

    /**
     * Gets the ParticleStyle with the internal name given, returns null if not found
     *
     * @param styleName The internal name of the style to search for
     * @return The ParticleStyle with a matching name
     */
    static ParticleStyle fromInternalName(String styleName) {
        for (ParticleStyle style : PlayerParticles.getInstance().getManager(ParticleStyleManager.class).getStyles())
            if (style.getInternalName().equalsIgnoreCase(styleName))
                return style;
        return null;
    }

}