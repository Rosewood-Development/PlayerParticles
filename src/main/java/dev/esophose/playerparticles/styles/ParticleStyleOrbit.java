package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleOrbit extends DefaultParticleStyle {

    private int step = 0;

    private int orbs;
    private int numSteps;
    private double radius;

    protected ParticleStyleOrbit() {
        super("orbit", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < this.orbs; i++) {
            double dx = -(MathL.cos((this.step / (double) this.numSteps) * (Math.PI * 2) + (((Math.PI * 2) / this.orbs) * i))) * this.radius;
            double dz = -(MathL.sin((this.step / (double) this.numSteps) * (Math.PI * 2) + (((Math.PI * 2) / this.orbs) * i))) * this.radius;
            particles.add(new PParticle(location.clone().add(dx, 0, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.numSteps;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("ENCHANTING_TABLE", "ENCHANTMENT_TABLE");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("orbs", 3, "The number of orbs that orbit the player");
        this.setIfNotExists("steps", 120, "The number of spawning steps around the player");
        this.setIfNotExists("radius", 1.0, "The radius for spawning the orbs");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.orbs = config.getInt("orbs");
        this.numSteps = config.getInt("steps");
        this.radius = config.getDouble("radius");
    }

}
