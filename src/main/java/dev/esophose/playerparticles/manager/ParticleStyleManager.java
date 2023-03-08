package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.event.ParticleStyleRegistrationEvent;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.styles.ConfiguredParticleStyle;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import org.bukkit.Bukkit;

public class ParticleStyleManager extends Manager {

    /**
     * Arrays that contain all registered styles
     */
    private final Map<String, ParticleStyle> stylesByName;
    private final Map<String, ParticleStyle> stylesByInternalName;
    private final List<ParticleStyle> eventStyles;

    public ParticleStyleManager(RosePlugin playerParticles) {
        super(playerParticles);

        this.stylesByName = new HashMap<>();
        this.stylesByInternalName = new HashMap<>();
        this.eventStyles = new ArrayList<>();

        DefaultStyles.initStyles();
    }

    @Override
    public void reload() {
        this.stylesByName.clear();
        this.stylesByInternalName.clear();
        this.eventStyles.clear();

        // Run task a tick later to allow other plugins to finish registering to the event
        Bukkit.getScheduler().runTask(this.rosePlugin, () -> {
            // Call registration event
            // We use this event internally, so no other action needs to be done for us to register the default styles
            ParticleStyleRegistrationEvent event = new ParticleStyleRegistrationEvent();

            // Register styles from particle packs
            this.rosePlugin.getManager(ParticlePackManager.class).getLoadedParticlePacks().forEach(pack -> {
                pack.getStyles().forEach(event::registerStyle);
                pack.getEventStyles().forEach(event::registerEventStyle);
            });

            // Register default styles
            DefaultStyles.registerStyles(event);

            // Call event for other plugins to register styles
            Bukkit.getPluginManager().callEvent(event);

            Collection<ParticleStyle> eventStyles = event.getRegisteredEventStyles().values();
            List<ParticleStyle> styles = new ArrayList<>(event.getRegisteredStyles().values());
            styles.addAll(eventStyles);

            for (ParticleStyle style : styles) {
                try {
                    if (style == null)
                        throw new IllegalArgumentException("Tried to register a null style");

                    if (style.getInternalName() == null || style.getInternalName().trim().isEmpty())
                        throw new IllegalArgumentException("Tried to register a style with a null or empty name: '" + style.getInternalName() + "'");

                    if (this.stylesByName.containsValue(style))
                        throw new IllegalArgumentException("Tried to register the same style twice: '" + style.getInternalName() + "'");

                    if (this.stylesByInternalName.containsKey(style.getInternalName().toLowerCase()))
                        throw new IllegalArgumentException("Tried to register two styles with the same internal name spelling: '" + style.getInternalName() + "'");

                    if (style instanceof ConfiguredParticleStyle)
                        ((ConfiguredParticleStyle) style).loadSettings();

                    this.stylesByName.put(style.getName().toLowerCase(), style);
                    this.stylesByInternalName.put(style.getInternalName().toLowerCase(), style);

                    if (eventStyles.contains(style))
                        this.eventStyles.add(style);
                } catch (IllegalArgumentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void disable() {

    }

    /**
     * Removes all references of a ParticleStyle from all PPlayers
     *
     * @param style The style to remove
     */
    public void removeAllStyleReferences(ParticleStyle style) {
        Collection<PPlayer> pplayers = this.rosePlugin.getManager(ParticleManager.class).getPPlayers().values();
        for (PPlayer pplayer : pplayers) {
            // Remove all references to style from groups
            pplayer.getParticleGroups().values().removeIf(group -> {
                group.getParticles().values().removeIf(particle -> particle.getStyle().equals(style));
                return group.getParticles().isEmpty() && !group.getName().equals(ParticleGroup.DEFAULT_NAME);
            });

            // Remove all references to style from fixed effects
            pplayer.getFixedParticlesMap().values().removeIf(x -> x.getParticlePair().getStyle().equals(style));
        }
    }

    /**
     * Returns if a given style is customly handled
     * 
     * @param style The style to check
     * @return If the style is handled in a custom manner
     */
    public boolean isEventHandled(ParticleStyle style) {
        return this.eventStyles.contains(style);
    }

    /**
     * @return A List of styles that are registered, enabled, and sorted by name
     */
    public List<ParticleStyle> getStyles() {
        return this.stylesByName.entrySet().stream()
                .filter(x -> x.getValue().isEnabled())
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    /**
     * @return all registered styles, regardless if they are enabled or not
     */
    public Collection<ParticleStyle> getStylesWithDisabled() {
        return this.stylesByName.values();
    }

    /**
     * Gets a registered ParticleStyle by its name
     *
     * @param name The name of the ParticleStyle
     * @return The ParticleStyle, or null if not found
     */
    public ParticleStyle getStyleByName(String name) {
        ParticleStyle style = this.stylesByName.get(name.toLowerCase());
        if (style != null && !style.isEnabled())
            style = null;
        return style;
    }

    /**
     * Gets a registered ParticleStyle by its internal name
     *
     * @param internalName The internal name of the ParticleStyle
     * @return The ParticleStyle, or null if not found
     */
    public ParticleStyle getStyleByInternalName(String internalName) {
        ParticleStyle style = this.stylesByInternalName.get(internalName.toLowerCase());
        if (style != null && !style.isEnabled())
            style = null;
        return style;
    }

    /**
     * Updates all the timers for the particle styles to make the animations
     * 
     * Do not call this in your plugin, it will mess with other styles
     */
    public void updateTimers() {
        this.stylesByName.values().forEach(ParticleStyle::updateTimers);
    }

}
