package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleOverhead extends DefaultParticleStyle {

    public ParticleStyleOverhead() {
        super("overhead", true, false, -0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        particles.add(new PParticle(location.clone().add(0, 1.75, 0), 0.4F, 0.1F, 0.4F, 0.0F));
        particles.add(new PParticle(location.clone().add(0, 1.75, 0), 0.4F, 0.1F, 0.4F, 0.0F));
        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
