package dev.esophose.playerparticles.styles.api;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;

public class ParticleStyleManager {

    /**
     * Arrays that contain all registered styles
     */
    private static List<ParticleStyle> styles = new ArrayList<>();
    private static List<ParticleStyle> customHandledStyles = new ArrayList<>();

    /**
     * Registers a style that is put into the plugin's update loop
     * 
     * @param style The style to add
     */
    public static void registerStyle(ParticleStyle style) {
        if (style == null) {
            PlayerParticles.getPlugin().getLogger().severe("Tried to register a null style");
            return;
        }
        
        if (style.getName() == null || style.getName().trim().equals("")) {
            PlayerParticles.getPlugin().getLogger().severe("Tried to register a style with a null or empty name: '" + style.getName() + "'");
            return;
        }
        
        for (ParticleStyle testAgainst : styles) {
            if (testAgainst.getName().equalsIgnoreCase(style.getName())) {
                PlayerParticles.getPlugin().getLogger().severe("Tried to register two styles with the same name spelling: '" + style.getName() + "'");
                return;
            } else if (testAgainst.equals(style)) {
                PlayerParticles.getPlugin().getLogger().severe("Tried to register the same style twice: '" + style.getName() + "'");
                return;
            }
        }
        
        styles.add(style);
    }

    /**
     * Registers a style that isn't updated on the normal update loop
     * 
     * @param style The style to register
     */
    public static void registerCustomHandledStyle(ParticleStyle style) {
        registerStyle(style);
        customHandledStyles.add(style);
    }

    /**
     * Returns if a given style is customly handled
     * 
     * @param style The style to check
     * @return If the style is handled in a custom manner
     */
    public static boolean isCustomHandled(ParticleStyle style) {
        return customHandledStyles.contains(style);
    }

    /**
     * Gets all registered styles
     * 
     * @return A List of ParticleStyles that are registered
     */
    public static List<ParticleStyle> getStyles() {
        return styles;
    }
    
    /**
     * Removes all styles from the registry
     * 
     * It is not recommended to call this
     */
    public static void reset() {
        styles.clear();
        customHandledStyles.clear();
    }

    /**
     * Updates all the timers for the particle styles to make the animations
     * 
     * Do not call this in your plugin, it will mess with other styles
     */
    public static void updateTimers() {
        for (ParticleStyle style : styles)
            style.updateTimers();
    }

}
