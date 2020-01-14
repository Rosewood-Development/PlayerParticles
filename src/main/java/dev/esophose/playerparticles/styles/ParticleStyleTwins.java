package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleTwins extends DefaultParticleStyle {

    private static double[] cos, sin;
    private static final int orbs = 2;
    private static final int numSteps = 60;
    private int stepX = 0;
    private int stepY = 0;
    private int maxStepY = 30;
    private boolean reverse = false;

    static {
        cos = new double[120];
        sin = new double[120];

        int i = 0;
        for (double n = 0; n < numSteps; n++) {
            cos[i] = -MathL.cos(n / numSteps * Math.PI * 2);
            sin[i] = -MathL.sin(n / numSteps * Math.PI * 2);
            i++;
        }
    }

    public ParticleStyleTwins() {
        super("twins", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < orbs; i++) {
            double dx = cos[(this.stepX + (numSteps / orbs * i)) % numSteps];
            double dy = (this.stepY / (double) this.maxStepY);
            double dz = sin[(this.stepX + (numSteps / orbs * i)) % numSteps];
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.stepX++;
        if (this.stepX > numSteps) {
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
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
