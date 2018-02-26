package com.esophose.playerparticles.particles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

/**
 * <b>ParticleEffect Library</b>
 * <p>
 * This library was created by @DarkBlade12 and allows you to display all Minecraft particle effects on a Bukkit server
 * <p>
 * You are welcome to use it, modify it and redistribute it under the following conditions:
 * <ul>
 * <li>Don't claim this class as your own
 * <li>Don't remove this disclaimer
 * </ul>
 * <p>
 * Special thanks:
 * <ul>
 * <li>@microgeek (original idea, names and packet parameters)
 * <li>@ShadyPotato (1.8 names, ids and packet parameters)
 * <li>@RingOfStorms (particle behavior)
 * <li>@Cybermaxke (particle behavior)
 * <li>@JamieSinn (hosting a jenkins server and documentation for ParticleEffect)
 * </ul>
 * <p>
 * <i>It would be nice if you provide credit to me if you use this class in a published project</i>
 * 
 * @author DarkBlade12
 * @version 1.7
 */

/**
 * Heavily modified to work with the Spigot Particle API
 * 
 * @author (of changes) Esophose
 */
public enum ParticleEffect {

    NONE("none", ""), // Custom effect type, has no Spigot Particle Enum
    EXPLODE("explode", "EXPLOSION_NORMAL"),
    LARGE_EXPLODE("largeexplode", "EXPLOSION_LARGE"),
    HUGE_EXPLOSION("hugeexplosion", "EXPLOSION_HUGE"),
    FIREWORKS_SPARK("fireworksSpark", "FIREWORKS_SPARK"),
    BUBBLE("bubble", "WATER_BUBBLE"),
    //SPLASH("splash", "WATER_SPLASH"), // Basically the same thing as droplet, just goes a tiny bit higher
    WAKE("wake", "WATER_WAKE"),
    SUSPENDED("suspended", "SUSPENDED"),
    DEPTH_SUSPEND("depthSuspend", "SUSPENDED_DEPTH"),
    CRIT("crit", "CRIT"),
    MAGIC_CRIT("magicCrit", "CRIT_MAGIC"),
    SMOKE("smoke", "SMOKE_NORMAL"),
    LARGE_SMOKE("largesmoke", "SMOKE_LARGE"),
    SPELL("spell", "SPELL"),
    INSTANT_SPELL("instantSpell", "SPELL_INSTANT"),
    MOB_SPELL("mobSpell", "SPELL_MOB", ParticleProperty.COLORABLE),
    MOB_SPELL_AMBIENT("mobSpellAmbient", "SPELL_MOB_AMBIENT", ParticleProperty.COLORABLE),
    WITCH_MAGIC("witchMagic", "SPELL_WITCH"),
    DRIP_WATER("dripWater", "DRIP_WATER"),
    DRIP_LAVA("dripLava", "DRIP_LAVA"),
    ANGRY_VILLAGER("angryVillager", "VILLAGER_ANGRY"),
    HAPPY_VILLAGER("happyVillager", "VILLAGER_HAPPY"),
    // TOWN_AURA("townaura", "TOWN_AURA", ParticleProperty.DIRECTIONAL), // Same thing as depthsuspend
    NOTE("note", "NOTE", ParticleProperty.COLORABLE),
    PORTAL("portal", "PORTAL"),
    ENCHANTMENT_TABLE("enchantmenttable", "ENCHANTMENT_TABLE"),
    FLAME("flame", "FLAME"),
    LAVA("lava", "LAVA"),
    FOOTSTEP("footstep", "FOOTSTEP"),
    CLOUD("cloud", "CLOUD"),
    RED_DUST("reddust", "REDSTONE", ParticleProperty.COLORABLE),
    SNOWBALL_POOF("snowballpoof", "SNOWBALL"),
    SNOW_SHOVEL("snowshovel", "SNOW_SHOVEL"),
    SLIME("slime", "SLIME"),
    HEART("heart", "HEART"),
    BARRIER("barrier", "BARRIER"),
    ITEM_CRACK("iconcrack", "ITEM_CRACK", ParticleProperty.REQUIRES_DATA),
    BLOCK_CRACK("blockcrack", "BLOCK_CRACK", ParticleProperty.REQUIRES_DATA),
    BLOCK_DUST("blockdust", "BLOCK_DUST", ParticleProperty.REQUIRES_DATA),
    DROPLET("droplet", "WATER_DROP"),
    // ITEM_TAKE("take", "ITEM_TAKE"), // Doesn't do anything
    // MOB_APPEARANCE("mobappearance", "MOB_APPEARANCE"), // Looks ridiculous
    DRAGON_BREATH("dragonbreath", "DRAGON_BREATH"),
    END_ROD("endrod", "END_ROD"),
    DAMAGE_INDICATOR("damageindicator", "DAMAGE_INDICATOR"),
    SWEEP_ATTACK("sweepattack", "SWEEP_ATTACK"),
    FALLING_DUST("fallingdust", "FALLING_DUST", ParticleProperty.REQUIRES_DATA),
    TOTEM("totem", "TOTEM"),
    SPIT("spit", "SPIT");

