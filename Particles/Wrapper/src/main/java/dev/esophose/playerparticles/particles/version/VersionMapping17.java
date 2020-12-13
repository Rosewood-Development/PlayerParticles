package dev.esophose.playerparticles.particles.version;

import dev.esophose.playerparticles.particles.ParticleEffect;
import java.util.HashMap;
import java.util.Map;

public class VersionMapping17 extends VersionMapping {

    private final Map<Integer, ParticleEffect> particleEffectNameMapping;

    public VersionMapping17() {
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

            this.put(15, ParticleEffect.DUST_COLOR_TRANSITION);

            this.put(16, ParticleEffect.SPELL);
            this.put(17, ParticleEffect.ELDER_GUARDIAN);
            this.put(18, ParticleEffect.ENCHANTED_HIT);
            this.put(19, ParticleEffect.ENCHANT);
            this.put(20, ParticleEffect.END_ROD);
            this.put(21, ParticleEffect.ENTITY_EFFECT);
            this.put(22, ParticleEffect.EXPLOSION_EMITTER);
            this.put(23, ParticleEffect.EXPLOSION);
            this.put(24, ParticleEffect.FALLING_DUST);
            this.put(25, ParticleEffect.FIREWORK);
            this.put(26, ParticleEffect.FISHING);
            this.put(27, ParticleEffect.FLAME);
            this.put(28, ParticleEffect.SOUL_FIRE_FLAME);
            this.put(29, ParticleEffect.SOUL);
            this.put(30, ParticleEffect.FLASH);
            this.put(31, ParticleEffect.HAPPY_VILLAGER);
            this.put(32, ParticleEffect.COMPOSTER);
            this.put(33, ParticleEffect.HEART);
            this.put(34, ParticleEffect.INSTANT_EFFECT);
            this.put(35, ParticleEffect.ITEM);

            this.put(36, ParticleEffect.VIBRATION);

            this.put(37, ParticleEffect.ITEM_SLIME);
            this.put(38, ParticleEffect.ITEM_SNOWBALL);
            this.put(39, ParticleEffect.LARGE_SMOKE);
            this.put(40, ParticleEffect.LAVA);
            this.put(41, ParticleEffect.MYCELIUM);
            this.put(42, ParticleEffect.NOTE);
            this.put(43, ParticleEffect.POOF);
            this.put(44, ParticleEffect.PORTAL);
            this.put(45, ParticleEffect.RAIN);
            this.put(46, ParticleEffect.SMOKE);
            this.put(47, ParticleEffect.SNEEZE);
            this.put(48, ParticleEffect.SPIT);
            this.put(49, ParticleEffect.SQUID_INK);
            this.put(50, ParticleEffect.SWEEP_ATTACK);
            this.put(51, ParticleEffect.TOTEM_OF_UNDYING);
            this.put(52, ParticleEffect.UNDERWATER);
            this.put(53, ParticleEffect.SPLASH);
            this.put(54, ParticleEffect.WITCH);
            this.put(55, ParticleEffect.BUBBLE_POP);
            this.put(56, ParticleEffect.CURRENT_DOWN);
            this.put(57, ParticleEffect.BUBBLE_COLUMN_UP);
            this.put(58, ParticleEffect.NAUTILUS);
            this.put(59, ParticleEffect.DOLPHIN);
            this.put(60, ParticleEffect.CAMPFIRE_COSY_SMOKE);
            this.put(61, ParticleEffect.CAMPFIRE_SIGNAL_SMOKE);
            this.put(62, ParticleEffect.DRIPPING_HONEY);
            this.put(63, ParticleEffect.FALLING_HONEY);
            this.put(64, ParticleEffect.LANDING_HONEY);
            this.put(65, ParticleEffect.FALLING_NECTAR);
            this.put(66, ParticleEffect.ASH);
            this.put(67, ParticleEffect.CRIMSON_SPORE);
            this.put(68, ParticleEffect.WARPED_SPORE);
            this.put(69, ParticleEffect.DRIPPING_OBSIDIAN_TEAR);
            this.put(70, ParticleEffect.FALLING_OBSIDIAN_TEAR);
            this.put(71, ParticleEffect.LANDING_OBSIDIAN_TEAR);
            this.put(72, ParticleEffect.REVERSE_PORTAL);
            this.put(73, ParticleEffect.WHITE_ASH);

            this.put(74, ParticleEffect.SMALL_FLAME);
            this.put(75, ParticleEffect.SNOWFLAKE);
            this.put(76, ParticleEffect.DRIPPING_DRIPSTONE_LAVA);
            this.put(77, ParticleEffect.FALLING_DRIPSTONE_LAVA);
            this.put(78, ParticleEffect.DRIPPING_DRIPSTONE_WATER);
            this.put(79, ParticleEffect.FALLING_DRIPSTONE_WATER);
        }};
    }

    @Override
    public Map<Integer, ParticleEffect> getParticleEffectIdMapping() {
        return this.particleEffectNameMapping;
    }

}
