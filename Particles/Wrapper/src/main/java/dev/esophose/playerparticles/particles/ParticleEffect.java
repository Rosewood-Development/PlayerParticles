package dev.esophose.playerparticles.particles;

import java.util.Arrays;
import java.util.List;

public enum ParticleEffect {

    AMBIENT_ENTITY_EFFECT(ParticleProperty.REQUIRES_COLOR_DATA),
    ANGRY_VILLAGER,
    ASH,
    BARRIER,
    BLOCK(ParticleProperty.REQUIRES_BLOCK_DATA),
    BUBBLE,
    BUBBLE_COLUMN_UP,
    BUBBLE_POP,
    CAMPFIRE_COSY_SMOKE,
    CAMPFIRE_SIGNAL_SMOKE,
    CLOUD,
    COMPOSTER,
    CRIMSON_SPORE,
    CRIT,
    CURRENT_DOWN,
    DAMAGE_INDICATOR,
    DOLPHIN,
    DRAGON_BREATH,
    DRIPPING_HONEY,
    DRIPPING_LAVA,
    DRIPPING_OBSIDIAN_TEAR,
    DRIPPING_WATER,
    DUST(ParticleProperty.REQUIRES_COLOR_DATA),
    ELDER_GUARDIAN,
    ENCHANT,
    ENCHANTED_HIT,
    END_ROD,
    ENTITY_EFFECT(ParticleProperty.REQUIRES_COLOR_DATA),
    EXPLOSION,
    EXPLOSION_EMITTER,
    FALLING_DUST(ParticleProperty.REQUIRES_BLOCK_DATA),
    FALLING_HONEY,
    FALLING_LAVA,
    FALLING_NECTAR,
    FALLING_OBSIDIAN_TEAR,
    FALLING_WATER,
    FIREWORK,
    FISHING,
    FLAME,
    FLASH,
    FOOTSTEP,
    HAPPY_VILLAGER,
    HEART,
    INSTANT_EFFECT,
    ITEM(ParticleProperty.REQUIRES_ITEM_DATA),
    ITEM_SLIME,
    ITEM_SNOWBALL,
    LANDING_HONEY,
    LANDING_LAVA,
    LANDING_OBSIDIAN_TEAR,
    LARGE_SMOKE,
    LAVA,
    MYCELIUM,
    NAUTILUS,
    NOTE(ParticleProperty.REQUIRES_COLOR_DATA),
    POOF,
    PORTAL,
    RAIN,
    SMOKE,
    SNEEZE,
    SOUL,
    SOUL_FIRE_FLAME,
    SPELL, // The Minecraft internal name for this is actually "effect", but that's the command name, so it's SPELL for the plugin instead
    SPIT,
    SPLASH,
    SQUID_INK,
    SWEEP_ATTACK,
    TOTEM_OF_UNDYING,
    UNDERWATER,
    WARPED_SPORE,
    WITCH;

    private List<ParticleProperty> properties;

    ParticleEffect(ParticleProperty... properties) {
        this.properties = Arrays.asList(properties);
    }

    /**
     * Determine if this particle effect has a specific property
     *
     * @param property The property to check
     * @return Whether it has the property or not
     */
    public boolean hasProperty(ParticleProperty property) {
        return this.properties.contains(property);
    }

    /**
     * @return true if this ParticleEffect has properties, otherwise false
     */
    public boolean hasProperties() {
        return !this.properties.isEmpty();
    }

}
