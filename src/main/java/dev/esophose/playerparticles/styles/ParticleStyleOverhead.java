package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleOverhead implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        particles.add(new PParticle(location.clone().add(0, 1.75, 0), 0.4F, 0.1F, 0.4F, 0.0F));
        particles.add(new PParticle(location.clone().add(0, 1.75, 0), 0.4F, 0.1F, 0.4F, 0.0F));
        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "overhead";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return false;
    }
    
    public double getFixedEffectOffset() {
        return -0.5;
    }

}
