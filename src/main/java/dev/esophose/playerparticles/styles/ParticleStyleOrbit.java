package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleOrbit extends DefaultParticleStyle {

    private static final int orbs = 3;
    private static final int numSteps = 120;
    private int step = 0;

    public ParticleStyleOrbit() {
        super("orbit", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < orbs; i++) {
            double dx = -(Math.cos((this.step / (double) numSteps) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i)));
            double dz = -(Math.sin((this.step / (double) numSteps) * (Math.PI * 2) + (((Math.PI * 2) / orbs) * i)));
            particles.add(new PParticle(location.clone().add(dx, 0, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % numSteps;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
