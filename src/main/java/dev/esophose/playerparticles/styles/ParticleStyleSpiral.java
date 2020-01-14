package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSpiral extends DefaultParticleStyle {

    private int stepX = 0;

    public ParticleStyleSpiral() {
        super("spiral", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int stepY = -60; stepY < 60; stepY += 10) {
            double dx = -(Math.cos(((this.stepX + stepY) / 90D) * Math.PI * 2)) * 0.8;
            double dy = stepY / 45D;
            double dz = -(Math.sin(((this.stepX + stepY) / 90D) * Math.PI * 2)) * 0.8;
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.stepX++;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
