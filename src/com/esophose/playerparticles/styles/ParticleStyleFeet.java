package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleFeet implements ParticleStyle {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
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
