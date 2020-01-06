package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStylePopper implements ParticleStyle {

    private double grow = 0.08f;
    private double radials = Math.PI / 16;
    private int helices = 2;
    private int step = 0;
    private int maxStep = 35;

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        double radius = (1 - (double)step / maxStep);
        for (int i = 0; i < helices; i++) {
            double angle = step * radials + (2 * Math.PI * i / helices);
            Vector v = new Vector(Math.cos(angle) * radius, step * grow - 1, Math.sin(angle) * radius);

            particles.add(new PParticle(location.clone().add(v)));
        }
        
        if (step == maxStep - 1) {
            for (int i = 0; i < 10; i++) {
                particles.add(new PParticle(location.clone().add(0, 1.5, 0), 0.5, 0.5, 0.5, 0.03));
            }
        }
        
        return particles;
    }

    public void updateTimers() {
        step = (step + 1) % maxStep;
    }

    public String getName() {
        return "popper";
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
