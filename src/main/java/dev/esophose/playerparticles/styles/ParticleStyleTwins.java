package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleTwins extends DefaultParticleStyle {

    private int stepX = 0;
    private int stepY = 0;
    private boolean reverse = false;

    private int orbs = 2;
    private double radius = 1;
    private int numSteps = 60;
    private int maxStepY = 30;

    protected ParticleStyleTwins() {
        super("twins", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < this.orbs; i++) {
            double slice = Math.PI * 2 / this.numSteps;
            double orbSlice = Math.PI * 2 / this.orbs;

            double dx = -MathL.cos(slice * this.stepX + orbSlice * i) * this.radius;
            double dy = (this.stepY / (double) this.maxStepY);
            double dz = -MathL.sin(slice * this.stepX + orbSlice * i) * this.radius;
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.stepX++;
        if (this.stepX > this.numSteps) {
            this.stepX = 0;
        }

        if (this.reverse) {
            this.stepY++;
            if (this.stepY > this.maxStepY)
                this.reverse = false;
        } else {
            this.stepY--;
            if (this.stepY < -this.maxStepY)
                this.reverse = true;
        }
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("OAK_FENCE", "FENCE");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("orbs", 2, "The number of particle orbs to spawn");
        this.setIfNotExists("radius", 1.0, "The radius of where to spawn the particles");
        this.setIfNotExists("horizontal-steps", 60, "The number of particles that spawn to make a full horizontal rotation");
        this.setIfNotExists("vertical-steps", 30, "The number of particles that spawn to move either up or down");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.orbs = config.getInt("orbs");
        this.radius = config.getDouble("radius");
        this.numSteps = config.getInt("horizontal-steps");
        this.maxStepY = config.getInt("vertical-steps");
    }

}
