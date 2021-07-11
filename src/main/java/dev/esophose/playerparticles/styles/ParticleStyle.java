package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

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
     * Gets all the particles to display based on the style's logic
     *
     * @param particle The ParticlePair that contains the particle's data
     * @param location The central location of the particles
     * @param player   The player that the particles are spawning on - null for fixed effects
     * @return A List of PParticles to spawn
     */
    default List<PParticle> getParticles(ParticlePair particle, Location location, Player player) {
        return null;
    }

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
     * @return The Material icon that represents this style in the GUI
     */
    default Material getGuiIconMaterial() {
        return ParticleUtils.FALLBACK_MATERIAL;
    }

    /**
     * Gets if the style can be used in a FixedParticleEffect
     * 
     * @return If the style can be used in a FixedParticleEffect
     */
    boolean canBeFixed();
    
    /**
     * Gets if the style can be displayed differently based on the toggle-on-move setting when the player is moving
     * 
     * @return True if it can be, otherwise False
     */
    default boolean canToggleWithMovement() {
        return true;
    }

    /**
     * Gets if the style can be hidden if the player is in combat with the toggle-on-combat setting
     *
     * @return True if it can be, otherwise False
     */
    default boolean canToggleWithCombat() {
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
        return PlayerParticles.getInstance().getManager(ParticleStyleManager.class).getStyleByName(styleName);
    }

    /**
     * Gets the ParticleStyle with the internal name given, returns null if not found
     *
     * @param styleName The internal name of the style to search for
     * @return The ParticleStyle with a matching name
     */
    static ParticleStyle fromInternalName(String styleName) {
        return PlayerParticles.getInstance().getManager(ParticleStyleManager.class).getStyleByInternalName(styleName);
    }

}
