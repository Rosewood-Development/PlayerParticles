package dev.esophose.playerparticles.particles;

import com.google.common.collect.ObjectArrays;
import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.particles.spawning.ParticleSpawner;
import dev.esophose.playerparticles.particles.spawning.ParticleSpawner.ParticleColorException;
import dev.esophose.playerparticles.particles.spawning.ParticleSpawner.ParticleDataException;
import dev.esophose.playerparticles.particles.spawning.ReflectiveParticleSpawner;
import dev.esophose.playerparticles.particles.spawning.SpigotParticleSpawner;
import dev.esophose.playerparticles.particles.spawning.reflective.ReflectiveParticleEffectMapping;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.rosewood.rosegarden.utils.NMSUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public enum ParticleEffect {

    // Ordered and named by their Minecraft 1.13+ internal names
    AMBIENT_ENTITY_EFFECT("SPELL_MOB_AMBIENT", Collections.singletonList("BEACON"), ParticleDataType.COLORABLE), // Removed in 1.20.5
    ANGRY_VILLAGER("VILLAGER_ANGRY", Collections.singletonList("IRON_DOOR")),
    ASH("ASH", Collections.singletonList("BLACKSTONE")),
    BARRIER("BARRIER", Collections.singletonList("BARRIER")), // Removed in 1.18 and replaced with BLOCK_MARKER
    BLOCK("BLOCK_CRACK", Collections.singletonList("STONE"), ParticleDataType.BLOCK),
    BLOCK_CRUMBLE("BLOCK_CRUMBLE", Arrays.asList("COOKIE", "CREAKING_HEART"), ParticleDataType.BLOCK),
    BLOCK_MARKER("BLOCK_MARKER", Collections.singletonList("LIGHT"), ParticleDataType.BLOCK),
    BUBBLE("WATER_BUBBLE", Arrays.asList("BUBBLE_CORAL", "GLASS")),
    BUBBLE_COLUMN_UP("BUBBLE_COLUMN_UP", Collections.singletonList("MAGMA_BLOCK")),
    BUBBLE_POP("BUBBLE_POP", Collections.singletonList("BUBBLE_CORAL_FAN")),
    CAMPFIRE_COSY_SMOKE("CAMPFIRE_COSY_SMOKE", Collections.singletonList("CAMPFIRE")),
    CAMPFIRE_SIGNAL_SMOKE("CAMPFIRE_SIGNAL_SMOKE", Collections.singletonList("REDSTONE_TORCH")),
    CHERRY_LEAVES("CHERRY_LEAVES", Collections.singletonList("CHERRY_LEAVES")),
    CLOUD("CLOUD", Arrays.asList("WHITE_WOOL", "WOOL")),
    COMPOSTER("COMPOSTER", Collections.singletonList("COMPOSTER")),
    COPPER_FIRE_FLAME("COPPER_FIRE_FLAME", Collections.singletonList("OXIDIZED_COPPER_LANTERN")),
    CRIMSON_SPORE("CRIMSON_SPORE", Collections.singletonList("CRIMSON_FUNGUS")),
    CRIT("CRIT", Collections.singletonList("IRON_SWORD")),
    CURRENT_DOWN("CURRENT_DOWN", Collections.singletonList("SOUL_SAND")),
    DAMAGE_INDICATOR("DAMAGE_INDICATOR", Collections.singletonList("BOW")),
    DOLPHIN("DOLPHIN", Collections.singletonList("DOLPHIN_SPAWN_EGG")),
    DRAGON_BREATH("DRAGON_BREATH", Arrays.asList("DRAGON_BREATH", "DRAGONS_BREATH")),
    DRIPPING_DRIPSTONE_LAVA("DRIPPING_DRIPSTONE_LAVA", Collections.singletonList("POINTED_DRIPSTONE")),
    DRIPPING_DRIPSTONE_WATER("DRIPPING_DRIPSTONE_WATER", Collections.singletonList("DRIPSTONE_BLOCK")),
    DRIPPING_HONEY("DRIPPING_HONEY", Collections.singletonList("BEE_NEST")),
    DRIPPING_LAVA("DRIP_LAVA", Collections.singletonList("LAVA_BUCKET")),
    DRIPPING_OBSIDIAN_TEAR("DRIPPING_OBSIDIAN_TEAR", Collections.singletonList("CRYING_OBSIDIAN")),
    DRIPPING_WATER("DRIP_WATER", Collections.singletonList("WATER_BUCKET")),
    DUST("REDSTONE", Collections.singletonList("REDSTONE"), ParticleDataType.COLORABLE),
    DUST_COLOR_TRANSITION("DUST_COLOR_TRANSITION", Collections.singletonList("DEEPSLATE_REDSTONE_ORE"), ParticleDataType.COLORABLE_TRANSITION),
    DUST_PILLAR("DUST_PILLAR", Collections.singletonList("CRACKED_STONE_BRICKS"), ParticleDataType.BLOCK),
    DUST_PLUME("DUST_PLUME", Collections.singletonList("BONE_MEAL")),
    EGG_CRACK("EGG_CRACK", Collections.singletonList("EGG")),
    ELDER_GUARDIAN("MOB_APPEARANCE", Arrays.asList("ELDER_GUARDIAN_SPAWN_EGG", "PRISMARINE_CRYSTALS"), false, ParticleDataType.NONE), // No thank you
    ELECTRIC_SPARK("ELECTRIC_SPARK", Collections.singletonList("LIGHTNING_ROD")),
    ENCHANT("ENCHANTMENT_TABLE", Arrays.asList("ENCHANTING_TABLE", "ENCHANTMENT_TABLE")),
    ENCHANTED_HIT("CRIT_MAGIC", Collections.singletonList("DIAMOND_SWORD")),
    END_ROD("END_ROD", Collections.singletonList("END_ROD")),
    ENTITY_EFFECT("SPELL_MOB", Collections.singletonList("GLOWSTONE_DUST"), NMSUtil.getVersionNumber() > 20 || (NMSUtil.getVersionNumber() == 20 && NMSUtil.getMinorVersionNumber() >= 5) ? ParticleDataType.COLORABLE_TRANSPARENCY : ParticleDataType.COLORABLE),
    EXPLOSION("EXPLOSION_LARGE", Arrays.asList("FIRE_CHARGE", "FIREBALL")),
    EXPLOSION_EMITTER("EXPLOSION_HUGE", Collections.singletonList("TNT")),
    FALLING_DRIPSTONE_LAVA("FALLING_DRIPSTONE_LAVA", Collections.singletonList("SMOOTH_BASALT")),
    FALLING_DRIPSTONE_WATER("FALLING_DRIPSTONE_WATER", Collections.singletonList("CALCITE")),
    FALLING_DUST("FALLING_DUST", Collections.singletonList("SAND"), ParticleDataType.BLOCK),
    FALLING_HONEY("FALLING_HONEY", Collections.singletonList("HONEY_BOTTLE")),
    FALLING_LAVA("FALLING_LAVA", Collections.singletonList("RED_DYE")),
    FALLING_NECTAR("FALLING_NECTAR", Collections.singletonList("HONEYCOMB")),
    FALLING_OBSIDIAN_TEAR("FALLING_OBSIDIAN_TEAR", Collections.singletonList("ANCIENT_DEBRIS")),
    FALLING_SPORE_BLOSSOM("FALLING_SPORE_BLOSSOM", Collections.singletonList("FLOWERING_AZALEA")),
    FALLING_WATER("FALLING_WATER", Collections.singletonList("BLUE_DYE")),
    FIREFLY("FIREFLY", Collections.singletonList("FIREFLY_BUSH")),
    FIREWORK("FIREWORKS_SPARK", Arrays.asList("FIREWORK_ROCKET", "FIREWORK")),
    FISHING("WATER_WAKE", Collections.singletonList("FISHING_ROD")),
    FLAME("FLAME", Collections.singletonList("BLAZE_POWDER")),
    FLASH("FLASH", Collections.singletonList("GOLD_INGOT"), false, NMSUtil.getVersionNumber() > 21 || (NMSUtil.getVersionNumber() == 21 && NMSUtil.getMinorVersionNumber() >= 9) ? ParticleDataType.COLORABLE_TRANSPARENCY : ParticleDataType.NONE), // Also no thank you
    GLOW("GLOW", Collections.singletonList("GLOW_ITEM_FRAME")),
    GLOW_SQUID_INK("GLOW_SQUID_INK", Collections.singletonList("GLOW_INK_SAC")),
    GUST("GUST", Collections.singletonList("FLOW_ARMOR_TRIM_SMITHING_TEMPLATE")),
    GUST_EMITTER_LARGE("GUST_EMITTER_LARGE", Collections.singletonList("MACE")),
    GUST_EMITTER_SMALL("GUST_EMITTER_SMALL", Collections.singletonList("HEAVY_CORE")),
    FOOTSTEP("FOOTSTEP", Collections.singletonList("GRASS")), // Removed in Minecraft 1.13 :(
    HAPPY_VILLAGER("VILLAGER_HAPPY", Arrays.asList("DARK_OAK_DOOR_ITEM", "DARK_OAK_DOOR")),
    HEART("HEART", Arrays.asList("POPPY", "RED_ROSE")),
    INFESTED("INFESTED", Collections.singletonList("INFESTED_MOSSY_STONE_BRICKS")),
    INSTANT_EFFECT("SPELL_INSTANT", Arrays.asList("SPLASH_POTION", "POTION"), ParticleDataType.COLORABLE),
    ITEM("ITEM_CRACK", Collections.singletonList("ITEM_FRAME"), ParticleDataType.ITEM),
    ITEM_COBWEB("ITEM_COBWEB", Arrays.asList("COBWEB", "WEB")),
    ITEM_SLIME("SLIME", Collections.singletonList("SLIME_BALL")),
    ITEM_SNOWBALL("SNOWBALL", Arrays.asList("SNOWBALL", "SNOW_BALL")),
    LANDING_HONEY("LANDING_HONEY", Collections.singletonList("HONEY_BLOCK")),
    LANDING_LAVA("LANDING_LAVA", Collections.singletonList("ORANGE_DYE")),
    LANDING_OBSIDIAN_TEAR("LANDING_OBSIDIAN_TEAR", Collections.singletonList("NETHERITE_BLOCK")),
    LARGE_SMOKE("SMOKE_LARGE", Collections.singletonList("STRING")),
    LAVA("LAVA", Collections.singletonList("MAGMA_CREAM")),
    LIGHT("LIGHT", Collections.singletonList("LIGHT")), // Removed in 1.18 and replaced with BLOCK_MARKER
    MYCELIUM("TOWN_AURA", Arrays.asList("MYCELIUM", "MYCEL")),
    NAUTILUS("NAUTILUS", Collections.singletonList("HEART_OF_THE_SEA")),
    NOTE("NOTE", Collections.singletonList("NOTE_BLOCK"), ParticleDataType.COLORABLE),
    OMINOUS_SPAWNING("OMINOUS_SPAWNING", Collections.singletonList("TRIAL_SPAWNER")),
    PALE_OAK_LEAVES("PALE_OAK_LEAVES", Collections.singletonList("PALE_OAK_LEAVES")),
    POOF("EXPLOSION_NORMAL", Arrays.asList("FIREWORK_STAR", "FIREWORK_CHARGE")), // The 1.13 combination of explode and showshovel
    PORTAL("PORTAL", Collections.singletonList("OBSIDIAN")),
    RAID_OMEN("RAID_OMEN", Collections.singletonList("OMINOUS_BOTTLE")),
    RAIN("WATER_DROP", Arrays.asList("PUFFERFISH_BUCKET", "LAPIS_BLOCK")),
    REVERSE_PORTAL("REVERSE_PORTAL", Collections.singletonList("FLINT_AND_STEEL")),
    SCRAPE("SCRAPE", Collections.singletonList("GOLDEN_AXE")),
    SCULK_CHARGE("SCULK_CHARGE", Collections.singletonList("SCULK")),
    SCULK_CHARGE_POP("SCULK_CHARGE_POP", Collections.singletonList("SCULK_VEIN")),
    SCULK_SOUL("SCULK_SOUL", Collections.singletonList("SCULK_CATALYST")),
    SHRIEK("SHRIEK", Collections.singletonList("SCULK_SHRIEKER")),
    SMALL_FLAME("SMALL_FLAME", Collections.singletonList("CANDLE")),
    SMALL_GUST("SMALL_GUST", Collections.singletonList("WIND_CHARGE")),
    SMOKE("SMOKE_NORMAL", Collections.singletonList("TORCH")),
    SNEEZE("SNEEZE", Collections.singletonList("BAMBOO")),
    SNOWFLAKE("SNOWFLAKE", Collections.singletonList("POWDER_SNOW_BUCKET")),
    SONIC_BOOM("SONIC_BOOM", Collections.singletonList("WARDEN_SPAWN_EGG")),
    SOUL("SOUL", Collections.singletonList("SOUL_LANTERN")),
    SOUL_FIRE_FLAME("SOUL_FIRE_FLAME", Collections.singletonList("SOUL_CAMPFIRE")),
    SPELL("SPELL", Arrays.asList("POTION", "GLASS_BOTTLE")), // The Minecraft internal name for this is actually "effect", but that's the command name, so it's SPELL for the plugin instead
    SPIT("SPIT", Arrays.asList("LLAMA_SPAWN_EGG", "PUMPKIN_SEEDS")),
    SPLASH("WATER_SPLASH", Arrays.asList("SALMON", "FISH", "RAW_FISH")),
    SPORE_BLOSSOM_AIR("SPORE_BLOSSOM_AIR", Collections.singletonList("SPORE_BLOSSOM")),
    SQUID_INK("SQUID_INK", Collections.singletonList("INK_SAC")),
    SWEEP_ATTACK("SWEEP_ATTACK", Arrays.asList("GOLDEN_SWORD", "GOLD_SWORD")),
    TINTED_LEAVES("TINTED_LEAVES", Collections.singletonList("LEAF_LITTER"), ParticleDataType.COLORABLE_TRANSPARENCY),
    TOTEM_OF_UNDYING("TOTEM", Arrays.asList("TOTEM_OF_UNDYING", "TOTEM")),
    TRAIL("TRAIL", Collections.singletonList("DIRT_PATH"), ParticleDataType.COLORABLE),
    TRIAL_OMEN("TRIAL_OMEN", Collections.singletonList("COPPER_BULB")),
    TRIAL_SPAWNER_DETECTION("TRIAL_SPAWNER_DETECTION", Collections.singletonList("TRIAL_KEY")),
    TRIAL_SPAWNER_DETECTION_OMINOUS("TRIAL_SPAWNER_DETECTION_OMINOUS", Collections.singletonList("OMINOUS_TRIAL_KEY")),
    UNDERWATER("SUSPENDED_DEPTH", Arrays.asList("TURTLE_HELMET", "SPONGE")),
    VAULT_CONNECTION("VAULT_CONNECTION", Collections.singletonList("VAULT")),
    VIBRATION("VIBRATION", Collections.singletonList("SCULK_SENSOR"), false, ParticleDataType.VIBRATION),
    WARPED_SPORE("WARPED_SPORE", Collections.singletonList("WARPED_FUNGUS")),
    WAX_OFF("WAX_OFF", Collections.singletonList("OXIDIZED_COPPER")),
    WAX_ON("WAX_ON", Collections.singletonList("WAXED_COPPER_BLOCK")),
    WHITE_ASH("WHITE_ASH", Collections.singletonList("BASALT")),
    WITCH("SPELL_WITCH", Collections.singletonList("CAULDRON"));

    private static final ParticleSpawner PARTICLE_SPAWNER = NMSUtil.getVersionNumber() >= 9 ? new SpigotParticleSpawner() : new ReflectiveParticleSpawner();

    private Particle internalEnum;
    private final ParticleDataType dataType;
    private boolean supported;

    private CommentedFileConfiguration config;
    private final boolean enabledByDefault;
    private final List<String> defaultIconMaterialNames;

    private String effectName;
    private boolean enabled;
    private Material guiIconMaterial;
    private boolean hiddenWhenLimited;

    /**
     * Construct a new particle effect with a required data type
     * 
     * @param enumName Name of the Spigot Particle enum
     * @param defaultIconMaterialNames The names of the Materials to display as GUI icons
     * @param dataType Data type of this particle effect
     */
    ParticleEffect(String enumName, List<String> defaultIconMaterialNames, ParticleDataType dataType) {
        this(enumName, defaultIconMaterialNames, true, dataType);
    }

    /**
     * Construct a new particle effect with no data
     *
     * @param enumName Name of the Spigot Particle enum
     * @param defaultIconMaterialNames The names of the Materials to display as GUI icons
     */
    ParticleEffect(String enumName, List<String> defaultIconMaterialNames) {
        this(enumName, defaultIconMaterialNames, true, ParticleDataType.NONE);
    }

    /**
     * Construct a new particle effect with a required data type
     *
     * @param enumName Name of the Spigot Particle enum
     * @param defaultIconMaterialNames The names of the Materials to display as GUI icons
     * @param enabledByDefault If the particle type is enabled by default
     * @param dataType Data type of this particle effect
     */
    ParticleEffect(String enumName, List<String> defaultIconMaterialNames, boolean enabledByDefault, ParticleDataType dataType) {
        this.defaultIconMaterialNames = defaultIconMaterialNames;
        this.enabledByDefault = enabledByDefault;
        this.dataType = dataType;
        this.hiddenWhenLimited = false;

        // Will be null if this server's version doesn't support this particle type
        if (NMSUtil.getVersionNumber() > 8) {
            if (enumName.equals("SPELL"))
                enumName = "EFFECT"; // Special case

            for (Particle value : Particle.values()) {
                String key = value.name();
                // Look up by enum name then by old enum name
                if (key.equals(this.name()) || key.equals(enumName)) {
                    this.internalEnum = value;
                    break;
                }
            }
            this.supported = this.internalEnum != null;
        } else {
            try {
                this.supported = ReflectiveParticleEffectMapping.valueOf(this.name()).isSupported();
            } catch (Exception e) {
                this.supported = false;
            }
        }

        this.setDefaultSettings();
        this.loadSettings(false);
    }

    /**
     * Sets the default settings for the particle type
     */
    private void setDefaultSettings() {
        if (!this.isSupported())
            return;

        File directory = new File(PlayerParticles.getInstance().getDataFolder(), "effects");
        directory.mkdirs();

        File file = new File(directory, this.getInternalName() + ".yml");
        this.config = CommentedFileConfiguration.loadConfiguration(file);

        boolean changed = this.setIfNotExists("effect-name", this.getInternalName(), "The name the effect will display as");
        changed |= this.setIfNotExists("enabled", this.enabledByDefault, "If the effect is enabled or not");
        changed |= this.setIfNotExists("gui-icon-material", this.defaultIconMaterialNames);
        changed |= this.setIfNotExists("hide-in-limited-region", false, "If true, this effect will be hidden when a player is in a WorldGuard region with the flag 'player-particles-limited' denied");

        if (changed)
            this.config.save(file);
    }

    /**
     * Loads the settings shared for each style then calls loadSettings(CommentedFileConfiguration)
     *
     * @param reloadConfig If the settings should be reloaded or not
     */
    public void loadSettings(boolean reloadConfig) {
        if (!this.isSupported())
            return;

        if (reloadConfig)
            this.setDefaultSettings();

        this.effectName = this.config.getString("effect-name");
        this.enabled = this.config.getBoolean("enabled");
        this.guiIconMaterial = ParticleUtils.closestMatchWithFallback(true, this.config.getStringList("gui-icon-material").toArray(new String[0]));
        this.hiddenWhenLimited = this.config.getBoolean("hide-in-limited-region");
    }

    /**
     * Sets a value to the config if it doesn't already exist
     *
     * @param setting The setting name
     * @param value The setting value
     * @param comments Comments for the setting
     * @return true if changes were made
     */
    private boolean setIfNotExists(String setting, Object value, String... comments) {
        if (this.config.get(setting) != null)
            return false;

        String defaultMessage = "Default: ";
        if (value instanceof String && ParticleUtils.containsConfigSpecialCharacters((String) value)) {
            defaultMessage += "'" + value + "'";
        } else {
            defaultMessage += value;
        }

        this.config.set(setting, value, ObjectArrays.concat(comments, new String[] { defaultMessage }, String.class));
        return true;
    }

    /**
     * Reloads the settings for all ParticleEffects
     */
    public static void reloadSettings() {
        for (ParticleEffect effect : values())
            effect.loadSettings(true);
    }

    /**
     * @return the internal name of this particle effect that will never change
     */
    public String getInternalName() {
        return this.name().toLowerCase();
    }

    /**
     * @return the name that the style will display to the users as
     */
    public String getName() {
        return this.effectName;
    }

    /**
     * @return the Spigot enum this represents
     */
    public Particle getSpigotEnum() {
        return this.internalEnum;
    }

    /**
     * @return The Material icon that represents this style in the GUI
     */
    public Material getGuiIconMaterial() {
        return this.guiIconMaterial;
    }

    /**
     * @return true if this effect is hidden when the player is in a limited region
     */
    public boolean isHiddenWhenLimited() {
        return this.hiddenWhenLimited;
    }

    /**
     * @return the data type this particle effect requires
     */
    public ParticleDataType getDataType() {
        return this.dataType;
    }

    /**
     * Determine if this particle effect is supported by the current server version
     * 
     * @return Whether the particle effect is supported or not
     */
    public boolean isSupported() {
        return this.supported;
    }

    /**
     * @return true if this effect is enabled, otherwise false
     */
    public boolean isEnabled() {
        return this.enabled;
    }

    /**
     * Returns a ParticleEffect List of all enabled effects for the server version
     * 
     * @return Enabled effects
     */
    public static List<ParticleEffect> getEnabledEffects() {
        List<ParticleEffect> effects = new ArrayList<>();
        for (ParticleEffect pe : values())
            if (pe.isSupported() && pe.isEnabled())
                effects.add(pe);
        return effects;
    }

    /**
     * Returns the particle effect with the given name
     * 
     * @param name Name of the particle effect
     * @return The particle effect
     */
    public static ParticleEffect fromName(String name) {
        return Stream.of(values()).filter(x -> x.isSupported() && x.isEnabled() && x.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    /**
     * Returns the particle effect with the given name
     *
     * @param internalName Internal name of the particle effect
     * @return The particle effect
     */
    public static ParticleEffect fromInternalName(String internalName) {
        return Stream.of(values()).filter(x -> x.isSupported() && x.isEnabled() && x.getInternalName().equalsIgnoreCase(internalName)).findFirst().orElse(null);
    }

    /**
     * Invokes the correct spawn method for the particle information given
     *
     * @param particle The ParticlePair, given the effect/style/data
     * @param pparticle The particle spawn information
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     */
    public static void display(ParticlePair particle, PParticle pparticle, boolean isLongRange, Player owner) {
        ParticleEffect effect = pparticle.getOverrideEffect() != null ? pparticle.getOverrideEffect() : particle.getEffect();
        int count = pparticle.isDirectional() ? 0 : 1;

        ParticleDataType dataType = effect.getDataType();
        switch (dataType) {
            case BLOCK:
            case ITEM:
                Material material;
                if (pparticle.getOverrideData() instanceof Material) {
                    material = (Material) pparticle.getOverrideData();
                } else {
                    material = particle.getSpawnMaterial();
                }
                effect.display(material, pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), pparticle.getSpeed(), 1, pparticle.getLocation(false), isLongRange, owner);
                break;
            case COLORABLE:
            case COLORABLE_TRANSPARENCY:
                ParticleColor color;
                if (pparticle.getOverrideData() instanceof NoteColor && particle.getEffect() == ParticleEffect.NOTE) {
                    color = (NoteColor) pparticle.getOverrideData();
                } else if (pparticle.getOverrideData() instanceof OrdinaryColor && particle.getEffect() != ParticleEffect.NOTE) {
                    color = (OrdinaryColor) pparticle.getOverrideData();
                } else {
                    color = particle.getSpawnColor(effect);
                }
                effect.display(color, pparticle.getLocation(true), isLongRange, owner, pparticle.getSize());
                break;
            case COLORABLE_TRANSITION:
                ColorTransition colorTransition;
                if (pparticle.getOverrideData() instanceof ColorTransition) {
                    colorTransition = (ColorTransition) pparticle.getOverrideData();
                } else {
                    colorTransition = particle.getSpawnColorTransition();
                }
                effect.display(colorTransition, pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), 1, pparticle.getLocation(false), isLongRange, owner, pparticle.getSize());
                break;
            case VIBRATION:
                Vibration vibration;
                if (pparticle.getOverrideData() instanceof Vibration) {
                    vibration = (Vibration) pparticle.getOverrideData();
                } else {
                    vibration = particle.getVibration();
                }
                effect.display(vibration, pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), 1, pparticle.getLocation(false), isLongRange, owner);
                break;
            default:
                effect.display(pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), pparticle.getSpeed(), count, pparticle.getLocation(false), isLongRange, owner);
                break;
        }
    }

    /**
     * Displays a particle effect
     *
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @throws ParticleDataException If the particle effect requires additional data
     */
    public void display(double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        PARTICLE_SPAWNER.display(this, offsetX, offsetY, offsetZ, speed, amount, center, isLongRange, owner);
    }

    /**
     * Displays a single particle which is colored
     * 
     * @param color Color of the particle
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @param size The size of the particles
     * @throws ParticleColorException If the particle effect is not colorable or the color type is incorrect
     */
    public void display(ParticleColor color, Location center, boolean isLongRange, Player owner, float size) {
        PARTICLE_SPAWNER.display(this, color, center, isLongRange, owner, size);
    }

    /**
     * Displays a particle effect which requires additional data and is only
     * visible for all players within a certain range in the world of @param
     * center
     * 
     * @param spawnMaterial Material of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @throws ParticleDataException If the particle effect does not require additional data or if the data type is incorrect
     */
    public void display(Material spawnMaterial, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        PARTICLE_SPAWNER.display(this, spawnMaterial, offsetX, offsetY, offsetZ, speed, amount, center, isLongRange, owner);
    }

    /**
     * Displays a particle effect which requires additional data and is only
     * visible for all players within a certain range in the world of @param
     * center
     *
     * @param colorTransition Color transition of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @param size The size of the particles
     * @throws ParticleDataException If the particle effect does not require additional data or if the data type is incorrect
     */
    public void display(ColorTransition colorTransition, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner, float size) {
        PARTICLE_SPAWNER.display(this, colorTransition, offsetX, offsetY, offsetZ, amount, center, isLongRange, owner, size);
    }

    /**
     * Displays a particle effect which requires additional data and is only
     * visible for all players within a certain range in the world of @param
     * center
     *
     * @param vibration Vibration of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @throws ParticleDataException If the particle effect does not require additional data or if the data type is incorrect
     */
    public void display(Vibration vibration, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner) {
        PARTICLE_SPAWNER.display(this, vibration, offsetX, offsetY, offsetZ, amount, center, isLongRange, owner);
    }

    /**
     * Represents the type of data that a particle effect can have applied
     */
    public enum ParticleDataType {
        /**
         * The particle effect requires blockdata to be displayed
         */
        BLOCK,
        /**
         * The particle effect requires an itemstack to be displayed
         */
        ITEM,
        /**
         * The particle effect uses a color value
         */
        COLORABLE,
        /**
         * The particle effect uses a color value that supports transparency
         */
        COLORABLE_TRANSPARENCY,
        /**
         * The particle effect uses two color values to transition between
         */
        COLORABLE_TRANSITION,
        /**
         * The particle effect uses an origin location, destination location, and duration in ticks
         */
        VIBRATION,
        /**
         * The particle effect does not have any data
         */
        NONE
    }

}
