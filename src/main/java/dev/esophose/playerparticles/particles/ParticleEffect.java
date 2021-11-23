package dev.esophose.playerparticles.particles;

import com.google.common.collect.ObjectArrays;
import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
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
import dev.esophose.playerparticles.util.NMSUtil;
import dev.esophose.playerparticles.util.ParticleUtils;
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
    AMBIENT_ENTITY_EFFECT("SPELL_MOB_AMBIENT", Collections.singletonList("BEACON"), ParticleProperty.COLORABLE),
    ANGRY_VILLAGER("VILLAGER_ANGRY", Collections.singletonList("IRON_DOOR")),
    ASH("ASH", Collections.singletonList("BLACKSTONE")),
    BARRIER("BARRIER", Collections.singletonList("BARRIER")),
    BLOCK("BLOCK_CRACK", Collections.singletonList("STONE"), ParticleProperty.REQUIRES_MATERIAL_DATA),
    BLOCK_MARKER("BLOCK_MARKER", Collections.singletonList("LIGHT"), ParticleProperty.REQUIRES_MATERIAL_DATA),
    BUBBLE("WATER_BUBBLE", Arrays.asList("BUBBLE_CORAL", "GLASS")),
    BUBBLE_COLUMN_UP("BUBBLE_COLUMN_UP", Collections.singletonList("MAGMA_BLOCK")),
    BUBBLE_POP("BUBBLE_POP", Collections.singletonList("BUBBLE_CORAL_FAN")),
    CAMPFIRE_COSY_SMOKE("CAMPFIRE_COSY_SMOKE", Collections.singletonList("CAMPFIRE")),
    CAMPFIRE_SIGNAL_SMOKE("CAMPFIRE_SIGNAL_SMOKE", Collections.singletonList("REDSTONE_TORCH")),
    CLOUD("CLOUD", Arrays.asList("WHITE_WOOL", "WOOL")),
    COMPOSTER("COMPOSTER", Collections.singletonList("COMPOSTER")),
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
    DUST("REDSTONE", Collections.singletonList("REDSTONE"), ParticleProperty.COLORABLE),
    DUST_COLOR_TRANSITION("DUST_COLOR_TRANSITION", Collections.singletonList("DEEPSLATE_REDSTONE_ORE"), ParticleProperty.COLORABLE_TRANSITION),
    ELDER_GUARDIAN("MOB_APPEARANCE", Arrays.asList("ELDER_GUARDIAN_SPAWN_EGG", "PRISMARINE_CRYSTALS"), false), // No thank you
    ELECTRIC_SPARK("ELECTRIC_SPARK", Collections.singletonList("LIGHTNING_ROD")),
    ENCHANT("ENCHANTMENT_TABLE", Arrays.asList("ENCHANTING_TABLE", "ENCHANTMENT_TABLE")),
    ENCHANTED_HIT("CRIT_MAGIC", Collections.singletonList("DIAMOND_SWORD")),
    END_ROD("END_ROD", Collections.singletonList("END_ROD")),
    ENTITY_EFFECT("SPELL_MOB", Collections.singletonList("GLOWSTONE_DUST"), ParticleProperty.COLORABLE),
    EXPLOSION("EXPLOSION_LARGE", Arrays.asList("FIRE_CHARGE", "FIREBALL")),
    EXPLOSION_EMITTER("EXPLOSION_HUGE", Collections.singletonList("TNT")),
    FALLING_DRIPSTONE_LAVA("FALLING_DRIPSTONE_LAVA", Collections.singletonList("SMOOTH_BASALT")),
    FALLING_DRIPSTONE_WATER("FALLING_DRIPSTONE_WATER", Collections.singletonList("CALCITE")),
    FALLING_DUST("FALLING_DUST", Collections.singletonList("SAND"), ParticleProperty.REQUIRES_MATERIAL_DATA),
    FALLING_HONEY("FALLING_HONEY", Collections.singletonList("HONEY_BOTTLE")),
    FALLING_LAVA("FALLING_LAVA", Collections.singletonList("RED_DYE")),
    FALLING_NECTAR("FALLING_NECTAR", Collections.singletonList("HONEYCOMB")),
    FALLING_OBSIDIAN_TEAR("FALLING_OBSIDIAN_TEAR", Collections.singletonList("ANCIENT_DEBRIS")),
    FALLING_SPORE_BLOSSOM("FALLING_SPORE_BLOSSOM", Collections.singletonList("FLOWERING_AZALEA")),
    FALLING_WATER("FALLING_WATER", Collections.singletonList("BLUE_DYE")),
    FIREWORK("FIREWORKS_SPARK", Arrays.asList("FIREWORK_ROCKET", "FIREWORK")),
    FISHING("WATER_WAKE", Collections.singletonList("FISHING_ROD")),
    FLAME("FLAME", Collections.singletonList("BLAZE_POWDER")),
    FLASH("FLASH", Collections.singletonList("GOLD_INGOT"), false), // Also no thank you
    GLOW("GLOW", Collections.singletonList("GLOW_ITEM_FRAME")),
    GLOW_SQUID_INK("GLOW_SQUID_INK", Collections.singletonList("GLOW_INK_SAC")),
    FOOTSTEP("FOOTSTEP", Collections.singletonList("GRASS")), // Removed in Minecraft 1.13 :(
    HAPPY_VILLAGER("VILLAGER_HAPPY", Arrays.asList("DARK_OAK_DOOR_ITEM", "DARK_OAK_DOOR")),
    HEART("HEART", Arrays.asList("POPPY", "RED_ROSE")),
    INSTANT_EFFECT("SPELL_INSTANT", Arrays.asList("SPLASH_POTION", "POTION")),
    ITEM("ITEM_CRACK", Collections.singletonList("ITEM_FRAME"), ParticleProperty.REQUIRES_MATERIAL_DATA),
    ITEM_SLIME("SLIME", Collections.singletonList("SLIME_BALL")),
    ITEM_SNOWBALL("SNOWBALL", Arrays.asList("SNOWBALL", "SNOW_BALL")),
    LANDING_HONEY("LANDING_HONEY", Collections.singletonList("HONEY_BLOCK")),
    LANDING_LAVA("LANDING_LAVA", Collections.singletonList("ORANGE_DYE")),
    LANDING_OBSIDIAN_TEAR("LANDING_OBSIDIAN_TEAR", Collections.singletonList("NETHERITE_BLOCK")),
    LARGE_SMOKE("SMOKE_LARGE", Arrays.asList("COBWEB", "WEB")),
    LAVA("LAVA", Collections.singletonList("MAGMA_CREAM")),
    LIGHT("LIGHT", Collections.singletonList("LIGHT")),
    MYCELIUM("TOWN_AURA", Arrays.asList("MYCELIUM", "MYCEL")),
    NAUTILUS("NAUTILUS", Collections.singletonList("HEART_OF_THE_SEA")),
    NOTE("NOTE", Collections.singletonList("NOTE_BLOCK"), ParticleProperty.COLORABLE),
    POOF("EXPLOSION_NORMAL", Arrays.asList("FIREWORK_STAR", "FIREWORK_CHARGE")), // The 1.13 combination of explode and showshovel
    PORTAL("PORTAL", Collections.singletonList("OBSIDIAN")),
    RAIN("WATER_DROP", Arrays.asList("PUFFERFISH_BUCKET", "LAPIS_BLOCK")),
    REVERSE_PORTAL("REVERSE_PORTAL", Collections.singletonList("FLINT_AND_STEEL")),
    SCRAPE("SCRAPE", Collections.singletonList("GOLDEN_AXE")),
    SMALL_FLAME("SMALL_FLAME", Collections.singletonList("CANDLE")),
    SMOKE("SMOKE_NORMAL", Collections.singletonList("TORCH")),
    SNEEZE("SNEEZE", Collections.singletonList("BAMBOO")),
    SNOWFLAKE("SNOWFLAKE", Collections.singletonList("POWDER_SNOW_BUCKET")),
    SOUL("SOUL", Collections.singletonList("SOUL_LANTERN")),
    SOUL_FIRE_FLAME("SOUL_FIRE_FLAME", Collections.singletonList("SOUL_CAMPFIRE")),
    SPELL("SPELL", Arrays.asList("POTION", "GLASS_BOTTLE")), // The Minecraft internal name for this is actually "effect", but that's the command name, so it's SPELL for the plugin instead
    SPIT("SPIT", Arrays.asList("LLAMA_SPAWN_EGG", "PUMPKIN_SEEDS")),
    SPLASH("WATER_SPLASH", Arrays.asList("SALMON", "FISH", "RAW_FISH")),
    SPORE_BLOSSOM_AIR("SPORE_BLOSSOM_AIR", Collections.singletonList("SPORE_BLOSSOM")),
    SQUID_INK("SQUID_INK", Collections.singletonList("INK_SAC")),
    SWEEP_ATTACK("SWEEP_ATTACK", Arrays.asList("GOLDEN_SWORD", "GOLD_SWORD")),
    TOTEM_OF_UNDYING("TOTEM", Arrays.asList("TOTEM_OF_UNDYING", "TOTEM")),
    UNDERWATER("SUSPENDED_DEPTH", Arrays.asList("TURTLE_HELMET", "SPONGE")),
    VIBRATION("VIBRATION", Collections.singletonList("SCULK_SENSOR"), false, ParticleProperty.VIBRATION),
    WARPED_SPORE("WARPED_SPORE", Collections.singletonList("WARPED_FUNGUS")),
    WAX_OFF("WAX_OFF", Collections.singletonList("OXIDIZED_COPPER")),
    WAX_ON("WAX_ON", Collections.singletonList("WAXED_COPPER_BLOCK")),
    WHITE_ASH("WHITE_ASH", Collections.singletonList("BASALT")),
    WITCH("SPELL_WITCH", Collections.singletonList("CAULDRON"));

    private final static ParticleSpawner particleSpawner = NMSUtil.getVersionNumber() >= 9 ? new SpigotParticleSpawner() : new ReflectiveParticleSpawner();

    private Particle internalEnum;
    private List<ParticleProperty> properties;
    private boolean supported;

    private CommentedFileConfiguration config;
    private boolean enabledByDefault;
    private List<String> defaultIconMaterialNames;

    private String effectName;
    private boolean enabled;
    private Material guiIconMaterial;

    /**
     * Construct a new particle effect
     * 
     * @param enumName Name of the Spigot Particle enum
     * @param defaultIconMaterialNames The names of the Materials to display as GUI icons
     * @param properties Properties of this particle effect
     */
    ParticleEffect(String enumName, List<String> defaultIconMaterialNames, ParticleProperty... properties) {
        this(enumName, defaultIconMaterialNames, true, properties);
    }

    /**
     * Construct a new particle effect
     *
     * @param enumName Name of the Spigot Particle enum
     * @param defaultIconMaterialNames The names of the Materials to display as GUI icons
     * @param enabledByDefault If the particle type is enabled by default
     * @param properties Properties of this particle effect
     */
    ParticleEffect(String enumName, List<String> defaultIconMaterialNames, boolean enabledByDefault, ParticleProperty... properties) {
        this.defaultIconMaterialNames = defaultIconMaterialNames;
        this.enabledByDefault = enabledByDefault;
        this.properties = Arrays.asList(properties);

        // Will be null if this server's version doesn't support this particle type
        if (NMSUtil.getVersionNumber() > 8) {
            this.internalEnum = Stream.of(Particle.values()).filter(x -> x.name().equals(enumName)).findFirst().orElse(null);
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
        this.config = CommentedFileConfiguration.loadConfiguration(PlayerParticles.getInstance(), file);

        boolean changed = this.setIfNotExists("effect-name", this.getInternalName(), "The name the effect will display as");
        changed |= this.setIfNotExists("enabled", this.enabledByDefault, "If the effect is enabled or not");
        changed |= this.setIfNotExists("gui-icon-material", this.defaultIconMaterialNames);

        if (changed)
            this.config.save();
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
     * Determine if this particle effect has a specific property
     * 
     * @param property The property to check
     * @return Whether it has the property or not
     */
    public boolean hasProperty(ParticleProperty property) {
        return this.properties.contains(property);
    }

    /**
     * @return true if this effect has any properties
     */
    public boolean hasProperties() {
        return !this.properties.isEmpty();
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
        ParticleEffect effect = particle.getEffect();
        int count = pparticle.isDirectional() ? 0 : 1;

        if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            Material data;
            if (pparticle.getOverrideData() instanceof Material) {
                data = (Material) pparticle.getOverrideData();
            } else {
                data = particle.getSpawnMaterial();
            }
            effect.display(data, pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), pparticle.getSpeed(), 1, pparticle.getLocation(false), isLongRange, owner);
        } else if (effect.hasProperty(ParticleProperty.COLORABLE)) {
            ParticleColor data;
            if (pparticle.getOverrideData() instanceof NoteColor && particle.getEffect() == ParticleEffect.NOTE) {
                data = (NoteColor) pparticle.getOverrideData();
            } else if (pparticle.getOverrideData() instanceof OrdinaryColor && particle.getEffect() != ParticleEffect.NOTE) {
                data = (OrdinaryColor) pparticle.getOverrideData();
            } else {
                data = particle.getSpawnColor();
            }
            effect.display(data, pparticle.getLocation(true), isLongRange, owner, pparticle.getSize());
        } else if (effect.hasProperty(ParticleProperty.COLORABLE_TRANSITION)) {
            ColorTransition data;
            if (pparticle.getOverrideData() instanceof ColorTransition) {
                data = (ColorTransition) pparticle.getOverrideData();
            } else {
                data = particle.getSpawnColorTransition();
            }
            effect.display(data, pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), 1, pparticle.getLocation(false), isLongRange, owner, pparticle.getSize());
        } else if (effect.hasProperty(ParticleProperty.VIBRATION)) {
            Vibration data;
            if (pparticle.getOverrideData() instanceof Vibration) {
                data = (Vibration) pparticle.getOverrideData();
            } else {
                data = particle.getVibration();
            }
            effect.display(data, pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), 1, pparticle.getLocation(false), isLongRange, owner);
        } else {
            effect.display(pparticle.getXOff(), pparticle.getYOff(), pparticle.getZOff(), pparticle.getSpeed(), count, pparticle.getLocation(false), isLongRange, owner);
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
        particleSpawner.display(this, offsetX, offsetY, offsetZ, speed, amount, center, isLongRange, owner);
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
        particleSpawner.display(this, color, center, isLongRange, owner, size);
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
        particleSpawner.display(this, spawnMaterial, offsetX, offsetY, offsetZ, speed, amount, center, isLongRange, owner);
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
        particleSpawner.display(this, colorTransition, offsetX, offsetY, offsetZ, amount, center, isLongRange, owner, size);
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
        particleSpawner.display(this, vibration, offsetX, offsetY, offsetZ, amount, center, isLongRange, owner);
    }

    /**
     * Represents the property of a particle effect
     */
    public enum ParticleProperty {
        /**
         * The particle effect requires block or item material data to be displayed
         */
        REQUIRES_MATERIAL_DATA,
        /**
         * The particle effect uses the offsets as color values
         */
        COLORABLE,
        /**
         * The particle effect uses two color values to transition between
         */
        COLORABLE_TRANSITION,
        /**
         * The particle effect uses an origin location, destination location, and duration in ticks
         */
        VIBRATION
    }

}
