package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleSphere implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        int density = 15;
        float radius = 1.5f;
        List<PParticle> particles = new ArrayList<PParticle>();

        for (int i = 0; i < density; i++) {
            double u = Math.random();
            double v = Math.random();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);
            double x = location.getX() + (radius * Math.sin(phi) * Math.cos(theta));
            double y = location.getY() + (radius * Math.sin(phi) * Math.sin(theta));
            double z = location.getZ() + (radius * Math.cos(phi));
            particles.add(new PParticle(new Location(location.getWorld(), x, y, z)));
        }

        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "sphere";
    }

    public boolean canBeFixed() {
        return true;
    }

}
