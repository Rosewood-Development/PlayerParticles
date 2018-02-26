package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.util.Vector;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.VectorUtils;

public class ParticleStyleWings implements ParticleStyle {

    int spawnTimer = 0; // Spawn particles every 3 ticks

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        if (spawnTimer == 0) {
            for (double t = 0; t < Math.PI * 2; t += Math.PI / 64) {
                double x = Math.sin(t) * (Math.pow(Math.E, Math.cos(t)) - 2 * Math.cos(t * 4) - Math.pow(Math.sin(t / 12), 5)) / 2;
                double y = Math.cos(t) * (Math.pow(Math.E, Math.cos(t)) - 2 * Math.cos(t * 4) - Math.pow(Math.sin(t / 12), 5)) / 2;
                Vector v = VectorUtils.rotateAroundAxisY(new Vector(x, y, -0.3), -Math.toRadians(location.getYaw()));
                Location loc = new Location(location.getWorld(), location.getX() + v.getX(), location.getY() + v.getY(), location.getZ() + v.getZ());
                particles.add(new PParticle(loc));
            }
        }
        return particles.toArray(new PParticle[particles.size()]);
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

}
