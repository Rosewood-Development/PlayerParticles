package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleWhirlwind extends DefaultParticleStyle {

    private double step = 0;

    private int points;
    private int numSteps;
    private double speedMultiplier;
    private double offset;

    protected ParticleStyleWhirlwind() {
        super("whirlwind", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        double speed = this.getSpeedByEffect(particle.getEffect()) * this.speedMultiplier;

        // Orbit going clockwise
        for (int i = 0; i < this.points; i++) {
            double dx = MathL.cos(this.step + (Math.PI * 2 * ((double) i / this.points)));
            double dy = this.offset;
            double dz = MathL.sin(this.step + (Math.PI * 2 * ((double) i / this.points)));
            double angle = Math.atan2(dz, dx);
            double xAng = MathL.cos(angle);
            double zAng = MathL.sin(angle);
            particles.add(new PParticle(location.clone().add(0, dy, 0), xAng, 0, zAng, speed, true));
        }

        return particles;
    }

    private double getSpeedByEffect(ParticleEffect effect) {
        switch (effect) {
            case CRIT:
            case DAMAGE_INDICATOR:
            case ENCHANTED_HIT:
                return 1;
            case ELECTRIC_SPARK:
            case SCRAPE:
                return 2;
            case WAX_OFF:
            case WAX_ON:
                return 3;
            case DRAGON_BREATH:
                return 0.01;
            case ENCHANT:
            case NAUTILUS:
            case PORTAL:
                return 1;
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
        this.step = (this.step + Math.PI * 2 / this.numSteps) % this.numSteps;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("STRING");
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
        this.speedMultiplier = config.getDouble("speed-multiplier");
        this.offset = config.getDouble("offset");
    }

}
