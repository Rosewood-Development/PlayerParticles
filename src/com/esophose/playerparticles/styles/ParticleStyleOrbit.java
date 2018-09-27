package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleOrbit implements ParticleStyle {

    private int step = 0;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        int orbs = 3;
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < orbs; i++) {
            double dx = -(Math.cos((step / 120D) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i)));
            double dz = -(Math.sin((step / 120D) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i)));
            particles.add(new PParticle(new Location(location.getWorld(), location.getX() + dx, location.getY(), location.getZ() + dz)));
        }
        return particles;
    }

    public void updateTimers() {
        step++;
        if (step > 120) {
            step = 0;
        }
    }

    public String getName() {
        return "orbit";
    }

    public boolean canBeFixed() {
        return true;
    }

}