    private static final Map<String, ParticleEffect> NAME_MAP = new HashMap<String, ParticleEffect>();
    private static final Map<Integer, ParticleEffect> ID_MAP = new HashMap<Integer, ParticleEffect>();
    private final String name;
    private final String spigotEnumName;
    private final Particle spigotEnum;
    private final List<ParticleProperty> properties;

    // Initialize map for quick name and id lookup
    static {
        for (ParticleEffect effect : values()) {
            NAME_MAP.put(effect.name, effect);
        }
    }

    /**
     * Construct a new particle effect
     * 
     * @param name Name of this particle effect
     * @param spigotEnumName Name of the Spigot Particle Enum
     * @param properties Properties of this particle effect
     */
    private ParticleEffect(String name, String spigotEnumName, ParticleProperty... properties) {
        this.name = name;
        this.spigotEnumName = spigotEnumName;
        this.properties = Arrays.asList(properties);

        Particle matchingSpigotEnum = null;
        for (Particle particle : Particle.values()) {
            if (particle.name().equals(spigotEnumName)) {
                matchingSpigotEnum = particle;
                break;
            }
        }
        this.spigotEnum = matchingSpigotEnum; // Will be null if this server's version doesn't support this particle type
    }

    /**
     * Returns the name of this particle effect
     * 
     * @return The name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the name of this particle effect's Spigot Particle Enum
     * 
     * @return The name of the Spigot Particle Enum this ParticleEffect represents
     */
    public String getSpigotEnumName() {
        return spigotEnumName;
    }

    /**
     * Gets the Spigot Particle Enum version of this ParticleEffect
     * 
     * @return The Spigot Particle Enum this ParticleEffect represents
     */
    public Particle getSpigotEnum() {
        return spigotEnum;
    }

    /**
     * Determine if this particle effect has a specific property
     * 
     * @param property The property to check
     * @return Whether it has the property or not
     */
    public boolean hasProperty(ParticleProperty property) {
        return properties.contains(property);
    }

    /**
     * Determine if this particle effect is supported by your current server
     * version
     * 
     * @return Whether the particle effect is supported or not
     */
    public boolean isSupported() {
        return this == ParticleEffect.NONE || spigotEnum != null;
    }

    /**
     * Returns a ParticleEffect List of all supported effects for the server version
     * 
     * @return Supported effects
     */
    public static List<ParticleEffect> getSupportedEffects() {
        List<ParticleEffect> effects = new ArrayList<ParticleEffect>();
        for (ParticleEffect pe : values())
            if (pe.isSupported()) effects.add(pe);
        return effects;
    }

