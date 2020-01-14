package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.VectorUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class ParticleStyleWings extends DefaultParticleStyle {

    private int spawnTimer = 0; // Spawn particles every 3 ticks

    public ParticleStyleWings() {
        super("wings", false, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        if (this.spawnTimer == 0) {
            for (double t = 0; t < Math.PI * 2; t += Math.PI / 48) {
                double offset = (Math.pow(Math.E, Math.cos(t)) - 2 * Math.cos(t * 4) - Math.pow(Math.sin(t / 12), 5)) / 2;
                double x = Math.sin(t) * offset;
                double y = Math.cos(t) * offset;
                Vector v = VectorUtils.rotateAroundAxisY(new Vector(x, y, -0.3), -Math.toRadians(location.getYaw()));
                particles.add(new PParticle(location.clone().add(v.getX(), v.getY(), v.getZ())));
            }
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        this.spawnTimer++;
        this.spawnTimer %= 3;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
