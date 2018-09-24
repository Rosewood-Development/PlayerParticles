package com.esophose.playerparticles.styles;

import java.util.Collections;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStylePoint implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return Collections.singletonList(new PParticle(location.add(0.0, 1.5, 0.0)));
    }

    public void updateTimers() {

    }

    public String getName() {
        return "point";
    }

    public boolean canBeFixed() {
        return true;
    }

}
