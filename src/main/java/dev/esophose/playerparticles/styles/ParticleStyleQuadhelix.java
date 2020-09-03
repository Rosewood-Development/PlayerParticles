package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleQuadhelix extends DefaultParticleStyle {

    private int stepX = 0;
    private int stepY = 0;
    private boolean reverse = false;

    private int orbs;
    private int maxStepX;
    private int maxStepY;

    protected ParticleStyleQuadhelix() {
        super("quadhelix", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < this.orbs; i++) {
            double dx = -(MathL.cos((this.stepX / (double) this.maxStepX) * (Math.PI * 2) + (((Math.PI * 2) / this.orbs) * i))) * ((this.maxStepY - Math.abs(this.stepY)) / (double) this.maxStepY);
            double dy = (this.stepY / (double) this.maxStepY) * 1.5;
            double dz = -(MathL.sin((this.stepX / (double) this.maxStepX) * (Math.PI * 2) + (((Math.PI * 2) / this.orbs) * i))) * ((this.maxStepY - Math.abs(this.stepY)) / (double) this.maxStepY);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.stepX++;
        if (this.stepX > this.maxStepX) {
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
        return Arrays.asList("NAUTILUS_SHELL", "ACTIVATOR_RAIL");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("orbs", 4, "The number of orbs to spawn");
        this.setIfNotExists("steps-x", 80, "The number of steps for the x-axis");
        this.setIfNotExists("steps-y", 60, "The number of steps for the y-axis");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.orbs = config.getInt("orbs");
        this.maxStepX = config.getInt("steps-x");
        this.maxStepY = config.getInt("steps-y");
    }

}
