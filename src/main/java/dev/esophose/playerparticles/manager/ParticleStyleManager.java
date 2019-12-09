package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.api.ParticleStyle;
import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;

public class ParticleStyleManager extends Manager {

    /**
     * Arrays that contain all registered styles
     */
    private List<ParticleStyle> styles = new ArrayList<>();
    private List<ParticleStyle> customHandledStyles = new ArrayList<>();

    public ParticleStyleManager(PlayerParticles playerParticles) {
        super(playerParticles);

        DefaultStyles.registerStyles(this);
    }

    @Override
    public void reload() {
        // Styles List is never reset so you don't need to re-register styles each time the plugin reloads
    }

    @Override
    public void disable() {

    }

    /**
     * Registers a style that is put into the plugin's update loop
     * 
     * @param style The style to add
     */
    public void registerStyle(ParticleStyle style) {
        if (style == null) {
            PlayerParticles.getInstance().getLogger().severe("Tried to register a null style");
            return;
        }
        
        if (style.getName() == null || style.getName().trim().equals("")) {
            PlayerParticles.getInstance().getLogger().severe("Tried to register a style with a null or empty name: '" + style.getName() + "'");
            return;
        }
        
        for (ParticleStyle testAgainst : this.styles) {
            if (testAgainst.getName().equalsIgnoreCase(style.getName())) {
                PlayerParticles.getInstance().getLogger().severe("Tried to register two styles with the same name spelling: '" + style.getName() + "'");
                return;
            } else if (testAgainst.equals(style)) {
                PlayerParticles.getInstance().getLogger().severe("Tried to register the same style twice: '" + style.getName() + "'");
                return;
            }
        }
        
        this.styles.add(style);
    }

    /**
     * Registers a style that isn't updated on the normal update loop
     * 
     * @param style The style to register
     */
    public void registerCustomHandledStyle(ParticleStyle style) {
        this.registerStyle(style);
        this.customHandledStyles.add(style);
    }

    /**
     * Returns if a given style is customly handled
     * 
     * @param style The style to check
     * @return If the style is handled in a custom manner
     */
    public boolean isCustomHandled(ParticleStyle style) {
        return this.customHandledStyles.contains(style);
    }

    /**
     * Gets all registered styles
     * 
     * @return A List of ParticleStyles that are registered
     */
    public List<ParticleStyle> getStyles() {
        return this.styles;
    }

    /**
     * Updates all the timers for the particle styles to make the animations
     * 
     * Do not call this in your plugin, it will mess with other styles
     */
    public void updateTimers() {
        for (ParticleStyle style : this.styles)
            style.updateTimers();
    }

}
