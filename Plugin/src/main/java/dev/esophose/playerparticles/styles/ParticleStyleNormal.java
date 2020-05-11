package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleNormal extends DefaultParticleStyle {

    public ParticleStyleNormal() {
        super("normal", true, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        float speed = 0;
        switch (particle.getEffect()) {
            case ENCHANT:
            case NAUTILUS:
            case PORTAL:
                speed = 1.0F;
                break;
            case FLAME:
                speed = 0.05F;
                break;

        }

        int amount = 1;
        switch (particle.getEffect()) {
            case FALLING_DUST:
                amount = 2;
                break;
            case UNDERWATER:
                amount = 5;
                break;
        }

        float offset = 0.5F;
        switch (particle.getEffect()) {
            case CLOUD:
                offset = 0.0F;
                break;
            case FLAME:
                offset = 0.1F;
                break;
        }

        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < amount; i++)
            particles.add(new PParticle(location, offset, offset, offset, speed));
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
