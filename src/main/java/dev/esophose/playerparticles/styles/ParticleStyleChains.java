package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleChains implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        
        for (double n = -0.2; n < 0.6; n += 0.1) {
            particles.add(new PParticle(location.clone().add(1 - n, n - 1.1, 1 - n)));
            particles.add(new PParticle(location.clone().add(1 - n, n - 1.1, -1 + n)));
            particles.add(new PParticle(location.clone().add(-1 + n, n - 1.1, 1 - n)));
            particles.add(new PParticle(location.clone().add(-1 + n, n - 1.1, -1 + n)));
        }
        
        return particles;
    }

    public void updateTimers() {
        
    }

    public String getName() {
        return "chains";
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
