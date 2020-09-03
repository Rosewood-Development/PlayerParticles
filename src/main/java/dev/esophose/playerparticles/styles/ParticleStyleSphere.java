package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleSphere extends DefaultParticleStyle {

    private int density;
    private double radius;

    protected ParticleStyleSphere() {
        super("sphere", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < this.density; i++) {
            double u = Math.random();
            double v = Math.random();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);
            double dx = this.radius * MathL.sin(phi) * MathL.cos(theta);
            double dy = this.radius * MathL.sin(phi) * MathL.sin(theta);
            double dz = this.radius * MathL.cos(phi);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("HEART_OF_THE_SEA", "SNOWBALL", "SNOW_BALL");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("density", 15, "The number of particles to spawn per tick");
        this.setIfNotExists("radius", 1.5, "The radius of the sphere");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.density = config.getInt("density");
        this.radius = config.getDouble("radius");
    }

}
