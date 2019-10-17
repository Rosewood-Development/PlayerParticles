package dev.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;

import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.api.PParticle;
import dev.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBeam implements ParticleStyle {

    private static double[] cos, sin;
    private static final int points = 16;
    private int step = 0;
    private boolean reversed = false;
    
    static {
        cos = new double[points];
        sin = new double[points];
        
        int i = 0;
        for (double n = 0; n < Math.PI * 2; n += Math.PI * 2 / points) {
            cos[i] = Math.cos(n);
            sin[i] = Math.sin(n);
            i++;
        }
    }

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        double radius = 1;
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < points; i++) {
            double newX = location.getX() + radius * cos[i];
            double newY = location.getY() + (step / 10D) - 1;
            double newZ = location.getZ() + radius * sin[i];
            particles.add(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
        }
        return particles;
    }

    public void updateTimers() {
        if (!reversed) step++;
        else step--;

        if (step >= 30) {
            reversed = true;
        } else if (step <= 0) {
            reversed = false;
        }
    }

    public String getName() {
        return "beam";
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
