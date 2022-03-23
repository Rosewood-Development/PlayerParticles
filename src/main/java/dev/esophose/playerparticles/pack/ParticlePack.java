package dev.esophose.playerparticles.pack;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ParticlePackManager;
import dev.esophose.playerparticles.styles.ParticleStyle;
import java.io.File;
import java.net.URLClassLoader;
import java.util.List;

public abstract class ParticlePack {

    protected PlayerParticles playerParticles;
    private ParticlePackDescription description;
    private URLClassLoader classLoader;

    /**
     * @return The list of styles registered by this particle pack
     */
    public abstract List<ParticleStyle> getStyles();

    /**
     * @return The list of event-based styles registered by this particle pack
     */
    public abstract List<ParticleStyle> getEventStyles();

    /**
     * Called when the pack is enabled, does nothing by default
     */
    public void onEnable() {

    }

    /**
     * @return The total number of styles returned by {@link #getStyles()} and {@link #getEventStyles()}
     */
    public final int getNumberOfStyles() {
        return this.getStyles().size() + this.getEventStyles().size();
    }

    /**
     * @return The name of the pack, shorthand for {@link ParticlePackDescription#getName()}
     */
    public final String getName() {
        return this.description.getName();
    }

    /**
     * @return The description of the pack
     */
    public final ParticlePackDescription getDescription() {
        return this.description;
    }

    /**
     * @return The classloader of the pack
     */
    public final URLClassLoader getClassLoader() {
        return this.classLoader;
    }

    /**
     * @return The directory of the pack's configuration files
     */
    public final File getConfigDirectory() {
        return new File(PlayerParticles.getInstance().getDataFolder(), ParticlePackManager.PACK_DIRECTORY_NAME + File.separator + this.getName());
    }

    /**
     * Called reflectively from the ParticlePackManager when loaded
     *
     * @param playerParticles The plugin instance
     * @param description The description of the pack
     * @param classLoader The classloader of the pack
     */
    private void init(PlayerParticles playerParticles, ParticlePackDescription description, URLClassLoader classLoader) {
        this.playerParticles = playerParticles;
        this.description = description;
        this.classLoader = classLoader;

        this.onEnable();
    }

}
