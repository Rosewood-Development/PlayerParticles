package com.esophose.playerparticles.styles;

import org.bukkit.Location;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleNone implements ParticleStyle {

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        ParticleEffect particleEffect = pplayer.getParticleEffect();
        if (particleEffect.equals(ParticleEffect.ANGRY_VILLAGER)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.BUBBLE)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.CLOUD)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.CRIT)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.DEPTH_SUSPEND)) {
            PParticle[] particles = new PParticle[5];
            for (int i = 0; i < 5; i++)
                particles[i] = new PParticle(location, 0.5F, 0.5F, 0.5F, 0.0F);
            return particles;
        } else if (particleEffect.equals(ParticleEffect.DRIP_LAVA)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.DRIP_WATER)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.ENCHANTMENT_TABLE)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.05F) };
        } else if (particleEffect.equals(ParticleEffect.EXPLODE)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.FIREWORKS_SPARK)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.FLAME)) {
            return new PParticle[] { new PParticle(location, 0.1F, 0.1F, 0.1F, 0.05F) };
        } else if (particleEffect.equals(ParticleEffect.FOOTSTEP)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.HAPPY_VILLAGER)) {
            return new PParticle[] { new PParticle(location, 0.5F, 0.5F, 0.5F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.HEART)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.HUGE_EXPLOSION)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.INSTANT_SPELL)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.LARGE_EXPLODE)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.LARGE_SMOKE)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.LAVA)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.MAGIC_CRIT)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.MOB_SPELL)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.MOB_SPELL_AMBIENT)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.NOTE)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.PORTAL)) {
            return new PParticle[] { new PParticle(location, 0.5F, 0.5F, 0.5F, 0.05F) };
        } else if (particleEffect.equals(ParticleEffect.RED_DUST)) {
            return new PParticle[] { new PParticle(location, 0.5F, 0.5F, 0.5F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SLIME)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SMOKE)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SNOW_SHOVEL)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SNOWBALL_POOF)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SPELL)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SUSPENDED)) {
            PParticle[] particles = new PParticle[5];
            for (int i = 0; i < 5; i++)
                particles[i] = new PParticle(location, 0.8F, 0.8F, 0.8F, 0.0F);
            return particles;
        } else if (particleEffect.equals(ParticleEffect.WAKE)) {
            PParticle[] particles = new PParticle[3];
            for (int i = 0; i < 3; i++)
                particles[i] = new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F);
            return particles;
        } else if (particleEffect.equals(ParticleEffect.WITCH_MAGIC)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.BARRIER)) {
            return new PParticle[] { new PParticle(location, 1.2F, 1.2F, 1.2F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.DROPLET)) {
            return new PParticle[] { new PParticle(location, 0.8F, 0.8F, 0.8F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.DRAGON_BREATH)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.END_ROD)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.DAMAGE_INDICATOR)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SWEEP_ATTACK)) {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.BLOCK_CRACK)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.BLOCK_DUST)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.ITEM_CRACK)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.FALLING_DUST)) {
            PParticle[] particles = new PParticle[2];
            for (int i = 0; i < 2; i++)
                particles[i] = new PParticle(location.add(0, 0.75, 0), 0.6F, 0.4F, 0.6F, 0.0F);
            return particles;
        } else if (particleEffect.equals(ParticleEffect.TOTEM)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else if (particleEffect.equals(ParticleEffect.SPIT)) {
            return new PParticle[] { new PParticle(location, 0.6F, 0.6F, 0.6F, 0.0F) };
        } else {
            return new PParticle[] { new PParticle(location, 0.4F, 0.4F, 0.4F, 0.0F) };
        }
    }

    public void updateTimers() {

    }

    public String getName() {
        return "none";
    }

    public boolean canBeFixed() {
        return true;
    }

}
