package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleInvocation extends DefaultParticleStyle {

    private double step = 0;
    private int circleStep = 0;

    private int points;
    private double radius;
    private int numSteps;
    private float playerOffset;
    private float speedMultiplier;

    public ParticleStyleInvocation() {
        super("invocation", true, true, 0.5);
    }

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        float speed = this.getSpeedByEffect(particle.getEffect()) * this.speedMultiplier;

        // Circle around everything, spawn less often
        if (this.circleStep % 5 == 0) {
            for (int i = 0; i < this.numSteps; i++) {
                double dx = MathL.cos(Math.PI * 2 * ((double) i / this.numSteps)) * this.radius;
                double dy = this.playerOffset;
                double dz = MathL.sin(Math.PI * 2 * ((double) i / this.numSteps)) * this.radius;
                particles.add(new PParticle(location.clone().add(dx, dy, dz)));
            }
        }

        // Orbit going clockwise
        for (int i = 0; i < this.points; i++) {
            double dx = MathL.cos(this.step + (Math.PI * 2 * ((double) i / this.points))) * this.radius;
            double dy = this.playerOffset;
            double dz = MathL.sin(this.step + (Math.PI * 2 * ((double) i / this.points))) * this.radius;
            double angle = Math.atan2(dz, dx);
            float xAng = -MathL.cos(angle);
            float zAng = -MathL.sin(angle);
            particles.add(new PParticle(location.clone().add(dx, dy, dz), xAng, 0, zAng, speed, true));
        }

        // Orbit going counter-clockwise
        for (int i = 0; i > -this.points; i--) {
            double dx = MathL.cos(-this.step + (Math.PI * 2 * ((double) i / this.points))) * this.radius;
            double dy = this.playerOffset;
            double dz = MathL.sin(-this.step + (Math.PI * 2 * ((double) i / this.points))) * this.radius;
            double angle = Math.atan2(dz, dx);
            float xAng = -MathL.cos(angle);
            float zAng = -MathL.sin(angle);
            particles.add(new PParticle(location.clone().add(dx, dy, dz), xAng, 0, zAng, speed, true));
        }

        return particles;
    }

    private float getSpeedByEffect(ParticleEffect effect) {
        switch (effect) {
            case CRIT:
            case DAMAGE_INDICATOR:
            case ENCHANTED_HIT:
                return 2;
            case DRAGON_BREATH:
                return 0.01F;
            case ENCHANT:
            case NAUTILUS:
            case PORTAL:
                return 7;
            case END_ROD:
            case SMOKE:
            case SQUID_INK:
                return 0.3F;
            case FIREWORK:
            case SPIT:
            case SPLASH:
                return 0.5F;
            case POOF:
                return 0.4F;
            case TOTEM_OF_UNDYING:
                return 1.25F;
            default:
                return 0.2F; // Flame
        }
    }

    public void updateTimers() {
        this.step = (this.step + Math.PI * 2 / this.numSteps) % this.numSteps;
        this.circleStep = (this.circleStep + 1) % this.numSteps;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("ENDER_EYE", "EYE_OF_ENDER");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("spinning-points", 6, "The number of points that spin around the circle in each direction");
        this.setIfNotExists("radius", 3.5, "The radius of the circle");
        this.setIfNotExists("circle-points", 120, "The number of points around the circle");
        this.setIfNotExists("player-offset", -0.9, "How far to vertically offset the player's location");
        this.setIfNotExists("speed-multiplier", 1, "A multiplier to change how fast the particles move");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.points = config.getInt("spinning-points");
        this.radius = config.getDouble("radius");
        this.numSteps = config.getInt("circle-points");
        this.playerOffset = config.getFloat("player-offset");
        this.speedMultiplier = config.getFloat("speed-multiplier");
    }

}
