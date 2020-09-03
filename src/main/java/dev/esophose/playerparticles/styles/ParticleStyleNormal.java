package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;

public class ParticleStyleNormal extends DefaultParticleStyle {

    protected ParticleStyleNormal() {
        super("normal", true, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        ParticleEffect particleEffect = particle.getEffect();
        List<PParticle> particles = new ArrayList<>();

        switch (particleEffect) {
            case AMBIENT_ENTITY_EFFECT:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case ANGRY_VILLAGER:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case BARRIER:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case BLOCK:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case BUBBLE:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case BUBBLE_COLUMN_UP:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case BUBBLE_POP:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case CLOUD:
                return Collections.singletonList(new PParticle(location, 0.0, 0.0, 0.0, 0.0));
            case CRIT:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case CURRENT_DOWN:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case DAMAGE_INDICATOR:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case DOLPHIN:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case DRAGON_BREATH:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case DRIPPING_LAVA:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case DRIPPING_WATER:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case DUST:
                return Collections.singletonList(new PParticle(location, 0.5, 0.5, 0.5, 0.0));
            case ENCHANT:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 1.0));
            case ENCHANTED_HIT:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case END_ROD:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case ENTITY_EFFECT:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case EXPLOSION:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case EXPLOSION_EMITTER:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case FALLING_DUST:
                for (int i = 0; i < 2; i++)
                    particles.add(new PParticle(location.add(0, 0.75, 0), 0.6, 0.4, 0.6, 0.0));
                return particles;
            case FIREWORK:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case FISHING:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case FLAME:
                return Collections.singletonList(new PParticle(location, 0.1, 0.1, 0.1, 0.05));
            case FOOTSTEP:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case HAPPY_VILLAGER:
                return Collections.singletonList(new PParticle(location, 0.5, 0.5, 0.5, 0.0));
            case HEART:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case INSTANT_EFFECT:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case ITEM:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case ITEM_SLIME:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case ITEM_SNOWBALL:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case LARGE_SMOKE:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case LAVA:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case MYCELIUM:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case NAUTILUS:
                return Collections.singletonList(new PParticle(location, 0.5, 0.5, 0.5, 1.0));
            case NOTE:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case POOF:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case PORTAL:
                return Collections.singletonList(new PParticle(location, 0.5, 0.5, 0.5, 1.0));
            case RAIN:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case SMOKE:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case SPELL:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case SPIT:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case SPLASH:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case SQUID_INK:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case SWEEP_ATTACK:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            case TOTEM_OF_UNDYING:
                return Collections.singletonList(new PParticle(location, 0.6, 0.6, 0.6, 0.0));
            case UNDERWATER:
                for (int i = 0; i < 5; i++)
                    particles.add(new PParticle(location, 0.5, 0.5, 0.5, 0.0));
                return particles;
            case WITCH:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
            default:
                return Collections.singletonList(new PParticle(location, 0.4, 0.4, 0.4, 0.0));
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
