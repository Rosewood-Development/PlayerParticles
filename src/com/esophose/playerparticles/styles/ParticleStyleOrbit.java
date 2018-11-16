package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleOrbit implements ParticleStyle {

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

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < orbs; i++) {
            double dx = cos[(step + (numSteps / orbs * i)) % numSteps];
            double dz = sin[(step + (numSteps / orbs * i)) % numSteps];
            particles.add(new PParticle(location.clone().add(dx, 0, dz)));
        }
        return particles;
    }

    public void updateTimers() {
        step++;
        if (step > numSteps) {
            step = 0;
        }
    }

    public String getName() {
        return "orbit";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return true;
    }

}
