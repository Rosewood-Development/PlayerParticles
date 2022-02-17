package dev.esophose.playerparticles.pack;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.styles.ParticleStyle;
import java.net.URLClassLoader;
import java.util.List;
import org.bukkit.configuration.file.FileConfiguration;

public abstract class ParticlePack {

    protected PlayerParticles playerParticles;
    private ParticlePackDescription description;
    private URLClassLoader classLoader;

    private void init(PlayerParticles playerParticles, FileConfiguration particlePackConfig, URLClassLoader classLoader) {
        this.playerParticles = playerParticles;
        this.description = new ParticlePackDescription(particlePackConfig);
        this.classLoader = classLoader;

        this.onEnable();
    }

    public abstract void onEnable();

    public abstract List<ParticleStyle> getStyles();

    public abstract List<ParticleStyle> getEventStyles();

    public final int getNumberOfStyles() {
        return this.getStyles().size() + this.getEventStyles().size();
    }

    public final String getName() {
        return this.description.getName();
    }

    public final ParticlePackDescription getDescription() {
        return this.description;
    }

    public final URLClassLoader getClassLoader() {
        return this.classLoader;
    }

}
