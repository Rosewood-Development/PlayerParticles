package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleThick extends DefaultParticleStyle {

    public ParticleStyleThick() {
        super("thick", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> baseParticles = DefaultStyles.NORMAL.getParticles(particle, location);

        int multiplyingFactor = 10; // Uses the same logic as ParticleStyleNormal except multiplies the resulting particles by 10x
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < baseParticles.size() * multiplyingFactor; i++) {
            particles.add(baseParticles.get(i % baseParticles.size()));
        }

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
