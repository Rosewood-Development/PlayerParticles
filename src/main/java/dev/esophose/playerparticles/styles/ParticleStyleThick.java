package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.PParticle;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleThick implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> baseParticles = DefaultStyles.NORMAL.getParticles(particle, location);

        int multiplyingFactor = 10; // Uses the same logic as ParticleStyleNormal except multiplies the resulting particles by 10x
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < baseParticles.size() * multiplyingFactor; i++) {
            particles.add(baseParticles.get(i % baseParticles.size()));
        }

        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "thick";
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
