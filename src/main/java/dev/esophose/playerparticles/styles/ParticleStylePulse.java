package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStylePulse extends DefaultParticleStyle {

    private double step = 0;

    private int points;
    private double radius;
    private double offset;
    private int numSteps;
    private double speedMultiplier;

    protected ParticleStylePulse() {
        super("pulse", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        double speed = this.getSpeedByEffect(particle.getEffect()) * this.speedMultiplier;

        if (this.step == 0) {
            for (int i = 0; i < this.points; i++) {
                double dx = MathL.cos(Math.PI * 2 * ((double) i / this.points)) * this.radius;
                double dy = this.offset;
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
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("REDSTONE_TORCH", "REDSTONE_TORCH_ON");
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
        this.offset = config.getDouble("offset");
        this.numSteps = config.getInt("delay");
        this.speedMultiplier = config.getDouble("speed-multiplier");
    }

}
