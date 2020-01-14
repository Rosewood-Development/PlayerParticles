package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSpin extends DefaultParticleStyle {

    private static final int maxSteps = 30;
    private int step = 0;

    public ParticleStyleSpin() {
        super("spin", true, true, -0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        double slice = (Math.PI * 2 / maxSteps) * this.step;

        double radius = .5;
        double newX = location.getX() + radius * MathL.cos(slice);
        double newY = location.getY() + 1.5;
        double newZ = location.getZ() + radius * MathL.sin(slice);
        return Collections.singletonList(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % maxSteps;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
