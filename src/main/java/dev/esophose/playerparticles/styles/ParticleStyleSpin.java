package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSpin extends DefaultParticleStyle {

    private static double[] cos, sin;
    private static final int maxSteps = 30;
    private int step = 0;
    
    static {
        cos = new double[maxSteps];
        sin = new double[maxSteps];
        
        int i = 0;
        for (double n = 0; n < Math.PI * 2; n += Math.PI * 2 / maxSteps) {
            cos[i] = Math.cos(n);
            sin[i] = Math.sin(n);
            i++;
        }
    }

    public ParticleStyleSpin() {
        super("spin", true, true, -0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        double radius = .5;
        double newX = location.getX() + radius * cos[this.step];
        double newY = location.getY() + 1.5;
        double newZ = location.getZ() + radius * sin[this.step];
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
