package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.PParticle;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSphere implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        int density = 15;
        double radius = 1.5f;
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < density; i++) {
            double u = Math.random();
            double v = Math.random();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);
            double dx = radius * Math.sin(phi) * Math.cos(theta);
            double dy = radius * Math.sin(phi) * Math.sin(theta);
            double dz = radius * Math.cos(phi);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
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
    
    public boolean canToggleWithMovement() {
        return true;
    }
    
    public double getFixedEffectOffset() {
        return 0;
    }

}
