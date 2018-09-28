package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleHalo implements ParticleStyle {

    private int step = 0;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        if (step % 2 == 0) return new ArrayList<PParticle>();

        int points = 16;
        double radius = .65;
        double slice = 2 * Math.PI / points;
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < points; i++) {
            double angle = slice * i;
            double newX = location.getX() + radius * Math.cos(angle);
            double newY = location.getY() + 1.5;
            double newZ = location.getZ() + radius * Math.sin(angle);
            particles.add(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
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

}
