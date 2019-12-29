package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.PParticle;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSpin implements ParticleStyle {

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

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        double radius = .5;
        double newX = location.getX() + radius * cos[step];
        double newY = location.getY() + 1.5;
        double newZ = location.getZ() + radius * sin[step];
        return Collections.singletonList(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
    }

    public void updateTimers() {
        step = (step + 1) % maxSteps;
    }

    public String getName() {
        return "spin";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return true;
    }
    
    public double getFixedEffectOffset() {
        return -0.5;
    }

}
