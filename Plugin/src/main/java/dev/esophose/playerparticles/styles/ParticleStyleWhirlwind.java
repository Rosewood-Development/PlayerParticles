package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleWhirlwind extends DefaultParticleStyle {

    private double step = 0;

    private int points;
    private int numSteps;
    private float speedMultiplier;
    private float offset;

    public ParticleStyleWhirlwind() {
        super("whirlwind", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        float speed = this.getSpeedByEffect(particle.getEffect()) * this.speedMultiplier;

        // Orbit going clockwise
        for (int i = 0; i < this.points; i++) {
            double dx = MathL.cos(this.step + (Math.PI * 2 * ((double) i / this.points)));
            double dy = this.offset;
            double dz = MathL.sin(this.step + (Math.PI * 2 * ((double) i / this.points)));
            double angle = Math.atan2(dz, dx);
            float xAng = MathL.cos(angle);
            float zAng = MathL.sin(angle);
            particles.add(new PParticle(location.clone().add(0, dy, 0), xAng, 0, zAng, speed, true));
        }

        return particles;
    }

    private float getSpeedByEffect(ParticleEffect effect) {
        switch (effect) {
            case CRIT:
            case DAMAGE_INDICATOR:
            case ENCHANTED_HIT:
            case ENCHANT:
            case NAUTILUS:
            case PORTAL:
                return 1;
            case DRAGON_BREATH:
                return 0.01F;
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
        this.step = (this.step + Math.PI * 2 / this.numSteps) % this.numSteps;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("rays", 3, "The number of rays to spawn");
        this.setIfNotExists("steps", 40, "The number of ticks it takes to make a full rotation");
        this.setIfNotExists("speed-multiplier", 2.5, "A multiplier to change how fast the particles move");
        this.setIfNotExists("offset", -0.9, "The vertical offset from the player location");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.points = config.getInt("rays");
        this.numSteps = config.getInt("steps");
        this.speedMultiplier = config.getFloat("speed-multiplier");
        this.offset = config.getFloat("offset");
    }

}
