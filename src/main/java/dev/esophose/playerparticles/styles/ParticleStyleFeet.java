package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleFeet implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        particles.add(new PParticle(location.clone().subtract(0, 0.95, 0), 0.4F, 0.0F, 0.4F, 0.0F));
        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "feet";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return false;
    }
    
    public double getFixedEffectOffset() {
        return 0.5;
    }

}
