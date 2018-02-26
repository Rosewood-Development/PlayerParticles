package com.esophose.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.styles.api.PParticle;

public class ParticleStyleThick extends ParticleStyleNone {

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        PParticle[] baseParticles = super.getParticles(pplayer, location);

        int multiplyingFactor = 15; // Uses the same logic as ParticleStyleNone except multiplies the resulting particles by 15x
        PParticle[] particles = new PParticle[baseParticles.length * multiplyingFactor];
        for (int i = 0; i < baseParticles.length * multiplyingFactor; i++) {
            particles[i] = baseParticles[i % baseParticles.length];
        }

        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "thick";
    }

}
