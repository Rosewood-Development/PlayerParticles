package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSphere extends DefaultParticleStyle {

    public ParticleStyleSphere() {
        super("sphere", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        int density = 15;
        double radius = 1.5f;
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < density; i++) {
            double u = Math.random();
            double v = Math.random();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);
            double dx = radius * MathL.sin(phi) * MathL.cos(theta);
            double dy = radius * MathL.sin(phi) * MathL.sin(theta);
            double dz = radius * MathL.cos(phi);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
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
