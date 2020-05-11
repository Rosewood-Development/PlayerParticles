package dev.esophose.playerparticles.particles.version;

import dev.esophose.playerparticles.particles.ParticleEffect;
import java.util.HashMap;
import java.util.Map;

public class VersionMapping15 extends VersionMapping {

    private final Map<Integer, ParticleEffect> particleEffectNameMapping;

    public VersionMapping15() {
        this.particleEffectNameMapping = new HashMap<Integer, ParticleEffect>() {{
            this.put(0, ParticleEffect.AMBIENT_ENTITY_EFFECT);
            this.put(1, ParticleEffect.ANGRY_VILLAGER);
            this.put(2, ParticleEffect.BARRIER);
            this.put(3, ParticleEffect.BLOCK);
            this.put(4, ParticleEffect.BUBBLE);
            this.put(5, ParticleEffect.CLOUD);
            this.put(6, ParticleEffect.CRIT);
            this.put(7, ParticleEffect.DAMAGE_INDICATOR);
            this.put(8, ParticleEffect.DRAGON_BREATH);
            this.put(9, ParticleEffect.DRIPPING_LAVA);
            this.put(10, ParticleEffect.FALLING_LAVA);
            this.put(11, ParticleEffect.LANDING_LAVA);
            this.put(12, ParticleEffect.DRIPPING_WATER);
            this.put(13, ParticleEffect.FALLING_WATER);
            this.put(14, ParticleEffect.DUST);
            this.put(15, ParticleEffect.SPELL);
            this.put(16, ParticleEffect.ELDER_GUARDIAN);
            this.put(17, ParticleEffect.ENCHANTED_HIT);
            this.put(18, ParticleEffect.ENCHANT);
            this.put(19, ParticleEffect.END_ROD);
            this.put(20, ParticleEffect.ENTITY_EFFECT);
            this.put(21, ParticleEffect.EXPLOSION_EMITTER);
            this.put(22, ParticleEffect.EXPLOSION);
            this.put(23, ParticleEffect.FALLING_DUST);
            this.put(24, ParticleEffect.FIREWORK);
            this.put(25, ParticleEffect.FISHING);
            this.put(26, ParticleEffect.FLAME);
            this.put(27, ParticleEffect.FLASH);
            this.put(28, ParticleEffect.HAPPY_VILLAGER);
            this.put(29, ParticleEffect.COMPOSTER);
            this.put(30, ParticleEffect.HEART);
            this.put(31, ParticleEffect.INSTANT_EFFECT);
            this.put(32, ParticleEffect.ITEM);
            this.put(33, ParticleEffect.ITEM_SLIME);
            this.put(34, ParticleEffect.ITEM_SNOWBALL);
            this.put(35, ParticleEffect.LARGE_SMOKE);
            this.put(36, ParticleEffect.LAVA);
            this.put(37, ParticleEffect.MYCELIUM);
            this.put(38, ParticleEffect.NOTE);
            this.put(39, ParticleEffect.POOF);
            this.put(40, ParticleEffect.PORTAL);
            this.put(41, ParticleEffect.RAIN);
            this.put(42, ParticleEffect.SMOKE);
            this.put(43, ParticleEffect.SNEEZE);
            this.put(44, ParticleEffect.SPIT);
            this.put(45, ParticleEffect.SQUID_INK);
            this.put(46, ParticleEffect.SWEEP_ATTACK);
            this.put(47, ParticleEffect.TOTEM_OF_UNDYING);
            this.put(48, ParticleEffect.UNDERWATER);
            this.put(49, ParticleEffect.SPLASH);
            this.put(50, ParticleEffect.WITCH);
            this.put(51, ParticleEffect.BUBBLE_POP);
            this.put(52, ParticleEffect.CURRENT_DOWN);
            this.put(53, ParticleEffect.BUBBLE_COLUMN_UP);
            this.put(54, ParticleEffect.NAUTILUS);
            this.put(55, ParticleEffect.DOLPHIN);
            this.put(56, ParticleEffect.CAMPFIRE_COSY_SMOKE);
            this.put(57, ParticleEffect.CAMPFIRE_SIGNAL_SMOKE);
            this.put(58, ParticleEffect.DRIPPING_HONEY);
            this.put(59, ParticleEffect.FALLING_HONEY);
            this.put(60, ParticleEffect.LANDING_HONEY);
            this.put(61, ParticleEffect.FALLING_NECTAR);
        }};
    }

    @Override
    public Map<Integer, ParticleEffect> getParticleEffectIdMapping() {
        return this.particleEffectNameMapping;
    }

}
