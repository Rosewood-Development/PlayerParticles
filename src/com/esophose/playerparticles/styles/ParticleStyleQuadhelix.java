package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleQuadhelix implements ParticleStyle {

    private static double[] cos, sin;
    private static final int orbs = 4;
    private static int maxStepX = 80;
    private static int maxStepY = 60;
    private int stepX = 0;
    private int stepY = 0;
    private boolean reverse = false;
    
    static {
        cos = new double[maxStepX];
        sin = new double[maxStepX];
        
        int i = 0;
        for (double n = 0; n < maxStepX; n++) {
            cos[i] = -Math.cos(n / maxStepX * Math.PI * 2);
            sin[i] = -Math.sin(n / maxStepX * Math.PI * 2);
            i++;
        }
    }

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < orbs; i++) {
            int step = (stepX + (maxStepX / orbs) * i) % maxStepX;
            double dx = cos[step] * ((60 - Math.abs(stepY)) / (double)maxStepY);
            double dy = (stepY / (double)maxStepY) * 1.5;
            double dz = sin[step] * ((60 - Math.abs(stepY)) / (double)maxStepY);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    public void updateTimers() {
        stepX++;
        if (stepX > maxStepX) {
            stepX = 0;
        }
        if (reverse) {
            stepY++;
            if (stepY > maxStepY) reverse = false;
        } else {
            stepY--;
            if (stepY < -maxStepY) reverse = true;
        }
    }

    public String getName() {
        return "quadhelix";
    }

    public boolean canBeFixed() {
        return true;
    }

}
