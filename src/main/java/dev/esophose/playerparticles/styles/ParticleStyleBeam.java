package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleBeam extends DefaultParticleStyle {

    private static double[] cos, sin;
    private static final int points = 16;
    private int step = 0;
    private boolean reversed = false;
    
    static {
        cos = new double[points];
        sin = new double[points];
        
        int i = 0;
        for (double n = 0; n < Math.PI * 2; n += Math.PI * 2 / points) {
            cos[i] = Math.cos(n);
            sin[i] = Math.sin(n);
            i++;
        }
    }

    public ParticleStyleBeam() {
        super("beam", true, true, 0.5);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        double radius = 1;
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < points; i++) {
            double newX = location.getX() + radius * cos[i];
            double newY = location.getY() + (step / 10D) - 1;
            double newZ = location.getZ() + radius * sin[i];
            particles.add(new PParticle(new Location(location.getWorld(), newX, newY, newZ)));
        }
        return particles;
    }

    @Override
    public void updateTimers() {
        if (!reversed) step++;
        else step--;

        if (step >= 30) {
            reversed = true;
        } else if (step <= 0) {
            reversed = false;
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
