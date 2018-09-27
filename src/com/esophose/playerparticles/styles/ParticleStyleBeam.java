package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBeam implements ParticleStyle {

    private int step = 0;
    private boolean reversed = false;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        int points = 16;
        double radius = 1;
        double slice = 2 * Math.PI / points;
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < points; i++) {
            double angle = slice * i;
            double newX = location.getX() + radius * Math.cos(angle);
            double newY = location.getY() + (step / 10D) - 1;
            double newZ = location.getZ() + radius * Math.sin(angle);
            particles.add(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
        }
        return particles;
    }

    public void updateTimers() {
        if (!reversed) step++;
        else step--;

        if (step >= 30) {
            reversed = true;
        } else if (step <= 0) {
            reversed = false;
        }
    }

    public String getName() {
        return "beam";
    }

    public boolean canBeFixed() {
        return true;
    }

}
