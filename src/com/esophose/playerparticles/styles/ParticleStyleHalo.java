package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

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
        if (step % 2 == 0) return new ArrayList<PParticle>();

        int points = 16;
        double radius = .65;
        List<PParticle> particles = new ArrayList<PParticle>();
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

}
