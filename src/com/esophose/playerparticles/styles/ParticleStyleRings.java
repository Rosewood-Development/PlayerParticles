package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleRings implements ParticleStyle {
    
    private static double[] cos, sin;
    private int index = 0;
    
    static {
        cos = new double[32];
        sin = new double[32];
        
        int i = 0;
        for (double n = 0; n < Math.PI * 2; n += Math.PI / 16) {
            cos[i] = Math.sin(n);
            sin[i] = Math.cos(n);
            i++;
        }
    }

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        
        particles.add(new PParticle(location.clone().add(cos[index], sin[index], sin[index])));
        particles.add(new PParticle(location.clone().add(cos[wrap(index + 16)], sin[wrap(index + 16)], sin[wrap(index + 16)])));
        particles.add(new PParticle(location.clone().add(cos[wrap(index + 16)], sin[index], sin[wrap(index + 16)])));
        particles.add(new PParticle(location.clone().add(cos[index], sin[wrap(index + 16)], sin[index])));
        
        return particles;
    }
    
    /**
     * Wraps an index around the cos/sin array length
     * 
     * @param index The index to wrap
     * @return The wrapped index
     */
    private int wrap(int index) {
        return index % cos.length;
    }

    public void updateTimers() {
        index = (index + 1) % cos.length;
    }

    public String getName() {
        return "rings";
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
