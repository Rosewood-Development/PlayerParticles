package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStylePopper extends DefaultParticleStyle {

    private double grow = 0.08f;
    private double radials = Math.PI / 16;
    private int helices = 2;
    private int step = 0;
    private int maxStep = 35;

    public ParticleStylePopper() {
        super("popper", true, true, 0.5);
    }

    @Override
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

    @Override
    public void updateTimers() {
        step = (step + 1) % maxStep;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