    /**
     * Returns the particle effect with the given name
     * 
     * @param name Name of the particle effect
     * @return The particle effect
     */
    public static ParticleEffect fromName(String name) {
        for (Entry<String, ParticleEffect> entry : NAME_MAP.entrySet()) {
            if (!entry.getKey().equalsIgnoreCase(name)) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }

    /**
     * Returns the particle effect with the given id
     * 
     * @param id Id of the particle effect
     * @return The particle effect
     */
    public static ParticleEffect fromId(int id) {
        for (Entry<Integer, ParticleEffect> entry : ID_MAP.entrySet()) {
            if (entry.getKey() != id) {
                continue;
            }
            return entry.getValue();
        }
        return null;
    }

    /**
     * Determine if the data type for a particle effect is correct
     * 
     * @param effect Particle effect
     * @param data Particle data
     * @return Whether the data type is correct or not
     */
    private static boolean isDataCorrect(ParticleEffect effect, ParticleData data) {
        return ((effect == BLOCK_CRACK || effect == BLOCK_DUST || effect == FALLING_DUST) && data instanceof BlockData) || (effect == ITEM_CRACK && data instanceof ItemData);
    }

    /**
     * Determine if the color type for a particle effect is correct
     * 
     * @param effect Particle effect
     * @param color Particle color
     * @return Whether the color type is correct or not
     */
    private static boolean isColorCorrect(ParticleEffect effect, ParticleColor color) {
        return ((effect == MOB_SPELL || effect == MOB_SPELL_AMBIENT || effect == RED_DUST) && color instanceof OrdinaryColor) || (effect == NOTE && color instanceof NoteColor);
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
     * @throws ParticleDataException If the particle effect requires additional data
     */
    public void display(float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center) throws ParticleDataException {
        if (hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect requires additional data");
        }
        center.getWorld().spawnParticle(spigotEnum, center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed);
    }

    /**
     * Displays a single particle which is colored
     * 
     * @param color Color of the particle
     * @param center Center location of the effect
     * @throws ParticleColorException If the particle effect is not colorable or the color type is incorrect
     */
    public void display(ParticleColor color, Location center) throws ParticleColorException {
        if (!hasProperty(ParticleProperty.COLORABLE)) {
            throw new ParticleColorException("This particle effect is not colorable");
        }
        if (!isColorCorrect(this, color)) {
            throw new ParticleColorException("The particle color type is incorrect");
        }
        // Minecraft clients require that you pass Float.MIN_VALUE if the Red value is 0
        center.getWorld().spawnParticle(spigotEnum, center.getX(), center.getY(), center.getZ(), 0, this == ParticleEffect.RED_DUST && color.getValueX() == 0 ? Float.MIN_VALUE : color.getValueX(), color.getValueY(), color.getValueZ(), 1);
    }

    /**
     * Displays a particle effect which requires additional data and is only
     * visible for all players within a certain range in the world of @param
     * center
     * 
     * @param data Data of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @throws ParticleDataException If the particle effect does not require additional data or if the data type is incorrect
     */
    @SuppressWarnings("deprecation")
    public void display(ParticleData data, float offsetX, float offsetY, float offsetZ, float speed, int amount, Location center) throws ParticleDataException {
        if (!hasProperty(ParticleProperty.REQUIRES_DATA)) {
            throw new ParticleDataException("This particle effect does not require additional data");
        }
        if (!isDataCorrect(this, data)) {
            throw new ParticleDataException("The particle data type is incorrect");
        }
        Object extraData;
        if (spigotEnum.getDataType() == MaterialData.class) {
            extraData = new MaterialData(data.getMaterial(), data.getData()); // Deprecated, but there is currently no replacement for it
        } else if (spigotEnum.getDataType() == ItemStack.class) {
            extraData = new ItemStack(data.getMaterial(), data.getData());
        } else {
            extraData = null;
            System.out.println(spigotEnum.getDataType().getName());
        }
        center.getWorld().spawnParticle(spigotEnum, center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed, extraData);
    }

    /**
     * Represents the property of a particle effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public static enum ParticleProperty {
        /**
         * The particle effect requires block or item data to be displayed
         */
        REQUIRES_DATA,
        /**
         * The particle effect uses the offsets as color values
         */
        COLORABLE;
    }

    /**
     * Represents the particle data for effects like
     * {@link ParticleEffect#ITEM_CRACK}, {@link ParticleEffect#BLOCK_CRACK},
     * {@link ParticleEffect#BLOCK_DUST}, and {@link ParticleEffect#FALLING_DUST}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.6
     */
    public static abstract class ParticleData {
        private final Material material;
        private final byte data;

        /**
         * Construct a new particle data
         * 
         * @param material Material of the item/block
         * @param data Data value of the item/block
         */
        public ParticleData(Material material, byte data) {
            this.material = material;
            this.data = data;
        }

        /**
         * Returns the material of this data
         * 
         * @return The material
         */
        public Material getMaterial() {
            return material;
        }

        /**
         * Returns the data value of this data
         * 
         * @return The data value
         */
        public byte getData() {
            return data;
        }
    }

    /**
     * Represents the item data for the {@link ParticleEffect#ITEM_CRACK} effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.6
     */
    public static final class ItemData extends ParticleData {
        /**
         * Construct a new item data
         * 
         * @param material Material of the item
         * @param data Data value of the item
         */
        public ItemData(Material material, byte data) {
            super(material, data);
        }
    }

    /**
     * Represents the block data for the {@link ParticleEffect#BLOCK_CRACK} and
     * {@link ParticleEffect#BLOCK_DUST} effects
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.6
     */
    public static final class BlockData extends ParticleData {
        /**
         * Construct a new block data
         * 
         * @param material Material of the block
         * @param data Data value of the block
         * @throws IllegalArgumentException If the material is not a block
         */
        public BlockData(Material material, byte data) throws IllegalArgumentException {
            super(material, data);
            if (!material.isBlock()) {
                throw new IllegalArgumentException("The material is not a block");
            }
        }
    }

    /**
     * Represents the color for effects like {@link ParticleEffect#MOB_SPELL},
     * {@link ParticleEffect#MOB_SPELL_AMBIENT}, {@link ParticleEffect#RED_DUST}
     * and {@link ParticleEffect#NOTE}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public static abstract class ParticleColor {
        /**
         * Returns the value for the offsetX field
         * 
         * @return The offsetX value
         */
        public abstract float getValueX();

        /**
         * Returns the value for the offsetY field
         * 
         * @return The offsetY value
         */
        public abstract float getValueY();

        /**
         * Returns the value for the offsetZ field
         * 
         * @return The offsetZ value
         */
        public abstract float getValueZ();
    }

