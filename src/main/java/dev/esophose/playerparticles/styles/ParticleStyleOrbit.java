package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleOrbit extends DefaultParticleStyle {

    private static double[] cos, sin;
    private static final int orbs = 3;
    private static final int numSteps = 120;
    private int step = 0;
    
    static {
        cos = new double[120];
        sin = new double[120];
        
        int i = 0;
        for (double n = 0; n < numSteps; n++) {
            cos[i] = -Math.cos(n / numSteps * Math.PI * 2);
            sin[i] = -Math.sin(n / numSteps * Math.PI * 2);
            i++;
        }
    }

    public ParticleStyleOrbit() {
        super("orbit", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < orbs; i++) {
            double dx = cos[(step + (numSteps / orbs * i)) % numSteps];
            double dz = sin[(step + (numSteps / orbs * i)) % numSteps];
            particles.add(new PParticle(location.clone().add(dx, 0, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        step++;
        if (step > numSteps) {
            step = 0;
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
