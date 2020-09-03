package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSpiral extends DefaultParticleStyle {

    private int stepX = 0;

    private int particles = 12;
    private int particlesPerRotation = 90;
    private double radius = 0.8;

    protected ParticleStyleSpiral() {
        super("spiral", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (double stepY = -60; stepY < 60; stepY += 120D / this.particles) {
            double dx = -(MathL.cos(((this.stepX + stepY) / (double) this.particlesPerRotation) * Math.PI * 2)) * this.radius;
            double dy = stepY / this.particlesPerRotation / 2D;
            double dz = -(MathL.sin(((this.stepX + stepY) / (double) this.particlesPerRotation) * Math.PI * 2)) * this.radius;
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.stepX++;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("HOPPER");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("particles", 12, "The number of particles to spawn around the player");
        this.setIfNotExists("particles-per-rotation", 90, "How many particles spawn before a full rotation is made");
        this.setIfNotExists("radius", 0.8, "The radius of the spiral");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.particles = config.getInt("particles");
        this.particlesPerRotation = config.getInt("particles-per-rotation");
        this.radius = config.getDouble("radius");
    }

}
