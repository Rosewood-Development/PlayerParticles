package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleChains extends DefaultParticleStyle {

    public ParticleStyleChains() {
        super("chains", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        
        for (double n = -0.2; n < 0.6; n += 0.1) {
            particles.add(new PParticle(location.clone().add(1 - n, n - 1.1, 1 - n)));
            particles.add(new PParticle(location.clone().add(1 - n, n - 1.1, -1 + n)));
            particles.add(new PParticle(location.clone().add(-1 + n, n - 1.1, 1 - n)));
            particles.add(new PParticle(location.clone().add(-1 + n, n - 1.1, -1 + n)));
        }
        
        return particles;
    }

    @Override
    public void updateTimers() {
        
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
