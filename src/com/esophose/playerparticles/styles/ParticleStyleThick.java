package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleThick implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> baseParticles = DefaultStyles.NONE.getParticles(particle, location);

        int multiplyingFactor = 10; // Uses the same logic as ParticleStyleNone except multiplies the resulting particles by 10x
        List<PParticle> particles = new ArrayList<PParticle>();
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

}
