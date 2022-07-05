package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleNormal extends ConfiguredParticleStyle {

    protected ParticleStyleNormal() {
        super("normal", true, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        ParticleEffect particleEffect = particle.getEffect();
        List<PParticle> particles = new ArrayList<>();

        switch (particleEffect) {
            case AMBIENT_ENTITY_EFFECT:
            case ANGRY_VILLAGER:
            case BARRIER:
            case BLOCK:
            case DRIPPING_LAVA:
            case DRIPPING_WATER:
            case HEART:
            case ITEM:
            case NOTE:
            case SPIT:
            case SQUID_INK:
            case TOTEM_OF_UNDYING:
                return Collections.singletonList(PParticle.builder(location).offsets(0.6, 0.6, 0.6).build());
            case DUST:
            case HAPPY_VILLAGER:
                return Collections.singletonList(PParticle.builder(location).offsets(0.5, 0.5, 0.5).build());
            case ENCHANT:
                return Collections.singletonList(PParticle.builder(location).offsets(0.6, 0.6, 0.6).speed(1.0).build());
            case FALLING_DUST:
                for (int i = 0; i < 2; i++)
                    particles.add(PParticle.builder(location.add(0, 0.75, 0)).offsets(0.6, 0.4, 0.6).build());
                return particles;
            case FLAME:
            case CLOUD:
                return Collections.singletonList(PParticle.builder(location).offsets(0.1, 0.1, 0.1).speed(0.05).build());
            case NAUTILUS:
            case PORTAL:
                return Collections.singletonList(PParticle.builder(location).offsets(0.5, 0.5, 0.5).speed(1.0).build());
            case UNDERWATER:
                for (int i = 0; i < 5; i++)
                    particles.add(PParticle.builder(location).offsets(0.5, 0.5, 0.5).build());
                return particles;
            default:
                return Collections.singletonList(PParticle.builder(location).offsets(0.4, 0.4, 0.4).build());
        }
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("DIRT");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
