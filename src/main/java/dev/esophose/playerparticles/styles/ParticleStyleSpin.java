package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSpin extends DefaultParticleStyle {

    private int step = 0;

    private int maxSteps = 30;
    private double radius;
    private double offset;

    protected ParticleStyleSpin() {
        super("spin", true, true, -0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        double slice = (Math.PI * 2 / this.maxSteps) * this.step;

        double newX = location.getX() + this.radius * MathL.cos(slice);
        double newY = location.getY() + this.offset;
        double newZ = location.getZ() + this.radius * MathL.sin(slice);
        return Collections.singletonList(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.maxSteps;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("BEACON");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("particles-per-rotation", 30, "The number of particles to spawn per rotation");
        this.setIfNotExists("radius", 0.5, "The radius of the circle");
        this.setIfNotExists("offset", 1.5, "The amount to vertically offset the player location");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.maxSteps = config.getInt("particles-per-rotation");
        this.radius = config.getDouble("radius");
        this.offset = config.getDouble("offset");
    }

}
