package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleRings extends DefaultParticleStyle {

    private int step = 0;
    private final static int maxStep = 32;

    public ParticleStyleRings() {
        super("rings", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        double ring1 = Math.PI / (maxStep / 2D) * this.step;
        double ring2 = Math.PI / (maxStep / 2D) * (((this.step + maxStep / 2D) % maxStep));

        particles.add(new PParticle(location.clone().add(Math.cos(ring1), Math.sin(ring1), Math.sin(ring1))));
        particles.add(new PParticle(location.clone().add(Math.cos(ring1 + Math.PI), Math.sin(ring1), Math.sin(ring1 + Math.PI))));
        particles.add(new PParticle(location.clone().add(Math.cos(ring2), Math.sin(ring2), Math.sin(ring2))));
        particles.add(new PParticle(location.clone().add(Math.cos(ring2 + Math.PI), Math.sin(ring2), Math.sin(ring2 + Math.PI))));

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % maxStep;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
