package dev.esophose.playerparticles.event;

import dev.esophose.playerparticles.styles.ParticleStyle;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 * An event that gets called during the PlayerParticles style registration
 */
public class ParticleStyleRegistrationEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private Map<String, ParticleStyle> registeredStyles;
    private Map<String, ParticleStyle> registeredEventStyles;

    public ParticleStyleRegistrationEvent() {
        super(!Bukkit.isPrimaryThread());
        this.registeredStyles = new HashMap<>();
        this.registeredEventStyles = new HashMap<>();
    }

    /**
     * @return An unmodifiable map of registered styles keyed by the style internal name
     */
    public Map<String, ParticleStyle> getRegisteredStyles() {
        return Collections.unmodifiableMap(this.registeredStyles);
    }

    /**
     * @return An unmodifiable map of registered event styles keyed by the style internal name
     */
    public Map<String, ParticleStyle> getRegisteredEventStyles() {
        return Collections.unmodifiableMap(this.registeredEventStyles);
    }

    /**
     * Registers a ParticleStyle, overwriting any existing styles with the same name
     *
     * @param style The ParticleStyle to register
     * @return true if registered without replacing an existing style, false if an existing style was replaced
     */
    public boolean registerStyle(ParticleStyle style) {
        if (this.registeredEventStyles.containsKey(style.getInternalName())) {
            this.registeredEventStyles.remove(style.getInternalName());
            this.registeredStyles.put(style.getInternalName(), style);
            return false;
        }

        return this.registeredStyles.put(style.getInternalName(), style) == null;
    }

    /**
     * Registers an event-based ParticleStyle, overwriting any existing styles with the same name.
     * Styles registered with this method bypass the normal update loop, and must instead be spawned manually.
     *
     * @param style The ParticleStyle to register
     * @return true if registered without replacing an existing style, false if an existing style was replaced
     */
    public boolean registerEventStyle(ParticleStyle style) {
        if (this.registeredStyles.containsKey(style.getInternalName())) {
            this.registeredStyles.remove(style.getInternalName());
            this.registeredEventStyles.put(style.getInternalName(), style);
            return false;
        }

        return this.registeredEventStyles.put(style.getInternalName(), style) == null;
    }

    /**
     * Unregisters a ParticleStyle
     *
     * @param internalName The internal name of the ParticleStyle to unregister
     * @return true if a style was unregistered, false otherwise
     */
    public boolean unregisterStyle(String internalName) {
        return this.registeredStyles.remove(internalName) != null || this.registeredEventStyles.remove(internalName) != null;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

}