    /**
     * Represents the color for effects like {@link ParticleEffect#MOB_SPELL},
     * {@link ParticleEffect#MOB_SPELL_AMBIENT} and {@link ParticleEffect#NOTE}
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class OrdinaryColor extends ParticleColor {
        private final int red;
        private final int green;
        private final int blue;

        /**
         * Construct a new ordinary color
         * 
         * @param red Red value of the RGB format
         * @param green Green value of the RGB format
         * @param blue Blue value of the RGB format
         * @throws IllegalArgumentException If one of the values is lower than 0
         *             or higher than 255
         */
        public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
            if (red == 999 && green == 999 && blue == 999) {
                this.red = red;
                this.green = green;
                this.blue = blue;
            } else {
                if (red < 0) {
                    throw new IllegalArgumentException("The red value is lower than 0");
                }
                if (red > 255) {
                    throw new IllegalArgumentException("The red value is higher than 255");
                }
                this.red = red;
                if (green < 0) {
                    throw new IllegalArgumentException("The green value is lower than 0");
                }
                if (green > 255) {
                    throw new IllegalArgumentException("The green value is higher than 255");
                }
                this.green = green;
                if (blue < 0) {
                    throw new IllegalArgumentException("The blue value is lower than 0");
                }
                if (blue > 255) {
                    throw new IllegalArgumentException("The blue value is higher than 255");
                }
                this.blue = blue;
            }
        }

        /**
         * Returns the red value of the RGB format
         * 
         * @return The red value
         */
        public int getRed() {
            return red;
        }

        /**
         * Returns the green value of the RGB format
         * 
         * @return The green value
         */
        public int getGreen() {
            return green;
        }

        /**
         * Returns the blue value of the RGB format
         * 
         * @return The blue value
         */
        public int getBlue() {
            return blue;
        }

        /**
         * Returns the red value divided by 255
         * 
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            if (red == 999) return 0F;
            return (float) red / 255F;
        }

        /**
         * Returns the green value divided by 255
         * 
         * @return The offsetY value
         */
        @Override
        public float getValueY() {
            if (green == 999) return 0F;
            return (float) green / 255F;
        }

        /**
         * Returns the blue value divided by 255
         * 
         * @return The offsetZ value
         */
        @Override
        public float getValueZ() {
            if (blue == 999) return 0F;
            return (float) blue / 255F;
        }
    }

    /**
     * Represents the color for the {@link ParticleEffect#NOTE} effect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class NoteColor extends ParticleColor {
        private final int note;

        /**
         * Construct a new note color
         * 
         * @param note Note id which determines color
         * @throws IllegalArgumentException If the note value is lower than 0 or
         *             higher than 24
         */
        public NoteColor(int note) throws IllegalArgumentException {
            if (note == 99) {
                this.note = note;
            } else {
                if (note < 0) {
                    throw new IllegalArgumentException("The note value is lower than 0");
                }
                if (note > 24) {
                    throw new IllegalArgumentException("The note value is higher than 24");
                }
                this.note = note;
            }
        }

        /**
         * Returns the note value divided by 24
         * 
         * @return The offsetX value
         */
        @Override
        public float getValueX() {
            return (float) note / 24F;
        }

        /**
         * Returns zero because the offsetY value is unused
         * 
         * @return zero
         */
        @Override
        public float getValueY() {
            return 0;
        }

        /**
         * Returns zero because the offsetZ value is unused
         * 
         * @return zero
         */
        @Override
        public float getValueZ() {
            return 0;
        }

    }

    /**
     * Represents a runtime exception that is thrown either if the displayed
     * particle effect requires data and has none or vice-versa or if the data
     * type is incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.6
     */
    private static final class ParticleDataException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle data exception
         * 
         * @param message Message that will be logged
         */
        public ParticleDataException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown either if the displayed
     * particle effect is not colorable or if the particle color type is
     * incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     * 
     * @author DarkBlade12
     * @since 1.7
     */
    private static final class ParticleColorException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle color exception
         * 
         * @param message Message that will be logged
         */
        public ParticleColorException(String message) {
            super(message);
        }
    }

}
