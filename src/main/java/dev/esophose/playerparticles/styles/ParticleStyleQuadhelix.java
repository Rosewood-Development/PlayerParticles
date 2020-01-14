package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleQuadhelix extends DefaultParticleStyle {

    private static final int orbs = 4;
    private static int maxStepX = 80;
    private static int maxStepY = 60;
    private int stepX = 0;
    private int stepY = 0;
    private boolean reverse = false;

    public ParticleStyleQuadhelix() {
        super("quadhelix", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < orbs; i++) {
            double dx = -(Math.cos((this.stepX / (double) maxStepX) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i))) * ((maxStepY - Math.abs(this.stepY)) / (double) maxStepY);
            double dy = (this.stepY / (double) maxStepY) * 1.5;
            double dz = -(Math.sin((this.stepX / (double) maxStepX) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i))) * ((maxStepY - Math.abs(this.stepY)) / (double) maxStepY);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.stepX++;
        if (this.stepX > maxStepX) {
            this.stepX = 0;
        }
        if (this.reverse) {
            this.stepY++;
            if (this.stepY > maxStepY)
                this.reverse = false;
        } else {
            this.stepY--;
            if (this.stepY < -maxStepY)
                this.reverse = true;
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
