package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStylePulse extends DefaultParticleStyle {

    private int points = 50;
    private double radius = 0.5;
    private double step = 0;
    private int numSteps = 15;

    public ParticleStylePulse() {
        super("pulse", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        double speed = this.getSpeedByEffect(particle.getEffect());

        if (this.step == 0) {
            for (int i = 0; i < this.points; i++) {
                double dx = MathL.cos(Math.PI * 2 * ((double) i / this.points)) * this.radius;
                double dy = -0.9;
                double dz = MathL.sin(Math.PI * 2 * ((double) i / this.points)) * this.radius;
                double angle = Math.atan2(dz, dx);
                double xAng = MathL.cos(angle);
                double zAng = MathL.sin(angle);
                particles.add(new PParticle(location.clone().add(dx, dy, dz), xAng, 0, zAng, speed, true));
            }
        }

        return particles;
    }

    private double getSpeedByEffect(ParticleEffect effect) {
        switch (effect) {
            case CRIT:
            case DAMAGE_INDICATOR:
            case ENCHANTED_HIT:
                return 1;
            case DRAGON_BREATH:
                return 0.01;
            case ENCHANT:
            case NAUTILUS:
            case PORTAL:
                return 0.5;
            case END_ROD:
            case SMOKE:
            case SQUID_INK:
                return 0.15;
            case FIREWORK:
            case SPIT:
            case SPLASH:
                return 0.25;
            case POOF:
                return 0.2;
            case TOTEM_OF_UNDYING:
                return 0.75;
            default:
                return 0.1; // Flame
        }
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.numSteps;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
