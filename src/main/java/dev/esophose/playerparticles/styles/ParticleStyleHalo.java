package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleHalo extends DefaultParticleStyle {

    private int step = 0;

    public ParticleStyleHalo() {
        super("halo", true, false, -0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        if (this.step % 2 == 0)
            return particles;

        int points = 16;
        double radius = .65;
        double slice = 2 * Math.PI / points;

        for (int i = 0; i < points; i++) {
            double angle = slice * i;
            double dx = radius * Math.cos(angle);
            double dy = 1.5;
            double dz = radius * Math.sin(angle);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.step++;
        if (this.step > 30)
            this.step = 0;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
