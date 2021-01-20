package dev.esophose.playerparticles.particles;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum ParticleEffect {

    AMBIENT_ENTITY_EFFECT(Collections.singletonList("BEACON"), ParticleProperty.REQUIRES_COLOR_DATA),
    ANGRY_VILLAGER(Collections.singletonList("IRON_DOOR")),
    ASH(Collections.singletonList("BLACKSTONE")),
    BARRIER(Collections.singletonList("BARRIER")),
    BLOCK(Collections.singletonList("STONE"), ParticleProperty.REQUIRES_BLOCK_DATA),
    BUBBLE(Arrays.asList("BUBBLE_CORAL", "GLASS")),
    BUBBLE_COLUMN_UP(Collections.singletonList("MAGMA_BLOCK")),
    BUBBLE_POP(Collections.singletonList("BUBBLE_CORAL_FAN")),
    CAMPFIRE_COSY_SMOKE(Collections.singletonList("CAMPFIRE")),
    CAMPFIRE_SIGNAL_SMOKE(Collections.singletonList("REDSTONE_TORCH")),
    CLOUD(Arrays.asList("WHITE_WOOL", "WOOL")),
    COMPOSTER(Collections.singletonList("COMPOSTER")),
    CRIMSON_SPORE(Collections.singletonList("CRIMSON_SPORE")),
    CRIT(Collections.singletonList("IRON_SWORD")),
    CURRENT_DOWN(Collections.singletonList("SOUL_SAND")),
    DAMAGE_INDICATOR(Collections.singletonList("BOW")),
    DOLPHIN(Collections.singletonList("DOLPHIN_SPAWN_EGG")),
    DRAGON_BREATH(Arrays.asList("DRAGON_BREATH", "DRAGONS_BREATH")),
    DRIPPING_DRIPSTONE_LAVA(Collections.singletonList("POINTED_DRIPSTONE")),
    DRIPPING_DRIPSTONE_WATER(Collections.singletonList("DRIPSTONE_BLOCK")),
    DRIPPING_HONEY(Collections.singletonList("BEE_NEST")),
    DRIPPING_LAVA(Collections.singletonList("LAVA_BUCKET")),
    DRIPPING_OBSIDIAN_TEAR(Collections.singletonList("CRYING_OBSIDIAN")),
    DRIPPING_WATER(Collections.singletonList("WATER_BUCKET")),
    DUST(Collections.singletonList("REDSTONE"), ParticleProperty.REQUIRES_COLOR_DATA),
    DUST_COLOR_TRANSITION(Collections.singletonList("REDSTONE_REPEATER"), ParticleProperty.REQUIRES_COLOR_DATA),
    ELDER_GUARDIAN(Arrays.asList("ELDER_GUARDIAN_SPAWN_EGG", "PRISMARINE_CRYSTALS")),
    ENCHANT(Arrays.asList("ENCHANTING_TABLE", "ENCHANTMENT_TABLE")),
    ENCHANTED_HIT(Collections.singletonList("DIAMOND_SWORD")),
    END_ROD(Collections.singletonList("END_ROD")),
    ENTITY_EFFECT(Collections.singletonList("GLOWSTONE_DUST"), ParticleProperty.REQUIRES_COLOR_DATA),
    EXPLOSION(Arrays.asList("FIRE_CHARGE", "FIREBALL")),
    EXPLOSION_EMITTER(Collections.singletonList("TNT")),
    FALLING_DRIPSTONE_LAVA(Collections.singletonList("AMETHYST_SHARD")),
    FALLING_DRIPSTONE_WATER(Collections.singletonList("AMETHYST_CLUSTER")),
    FALLING_DUST(Collections.singletonList("SAND"), ParticleProperty.REQUIRES_BLOCK_DATA),
    FALLING_HONEY(Collections.singletonList("HONEY_BOTTLE")),
    FALLING_LAVA(Collections.singletonList("RED_DYE")),
    FALLING_NECTAR(Collections.singletonList("HONEYCOMB")),
    FALLING_OBSIDIAN_TEAR(Collections.singletonList("ANCIENT_DEBRIS")),
    FALLING_WATER(Collections.singletonList("BLUE_DYE")),
    FIREWORK(Arrays.asList("FIREWORK_ROCKET", "FIREWORK")),
    FISHING(Collections.singletonList("FISHING_ROD")),
    FLAME(Collections.singletonList("BLAZE_POWDER")),
    FLASH(Collections.singletonList("GOLD_INGOT")),
    FOOTSTEP(Collections.singletonList("GRASS")),
    GLOW_SQUID_INK(Collections.singletonList("GLOW_SQUID_SPAWN_EGG")),
    GLOW(Collections.singletonList("GLOW_INK_SAC")),
    HAPPY_VILLAGER(Arrays.asList("DARK_OAK_DOOR_ITEM", "DARK_OAK_DOOR")),
    HEART(Arrays.asList("POPPY", "RED_ROSE")),
    INSTANT_EFFECT(Arrays.asList("SPLASH_POTION", "POTION")),
    ITEM(Collections.singletonList("ITEM_FRAME"), ParticleProperty.REQUIRES_ITEM_DATA),
    ITEM_SLIME(Collections.singletonList("SLIME_BALL")),
    ITEM_SNOWBALL(Arrays.asList("SNOWBALL", "SNOW_BALL")),
    LANDING_HONEY(Collections.singletonList("HONEY_BLOCK")),
    LANDING_LAVA(Collections.singletonList("ORANGE_DYE")),
    LANDING_OBSIDIAN_TEAR(Collections.singletonList("NETHERITE_BLOCK")),
    LARGE_SMOKE(Arrays.asList("COBWEB", "WEB")),
    LAVA(Collections.singletonList("MAGMA_CREAM")),
    MYCELIUM(Arrays.asList("MYCELIUM", "MYCEL")),
    NAUTILUS(Collections.singletonList("HEART_OF_THE_SEA")),
    NOTE(Collections.singletonList("NOTE_BLOCK"), ParticleProperty.REQUIRES_COLOR_DATA),
    POOF(Arrays.asList("FIREWORK_STAR", "FIREWORK_CHARGE")),
    PORTAL(Collections.singletonList("OBSIDIAN")),
    RAIN(Arrays.asList("PUFFERFISH_BUCKET", "LAPIS_BLOCK")),
    REVERSE_PORTAL(Collections.singletonList("FLINT_AND_STEEL")),
    SMALL_FLAME(Collections.singletonList("CANDLE")),
    SMOKE(Collections.singletonList("TORCH")),
    SNEEZE(Collections.singletonList("BAMBOO")),
    SNOWFLAKE(Collections.singletonList("POWDER_SNOW_BUCKET")),
    SOUL(Collections.singletonList("SOUL_LANTERN")),
    SOUL_FIRE_FLAME(Collections.singletonList("SOUL_CAMPFIRE")),
    SPELL(Arrays.asList("POTION", "GLASS_BOTTLE")), // The Minecraft internal name for this is actually "effect", but that's the command name, so it's SPELL for the plugin instead
    SPIT(Arrays.asList("LLAMA_SPAWN_EGG", "PUMPKIN_SEEDS")),
    SPLASH(Arrays.asList("SALMON", "FISH", "RAW_FISH")),
    SQUID_INK(Collections.singletonList("INK_SAC")),
    SWEEP_ATTACK(Arrays.asList("GOLDEN_SWORD", "GOLD_SWORD")),
    TOTEM_OF_UNDYING(Arrays.asList("TOTEM_OF_UNDYING", "TOTEM")),
    UNDERWATER(Arrays.asList("TURTLE_HELMET", "SPONGE")),
    VIBRATION(Collections.singletonList("SCULK_SENSOR")),
    WARPED_SPORE(Collections.singletonList("WARPED_FUNGUS")),
    WHITE_ASH(Collections.singletonList("BASALT")),
    WITCH(Collections.singletonList("CAULDRON"));

    private List<ParticleProperty> properties;
    private List<String> defaultIconMaterialNames;

    ParticleEffect(List<String> defaultIconMaterialNames, ParticleProperty... properties) {
        this.defaultIconMaterialNames = defaultIconMaterialNames;
        this.properties = Arrays.asList(properties);
    }

    /**
     * @return The Material icon that represents this style in the GUI
     */
    public List<String> getGuiIconMaterialNames() {
        return this.defaultIconMaterialNames;
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
