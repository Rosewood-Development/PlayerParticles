package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStylePulse implements ParticleStyle {

    private int points = 50;
    private double radius = 0.5;
    private double step = 0;
    private int numSteps = 15;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        double speed = getSpeedByEffect(particle.getEffect());
        
        if (step == 0) {
            for (int i = 0; i < points; i++) {
                double dx = Math.cos(Math.PI * 2 * ((double)i / points)) * radius;
                double dy = -0.9;
                double dz = Math.sin(Math.PI * 2 * ((double)i / points)) * radius;
                double angle = Math.atan2(dz, dx);
                double xAng = Math.cos(angle);
                double zAng = Math.sin(angle);
                particles.add(new PParticle(location.clone().add(dx, dy, dz), xAng, 0, zAng, speed, true));
            }
        }
        
        return particles;
    }
    
    private double getSpeedByEffect(ParticleEffect effect) {
        switch (effect) {
        case CRIT:
        case DAMAGE_INDICATOR:
        case ENCHANTED_HIT:
            return 1;
        case DRAGON_BREATH:
            return 0.01;
        case ENCHANT:
        case NAUTILUS:
        case PORTAL:
            return radius;
        case END_ROD:
        case SMOKE:
        case SQUID_INK:
            return 0.15;
        case FIREWORK:
        case SPIT:
        case SPLASH:
            return 0.25;
        case POOF:
            return 0.2;
        case TOTEM_OF_UNDYING:
            return 0.75;
        default:
            return 0.1; // Flame
        }
    }

    public void updateTimers() {
        step = (step + 1) % numSteps;
    }

    public String getName() {
        return "pulse";
    }

    public boolean canBeFixed() {
        return true;
    }
    
    public boolean canToggleWithMovement() {
        return true;
    }
    
    public double getFixedEffectOffset() {
        return 0.5;
    }

}
