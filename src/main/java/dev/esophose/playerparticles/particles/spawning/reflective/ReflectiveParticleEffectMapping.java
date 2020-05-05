package dev.esophose.playerparticles.particles.spawning.reflective;

import dev.esophose.playerparticles.util.NMSUtil;

public enum ReflectiveParticleEffectMapping {

    POOF("explode", 0),
    EXPLOSION("largeexplode", 1),
    EXPLOSION_EMITTER("hugeexplosion", 2),
    FIREWORK("fireworksSpark", 3),
    BUBBLE("bubble", 4),
    SPLASH("splash", 5),
    FISHING("wake", 6),
    //SUSPENDED("suspended", 7),
    MYCELIUM("depthSuspend", 8),
    CRIT("crit", 9),
    ENCHANTED_HIT("magicCrit", 10),
    SMOKE("smoke", 11),
    LARGE_SMOKE("largesmoke", 12),
    SPELL("spell", 13),
    INSTANT_EFFECT("instantSpell", 14),
    ENTITY_EFFECT("mobSpell", 15),
    AMBIENT_ENTITY_EFFECT("mobSpellAmbient", 16),
    WITCH("witchMagic", 17),
    DRIPPING_WATER("dripWater", 18),
    DRIPPING_LAVA("dripLava", 19),
    ANGRY_VILLAGER("angryVillager", 20),
    HAPPY_VILLAGER("happyVillager", 21),
    NOTE("note", 23),
    PORTAL("portal", 24),
    ENCHANT("enchantmenttable", 25),
    FLAME("flame", 26),
    LAVA("lava", 27),
    FOOTSTEP("footstep", 28),
    CLOUD("cloud", 29),
    DUST("reddust", 30),
    ITEM_SNOWBALL("snowballpoof", 31),
    //SNOW_SHOVEL("snowshovel", 32),
    ITEM_SLIME("slime", 33),
    HEART("heart", 34),
    BARRIER("barrier", 35, 8),
    ITEM("iconcrack", 36),
    BLOCK("blockcrack", 37),
    //BLOCK_DUST("blockdust", 38),
    RAIN("droplet", 39, 8),
    //ITEM_TAKE("take", 40, 8),
    ELDER_GUARDIAN("mobappearance", 41, 8);

    private final String name;
    private final int id;
    private final boolean supported;

    ReflectiveParticleEffectMapping(String name, int id, int requiredVersion) {
        this.name = name;
        this.id = id;
        this.supported = NMSUtil.getVersionNumber() >= requiredVersion;
    }

    ReflectiveParticleEffectMapping(String name, int id) {
        this(name, id, -1);
    }

    /**
     * @return the name of the internal minecraft particle effect
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return the id of the internal minecraft particle effect
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return true if this particle effect is supported, otherwise false
     */
    public boolean isSupported() {
        return this.supported;
    }

}