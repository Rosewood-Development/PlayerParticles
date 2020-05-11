package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStylePulse extends DefaultParticleStyle {

    private double step = 0;

    private int points;
    private double radius;
    private float offset;
    private int numSteps;
    private float speedMultiplier;

    public ParticleStylePulse() {
        super("pulse", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        float speed = this.getSpeedByEffect(particle.getEffect()) * this.speedMultiplier;

        if (this.step == 0) {
            for (int i = 0; i < this.points; i++) {
                double dx = MathL.cos(Math.PI * 2 * ((double) i / this.points)) * this.radius;
                double dy = this.offset;
                double dz = MathL.sin(Math.PI * 2 * ((double) i / this.points)) * this.radius;
                double angle = Math.atan2(dz, dx);
                float xAng = MathL.cos(angle);
                float zAng = MathL.sin(angle);
                particles.add(new PParticle(location.clone().add(dx, dy, dz), xAng, 0, zAng, speed, true));
            }
        }

        return particles;
    }

    private float getSpeedByEffect(ParticleEffect effect) {
        switch (effect) {
            case CRIT:
            case DAMAGE_INDICATOR:
            case ENCHANTED_HIT:
                return 1;
            case DRAGON_BREATH:
                return 0.01F;
            case ENCHANT:
            case NAUTILUS:
            case PORTAL:
                return 0.5F;
            case END_ROD:
            case SMOKE:
            case SQUID_INK:
                return 0.15F;
            case FIREWORK:
            case SPIT:
            case SPLASH:
                return 0.25F;
            case POOF:
                return 0.2F;
            case TOTEM_OF_UNDYING:
                return 0.75F;
            default:
                return 0.1F; // Flame
        }
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.numSteps;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("points", 50, "The number of points to spawn in the pulse circle");
        this.setIfNotExists("radius", 0.5, "The radius of the pulse circle");
        this.setIfNotExists("offset", -0.9, "The amount to vertically offset from the player location");
        this.setIfNotExists("delay", 15, "How many ticks to wait between pulses");
        this.setIfNotExists("speed-multiplier", 1, "A multiplier to change how fast the particles shoot away");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.points = config.getInt("points");
        this.radius = config.getDouble("radius");
        this.offset = config.getFloat("offset");
        this.numSteps = config.getInt("delay");
        this.speedMultiplier = config.getFloat("speed-multiplier");
    }

}
