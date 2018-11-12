package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleSpiral implements ParticleStyle {

    private int stepX = 0;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int stepY = -60; stepY < 60; stepY += 10) {
            double dx = -(Math.cos(((stepX + stepY) / 90D) * Math.PI * 2)) * 0.8;
            double dy = stepY / 45D;
            double dz = -(Math.sin(((stepX + stepY) / 90D) * Math.PI * 2)) * 0.8;
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    public void updateTimers() {
        stepX++;
    }

    public String getName() {
        return "spiral";
    }

    public boolean canBeFixed() {
        return true;
    }

}
