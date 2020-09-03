package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.MathL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleHalo extends DefaultParticleStyle {

    private boolean skipNextSpawn = false;

    private int points;
    private double radius;
    private double playerOffset;

    protected ParticleStyleHalo() {
        super("halo", true, false, -0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        if (this.skipNextSpawn)
            return particles;

        double slice = 2 * Math.PI / this.points;

        for (int i = 0; i < this.points; i++) {
            double angle = slice * i;
            double dx = this.radius * MathL.cos(angle);
            double dy = this.playerOffset;
            double dz = this.radius * MathL.sin(angle);
            particles.add(new PParticle(location.clone().add(dx, dy, dz)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.skipNextSpawn = !this.skipNextSpawn;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("END_PORTAL_FRAME", "ENDER_PORTAL_FRAME");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("particle-amount", 16, "The number of points in the halo");
        this.setIfNotExists("radius", 0.65, "The radius of the halo");
        this.setIfNotExists("player-offset", 1.5, "How far to offset the player location");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.points = config.getInt("particle-amount");
        this.radius = config.getDouble("radius");
        this.playerOffset = config.getDouble("player-offset");
    }

}
