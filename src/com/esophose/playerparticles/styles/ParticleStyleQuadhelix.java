package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleQuadhelix implements ParticleStyle {

    private float stepX = 0;
    private float stepY = 0;
    private boolean reverse = false;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < 4; i++) {
            double dx = -(Math.cos((stepX / 90) * (Math.PI * 2) + ((Math.PI / 2) * i))) * ((60 - Math.abs(stepY)) / 60);
            double dy = (stepY / 60) * 1.5;
            double dz = -(Math.sin((stepX / 90) * (Math.PI * 2) + ((Math.PI / 2) * i))) * ((60 - Math.abs(stepY)) / 60);
            particles.add(new PParticle(new Location(location.getWorld(), location.getX() + dx, location.getY() + dy, location.getZ() + dz)));
        }
        return particles;
    }

    public void updateTimers() {
        stepX++;
        if (stepX > 90) {
            stepX = 0;
        }
        if (reverse) {
            stepY++;
            if (stepY > 60) reverse = false;
        } else {
            stepY--;
            if (stepY < -60) reverse = true;
        }
    }

    public String getName() {
        return "quadhelix";
    }

    public boolean canBeFixed() {
        return true;
    }

}
