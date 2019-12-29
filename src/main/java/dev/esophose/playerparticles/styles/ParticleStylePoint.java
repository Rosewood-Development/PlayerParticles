package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.PParticle;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStylePoint implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return Collections.singletonList(new PParticle(location.clone().add(0.0, 1.5, 0.0)));
    }

    public void updateTimers() {

    }

    public String getName() {
        return "point";
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
