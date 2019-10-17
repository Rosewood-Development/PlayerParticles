package dev.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.api.PParticle;
import dev.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleInvocation implements ParticleStyle {
    
    private int points = 6;
    private double radius = 3.5;
    private double step = 0;
    private int circleStep = 0;
    private int numSteps = 120;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        double speed = getSpeedByEffect(particle.getEffect());
        
        // Circle around everything, spawn less often
        if (circleStep % 5 == 0) {
            for (int i = 0; i < numSteps; i++) {
                double dx = Math.cos(Math.PI * 2 * ((double)i / numSteps)) * radius;
                double dy = -0.9;
                double dz = Math.sin(Math.PI * 2 * ((double)i / numSteps)) * radius;
                particles.add(new PParticle(location.clone().add(dx, dy, dz)));
            }
        }
        
        // Orbit going clockwise
        for (int i = 0; i < points; i++) {
            double dx = Math.cos(step + (Math.PI * 2 * ((double)i / points))) * radius;
            double dy = -0.9;
            double dz = Math.sin(step + (Math.PI * 2 * ((double)i / points))) * radius;
            double angle = Math.atan2(dz, dx);
            double xAng = -Math.cos(angle);
            double zAng = -Math.sin(angle);
            particles.add(new PParticle(location.clone().add(dx, dy, dz), xAng, 0, zAng, speed, true));
        }
        
        // Orbit going counter-clockwise
        for (int i = 0; i > -points; i--) {
            double dx = Math.cos(-step + (Math.PI * 2 * ((double)i / points))) * radius;
            double dy = -0.9;
            double dz = Math.sin(-step + (Math.PI * 2 * ((double)i / points))) * radius;
            double angle = Math.atan2(dz, dx);
            double xAng = -Math.cos(angle);
            double zAng = -Math.sin(angle);
            particles.add(new PParticle(location.clone().add(dx, dy, dz), xAng, 0, zAng, speed, true));
        }
        
        return particles;
    }
    
    private double getSpeedByEffect(ParticleEffect effect) {
        switch (effect) {
        case CRIT:
        case DAMAGE_INDICATOR:
        case ENCHANTED_HIT:
            return 2;
        case DRAGON_BREATH:
            return 0.01;
        case ENCHANT:
        case NAUTILUS:
        case PORTAL:
            return radius * 2;
        case END_ROD:
        case SMOKE:
        case SQUID_INK:
            return 0.3;
        case FIREWORK:
        case SPIT:
        case SPLASH:
            return 0.5;
        case POOF:
            return 0.4;
        case TOTEM_OF_UNDYING:
            return 1.25;
        default:
            return 0.2; // Flame
        }
    }

    public void updateTimers() {
        step = (step + Math.PI * 2 / numSteps) % numSteps;
        circleStep = (circleStep + 1) % numSteps;
    }

    public String getName() {
        return "invocation";
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
