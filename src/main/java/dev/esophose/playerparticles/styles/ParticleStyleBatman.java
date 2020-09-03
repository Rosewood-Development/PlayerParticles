package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.VectorUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStyleBatman extends DefaultParticleStyle {
    
    private int step = 0;

    private int spawnDelay;

    protected ParticleStyleBatman() {
        super("batman", true, true, -1);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        
        if (this.step != 0)
            return particles;
        
        // Segment 1
        for (double x = -7; x <= -3; x += 0.05) {
            double y = 3 * Math.sqrt(-Math.pow(x / 7, 2) + 1);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        for (double x = 3; x <= 7; x += 0.05) {
            double y = 3 * Math.sqrt(-Math.pow(x / 7, 2) + 1);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        
        // Segment 2
        for (double x = -7; x <= -4; x += 0.05) {
            double y = -3 * Math.sqrt(-Math.pow(x / 7, 2) + 1);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        for (double x = 4; x <= 7; x += 0.05) {
            double y = -3 * Math.sqrt(-Math.pow(x / 7, 2) + 1);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        
        // Segment 3
        for (double x = -4; x <= 4; x += 0.125) {
            double y = Math.abs(x / 2) - ((3 * Math.sqrt(33) - 7) / 112) * Math.pow(x, 2) + Math.sqrt(1 - Math.pow(Math.abs(Math.abs(x) - 2) - 1, 2)) - 3;
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        
        // Segment 4
        for (double x = -1; x <= -0.75; x += 0.025) {
            double y = 9 - 8 * Math.abs(x);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        for (double x = 0.75; x <= 1; x += 0.025) {
            double y = 9 - 8 * Math.abs(x);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        
        // Segment 5
        for (double x = -0.75; x <= -0.5; x += 0.05) {
            double y = 3 * Math.abs(x) + 0.75;
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        for (double x = 0.5; x <= 0.75; x += 0.05) {
            double y = 3 * Math.abs(x) + 0.75;
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        
        // Segment 6
        for (double x = -0.5; x <= 0.5; x += 0.2) {
            double y = 2.25;
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        
        // Segment 7
        for (double x = -3; x <= -1; x += 0.02) {
            double y = 1.5 - 0.5 * Math.abs(x) - ((6 * Math.sqrt(10)) / 14) * (Math.sqrt(3 - Math.pow(x, 2) + 2 * Math.abs(x)) - 2);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }
        for (double x = 1; x <= 3; x += 0.02) {
            double y = 1.5 - 0.5 * Math.abs(x) - ((6 * Math.sqrt(10)) / 14) * (Math.sqrt(3 - Math.pow(x, 2) + 2 * Math.abs(x)) - 2);
            Vector segment = new Vector(x, y, 0).multiply(0.3);
            VectorUtils.rotateAroundAxisY(segment, -Math.toRadians(location.getYaw()));
            particles.add(new PParticle(location.clone().add(segment).add(0, 3, 0)));
        }

        return particles;
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.spawnDelay; // Only spawn once per second
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("BAT_SPAWN_EGG", "COAL");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("spawn-delay", 20, "The number of ticks to wait between particle spawns");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.spawnDelay = config.getInt("spawn-delay");
    }

}
