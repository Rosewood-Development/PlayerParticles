package dev.esophose.playerparticles.particles.version;

import dev.esophose.playerparticles.particles.ParticleEffect;
import java.util.HashMap;
import java.util.Map;

public class VersionMapping16 extends VersionMapping {

    private final Map<Integer, ParticleEffect> particleEffectNameMapping;

    public VersionMapping16() {
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
            this.put(27, ParticleEffect.SOUL_FIRE_FLAME);
            this.put(28, ParticleEffect.SOUL);
            this.put(29, ParticleEffect.FLASH);
            this.put(30, ParticleEffect.HAPPY_VILLAGER);
            this.put(31, ParticleEffect.COMPOSTER);
            this.put(32, ParticleEffect.HEART);
            this.put(33, ParticleEffect.INSTANT_EFFECT);
            this.put(34, ParticleEffect.ITEM);
            this.put(35, ParticleEffect.ITEM_SLIME);
            this.put(36, ParticleEffect.ITEM_SNOWBALL);
            this.put(37, ParticleEffect.LARGE_SMOKE);
            this.put(38, ParticleEffect.LAVA);
            this.put(39, ParticleEffect.MYCELIUM);
            this.put(40, ParticleEffect.NOTE);
            this.put(41, ParticleEffect.POOF);
            this.put(42, ParticleEffect.PORTAL);
            this.put(43, ParticleEffect.RAIN);
            this.put(44, ParticleEffect.SMOKE);
            this.put(45, ParticleEffect.SNEEZE);
            this.put(46, ParticleEffect.SPIT);
            this.put(47, ParticleEffect.SQUID_INK);
            this.put(48, ParticleEffect.SWEEP_ATTACK);
            this.put(49, ParticleEffect.TOTEM_OF_UNDYING);
            this.put(50, ParticleEffect.UNDERWATER);
            this.put(51, ParticleEffect.SPLASH);
            this.put(52, ParticleEffect.WITCH);
            this.put(53, ParticleEffect.BUBBLE_POP);
            this.put(54, ParticleEffect.CURRENT_DOWN);
            this.put(55, ParticleEffect.BUBBLE_COLUMN_UP);
            this.put(56, ParticleEffect.NAUTILUS);
            this.put(57, ParticleEffect.DOLPHIN);
            this.put(58, ParticleEffect.CAMPFIRE_COSY_SMOKE);
            this.put(59, ParticleEffect.CAMPFIRE_SIGNAL_SMOKE);
            this.put(60, ParticleEffect.DRIPPING_HONEY);
            this.put(61, ParticleEffect.FALLING_HONEY);
            this.put(62, ParticleEffect.LANDING_HONEY);
            this.put(63, ParticleEffect.FALLING_NECTAR);
            this.put(64, ParticleEffect.ASH);
            this.put(65, ParticleEffect.CRIMSON_SPORE);
            this.put(66, ParticleEffect.WARPED_SPORE);
            this.put(67, ParticleEffect.DRIPPING_OBSIDIAN_TEAR);
            this.put(68, ParticleEffect.FALLING_OBSIDIAN_TEAR);
            this.put(69, ParticleEffect.LANDING_OBSIDIAN_TEAR);
            this.put(70, ParticleEffect.REVERSE_PORTAL);
            this.put(71, ParticleEffect.WHITE_ASH);
        }};
    }

    @Override
    public Map<Integer, ParticleEffect> getParticleEffectIdMapping() {
        return this.particleEffectNameMapping;
    }

}
