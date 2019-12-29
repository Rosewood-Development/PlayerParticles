package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.util.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStyleWings implements ParticleStyle {

    private int spawnTimer = 0; // Spawn particles every 3 ticks

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        if (spawnTimer == 0) {
            for (double t = 0; t < Math.PI * 2; t += Math.PI / 48) {
                double x = Math.sin(t) * (Math.pow(Math.E, Math.cos(t)) - 2 * Math.cos(t * 4) - Math.pow(Math.sin(t / 12), 5)) / 2;
                double y = Math.cos(t) * (Math.pow(Math.E, Math.cos(t)) - 2 * Math.cos(t * 4) - Math.pow(Math.sin(t / 12), 5)) / 2;
                Vector v = VectorUtils.rotateAroundAxisY(new Vector(x, y, -0.3), -Math.toRadians(location.getYaw()));
                particles.add(new PParticle(location.clone().add(v.getX(), v.getY(), v.getZ())));
            }
        }
        return particles;
    }

    public void updateTimers() {
        spawnTimer++;
        spawnTimer %= 3;
    }

    public String getName() {
        return "wings";
    }

    public boolean canBeFixed() {
        return false;
    }
    
    public boolean canToggleWithMovement() {
        return true;
    }
    
    public double getFixedEffectOffset() {
        return 0;
    }

}
