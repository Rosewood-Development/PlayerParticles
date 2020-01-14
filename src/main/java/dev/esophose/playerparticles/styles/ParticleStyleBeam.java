package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleBeam extends DefaultParticleStyle {

    private int step = 0;
    private boolean reversed = false;

    public ParticleStyleBeam() {
        super("beam", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        int points = 16;
        double radius = 1;
        double slice = 2 * Math.PI / points;

        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double angle = slice * i;
            double newX = location.getX() + radius * MathL.cos(angle);
            double newY = location.getY() + (this.step / 10D) - 1;
            double newZ = location.getZ() + radius * MathL.sin(angle);
            particles.add(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.step += this.reversed ? -1 : 1;

        if (this.step >= 30) {
            this.reversed = true;
        } else if (this.step <= 0) {
            this.reversed = false;
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
