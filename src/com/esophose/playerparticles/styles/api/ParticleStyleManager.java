/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.styles.api;

import java.util.ArrayList;

public class ParticleStyleManager {

    /**
     * Arrays that contain all registered styles
     */
    private static ArrayList<ParticleStyle> styles = new ArrayList<ParticleStyle>();
    private static ArrayList<ParticleStyle> customHandledStyles = new ArrayList<ParticleStyle>();

    /**
     * Registers a style that is put into the plugin's update loop
     * 
     * @param style The style to add
     */
    public static void registerStyle(ParticleStyle style) {
        for (ParticleStyle testAgainst : styles) {
            if (testAgainst.getName().replace("_", "").equalsIgnoreCase(style.getName())) {
                throw new ParticleStyleAlreadyRegisteredException("Tried to register two styles with the same name!");
            } else if (testAgainst.equals(style)) {
                throw new ParticleStyleAlreadyRegisteredException("Tried to register the same style twice!");
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
     * @return An ArrayList of all registered styles
     */
    public static ArrayList<ParticleStyle> getStyles() {
        return styles;
    }

    /**
     * Gets the ParticleStyle with the name given, returns null if not found
     * 
     * @param styleName The string of the style to search for
     * @return The ParticleStyle with the name requested
     */
    public static ParticleStyle styleFromString(String styleName) {
        for (ParticleStyle style : styles)
            if (style.getName().toLowerCase().replace("_", "").equals(styleName)) return style;
        return null;
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

    /**
     * The exception to throw if a style is already registered
     */
    private static final class ParticleStyleAlreadyRegisteredException extends RuntimeException {

        private static final long serialVersionUID = -6116170395810178020L;

        private ParticleStyleAlreadyRegisteredException(String message) {
            super(message);
        }

    }

}
