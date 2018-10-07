package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleQuadhelix implements ParticleStyle {

    private int stepX = 0;
    private int maxStepX = 90;
    private int stepY = 0;
    private int maxStepY = 60;
    private boolean reverse = false;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < 4; i++) {
            double dx = -(Math.cos((stepX / (double)maxStepX) * (Math.PI * 2) + ((Math.PI / 2) * i))) * ((60 - Math.abs(stepY)) / (double)maxStepY);
            double dy = (stepY / (double)maxStepY) * 1.5;
            double dz = -(Math.sin((stepX / (double)maxStepX) * (Math.PI * 2) + ((Math.PI / 2) * i))) * ((60 - Math.abs(stepY)) / (double)maxStepY);
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
