package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.PParticle;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleHalo implements ParticleStyle {

    private static double[] cos, sin;
    private static final int points = 16;
    private int step = 0;
    
    static {
        cos = new double[points];
        sin = new double[points];
        
        int i = 0;
        for (double n = 0; n < Math.PI * 2; n += Math.PI * 2 / points) {
            cos[i] = Math.cos(n);
            sin[i] = Math.sin(n);
            i++;
        }
    }

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        if (step % 2 == 0) return new ArrayList<>();

        int points = 16;
        double radius = .65;
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double dx = radius * cos[i];
            double dy = 1.5;
            double dz = radius * sin[i];
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    public void updateTimers() {
        step++;
        if (step > 30) {
            step = 0;
        }
    }

    public String getName() {
        return "halo";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return false;
    }
    
    public double getFixedEffectOffset() {
        return -0.5;
    }

}
