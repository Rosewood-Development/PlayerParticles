package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleRings extends DefaultParticleStyle {

    private int step = 0;

    private int maxStep;

    protected ParticleStyleRings() {
        super("rings", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        double ring1 = Math.PI / (this.maxStep / 2D) * this.step;
        double ring2 = Math.PI / (this.maxStep / 2D) * (((this.step + this.maxStep / 2D) % this.maxStep));

        particles.add(new PParticle(location.clone().add(MathL.cos(ring1), MathL.sin(ring1), MathL.sin(ring1))));
        particles.add(new PParticle(location.clone().add(MathL.cos(ring1 + Math.PI), MathL.sin(ring1), MathL.sin(ring1 + Math.PI))));
        particles.add(new PParticle(location.clone().add(MathL.cos(ring2), MathL.sin(ring2), MathL.sin(ring2))));
        particles.add(new PParticle(location.clone().add(MathL.cos(ring2 + Math.PI), MathL.sin(ring2), MathL.sin(ring2 + Math.PI))));

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.maxStep;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("LEAD", "LEASH");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("particles-per-ring", 32, "The number of particles that will spawn for each ring");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.maxStep = config.getInt("particles-per-ring");
    }

}
