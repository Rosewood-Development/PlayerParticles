package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleThick extends DefaultParticleStyle {

    private int multiplier;

    public ParticleStyleThick() {
        super("thick", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < this.multiplier; i++)
            particles.addAll(DefaultStyles.NORMAL.getParticles(particle, location));

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("multiplier", 1, "The multiplier for the number of particles to spawn", "This style uses the same spawning as the 'normal' style");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.multiplier = config.getInt("multiplier");
    }

}