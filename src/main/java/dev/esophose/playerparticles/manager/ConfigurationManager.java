package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Material;

public class ConfigurationManager extends Manager {

    private static final String[] HEADER = new String[] {
            "     _________ __                           __________                __   __        __                    _________",
            "     \\______  \\  | _____  ___ __  __________\\______   \\_____ ________/  |_|__| ____ |  |   ____   ______   \\______  \\",
            "     |     ___/  | \\__  \\<   |  |/ __ \\_  __ \\     ___/\\__  \\\\_  __ \\   __\\  |/ ___\\|  | _/ __ \\ /  ___/       /    /",
            "     |    |   |  |__/ __ \\\\___  \\  ___/|  | \\/    |     / __ \\|  | \\/|  | |  \\  \\___|  |_\\  ___/ \\___ \\       /    /",
            "     |____|   |____(____  / ____|\\___  >__|  |____|    (____  /__|   |__| |__|\\___  >____/\\___  >____  >     /____/",
            "                        \\/\\/         \\/                     \\/                    \\/          \\/     \\/"
    };

    private static final String[] FOOTER = new String[] {
            "That's everything! You reached the end of the configuration.",
            "Enjoy the plugin!"
    };

    public enum Setting {
        CHECK_UPDATES("check-updates", true, "Check for new versions of the plugin"),
        SEND_METRICS("send-metrics", true, "If metrics should be sent to bStats", "I would appreciate it if you left this enabled, it helps me get statistics on how the plugin is used"),
        LOCALE("locale", "en_US", "The locale to use in the /locale folder"),
        MESSAGES_ENABLED("message-enabled", true, "If you're using other plugins to execute commands you may wish to turn off messages"),
        USE_MESSAGE_PREFIX("use-message-prefix", true, "Whether or not to use the message-prefix field when displaying messages"),
        GUI_ENABLED("gui-enabled", true, "If the command /pp gui is enabled", "Disable this if you have your own custom GUI through another plugin"),
        GUI_PRESETS_ONLY("gui-presets-only", false, "If true, only the preset groups will be available in the GUI", "Permissions to open the GUI will change to only open if the user has any preset groups available"),
        GUI_CLOSE_AFTER_GROUP_SELECTED("gui-close-after-group-selected", true, "If true, the GUI will close after selecting a group (either saved or preset)"),
        GUI_BUTTON_SOUND("gui-button-sound", true, "If clicking a GUI button should make a noise"),
        TOGGLE_ON_MOVE("toggle-on-move", false, "If true, styles will not display while the player is moving", "They will instead have the effect displayed at their feet", "Note: Not all styles abide by this rule, but most will"),
        TOGGLE_ON_MOVE_DELAY("toggle-on-move-delay", 9, "The time (in ticks) a player has to be standing still before they are considered to be stopped", "This setting has no effect if toggle-on-move is set to false", "The value must be a positive whole number"),
        DISABLED_WORLDS("disabled-worlds", Collections.singletonList("disabled_world_name"), "A list of worlds that the plugin is disabled in"),
        MAX_PARTICLES("max-particles", 3, "The maximum number of particles a player can apply at once", "The GUI will only display up to 21, don't set this any higher than that"),
        MAX_GROUPS("max-groups", 10, "The maximum number of groups a player can have saved", "The GUI will only display up to 21, don't set this any higher than that"),
        MAX_FIXED_EFFECTS("max-fixed-effects", 5, "Max fixed effects per player"),
        MAX_FIXED_EFFECT_CREATION_DISTANCE("max-fixed-effect-creation-distance", 32, "Max fixed effect creation distance", "Determines how far away a player may create a fixed effect from themselves", "This measurement is in blocks", "Set to 0 for infinite distance"),
        TICKS_PER_PARTICLE("ticks-per-particle", 1, "How many ticks to wait before spawning more particles", "Increasing this value may cause less lag (if there was any), but will decrease prettiness", "Only use whole numbers greater than or equal to 1", "Going over 3 will likely look terrible"),
        PARTICLE_RENDER_RANGE_PLAYER("particle-render-range-player", 48, "From how many blocks away should a player be able to see the particles from another player?"),
        PARTICLE_RENDER_RANGE_FIXED_EFFECT("particle-render-range-fixed-effect", 32, "From how many blocks away should a player be able to see the particles from a fixed effect?"),
        RAINBOW_CYCLE_SPEED("rainbow-cycle-speed", 2, "How many out of 360 hue ticks to move per game tick", "Higher values make the rainbow cycle faster", "Note: Must be a positive whole number"),
        DUST_SIZE("dust-size", 1, "How large should dust particles appear?", "Note: Can include decimals", "Only works in 1.13+"),

        MYSQL_SETTINGS("mysql-settings", null, "Settings for if you want to use MySQL for data management"),
        MYSQL_ENABLED("mysql-settings.enabled", false, "Enable MySQL", "If false, SQLite will be used instead"),
        MYSQL_HOSTNAME("mysql-settings.hostname", "", "MySQL Database Hostname"),
        MYSQL_PORT("mysql-settings.port", 3306, "MySQL Database Port"),
        MYSQL_DATABASE_NAME("mysql-settings.database-name", "", "MySQL Database Name"),
        MYSQL_USER_NAME("mysql-settings.user-name", "", "MySQL Database User Name"),
        MYSQL_USER_PASSWORD("mysql-settings.user-password", "", "MySQL Database User Password"),
        MYSQL_USE_SSL("mysql-settings.use-ssl", false, "If the database connection should use SSL", "You should enable this if your database supports SSL"),

        GUI_ICON("gui-icon", null, "=================================================================== #",
                "                       GUI ICON SETTINGS                            #",
                "This configuration option allows you to change any of the GUI       #",
                "icons to whatever block/item you want.                              #",
                "                                                                    #",
                "Important Notes:                                                    #",
                "* If any of the block/item names are invalid the icon in the GUI    #",
                "  will be the barrier icon to show that it failed to load.          #",
                "* Do NOT change the particle/style name                             #",
                "* You MUST use the Spigot-given name for it to work. You can see    #",
                "  all the Spigot-given names at the link below:                     #",
                "  https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html #",
                "* If two icons are listed, the second one is used for below MC 1.13 #",
                "=================================================================== #"),
        GUI_ICON_MISC("gui-icon.misc", null),
        GUI_ICON_MISC_PARTICLES("gui-icon.misc.particles", Collections.singletonList("BLAZE_POWDER")),
        GUI_ICON_MISC_GROUPS("gui-icon.misc.groups", Collections.singletonList("CHEST")),
        GUI_ICON_MISC_PRESET_GROUPS("gui-icon.misc.preset_groups", Collections.singletonList("ENDER_CHEST")),
        GUI_ICON_MISC_BACK("gui-icon.misc.back", Collections.singletonList("ARROW")),
        GUI_ICON_MISC_NEXT_PAGE("gui-icon.misc.next_page", Collections.singletonList("PAPER")),
        GUI_ICON_MISC_PREVIOUS_PAGE("gui-icon.misc.previous_page", Collections.singletonList("PAPER")),
        GUI_ICON_MISC_CREATE("gui-icon.misc.create", Arrays.asList("WRITABLE_BOOK", "BOOK_AND_QUILL")),
        GUI_ICON_MISC_EDIT_EFFECT("gui-icon.misc.edit_effect", Arrays.asList("FIREWORK_ROCKET", "FIREWORK")),
        GUI_ICON_MISC_EDIT_STYLE("gui-icon.misc.edit_style", Collections.singletonList("NETHER_STAR")),
        GUI_ICON_MISC_EDIT_DATA("gui-icon.misc.edit_data", Collections.singletonList("BOOK")),
        GUI_ICON_MISC_RESET("gui-icon.misc.reset", Collections.singletonList("BARRIER")),
        GUI_ICON_EFFECT("gui-icon.effect", null),
        GUI_ICON_EFFECT_AMBIENT_ENTITY_EFFECT("gui-icon.effect.ambient_entity_effect", Collections.singletonList("BEACON")),
        GUI_ICON_EFFECT_ANGRY_VILLAGER("gui-icon.effect.angry_villager", Collections.singletonList("IRON_DOOR")),
        GUI_ICON_EFFECT_BARRIER("gui-icon.effect.barrier", Collections.singletonList("BARRIER")),
        GUI_ICON_EFFECT_BLOCK("gui-icon.effect.block", Collections.singletonList("STONE")),
        GUI_ICON_EFFECT_BUBBLE("gui-icon.effect.bubble", Arrays.asList("BUBBLE_CORAL", "GLASS")),
        GUI_ICON_EFFECT_BUBBLE_COLUMN_UP("gui-icon.effect.bubble_column_up", Collections.singletonList("MAGMA_BLOCK")),
        GUI_ICON_EFFECT_BUBBLE_POP("gui-icon.effect.bubble_pop", Collections.singletonList("BUBBLE_CORAL_FAN")),
        GUI_ICON_EFFECT_CAMPFIRE_COSY_SMOKE("gui-icon.effect.campfire_cosy_smoke", Collections.singletonList("CAMPFIRE")),
        GUI_ICON_EFFECT_CAMPFIRE_SIGNAL_SMOKE("gui-icon.effect.campfire_signal_smoke", Collections.singletonList("REDSTONE_TORCH")),
        GUI_ICON_EFFECT_CLOUD("gui-icon.effect.cloud", Arrays.asList("WHITE_WOOL", "WOOL")),
        GUI_ICON_EFFECT_COMPOSTER("gui-icon.effect.composter", Collections.singletonList("COMPOSTER")),
        GUI_ICON_EFFECT_CRIT("gui-icon.effect.crit", Collections.singletonList("IRON_SWORD")),
        GUI_ICON_EFFECT_CURRENT_DOWN("gui-icon.effect.current_down", Collections.singletonList("SOUL_SAND")),
        GUI_ICON_EFFECT_DAMAGE_INDICATOR("gui-icon.effect.damage_indicator", Collections.singletonList("BOW")),
        GUI_ICON_EFFECT_DOLPHIN("gui-icon.effect.dolphin", Collections.singletonList("DOLPHIN_SPAWN_EGG")),
        GUI_ICON_EFFECT_DRAGON_BREATH("gui-icon.effect.dragon_breath", Arrays.asList("DRAGON_BREATH", "DRAGONS_BREATH")),
        GUI_ICON_EFFECT_DRIPPING_HONEY("gui-icon.effect.dripping_honey", Collections.singletonList("BEE_NEST")),
        GUI_ICON_EFFECT_DRIPPING_LAVA("gui-icon.effect.dripping_lava", Collections.singletonList("LAVA_BUCKET")),
        GUI_ICON_EFFECT_DRIPPING_WATER("gui-icon.effect.dripping_water", Collections.singletonList("WATER_BUCKET")),
        GUI_ICON_EFFECT_DUST("gui-icon.effect.dust", Collections.singletonList("REDSTONE")),
        GUI_ICON_EFFECT_ENCHANT("gui-icon.effect.enchant", Arrays.asList("ENCHANTING_TABLE", "ENCHANTMENT_TABLE")),
        GUI_ICON_EFFECT_ENCHANTED_HIT("gui-icon.effect.enchanted_hit", Collections.singletonList("DIAMOND_SWORD")),
        GUI_ICON_EFFECT_END_ROD("gui-icon.effect.end_rod", Collections.singletonList("END_ROD")),
        GUI_ICON_EFFECT_ENTITY_EFFECT("gui-icon.effect.entity_effect", Collections.singletonList("GLOWSTONE_DUST")),
        GUI_ICON_EFFECT_EXPLOSION("gui-icon.effect.explosion", Arrays.asList("FIRE_CHARGE", "FIREBALL")),
        GUI_ICON_EFFECT_EXPLOSION_EMITTER("gui-icon.effect.explosion_emitter", Collections.singletonList("TNT")),
        GUI_ICON_EFFECT_FALLING_DUST("gui-icon.effect.falling_dust", Collections.singletonList("SAND")),
        GUI_ICON_EFFECT_FALLING_HONEY("gui-icon.effect.falling_honey", Collections.singletonList("HONEY_BOTTLE")),
        GUI_ICON_EFFECT_FALLING_LAVA("gui-icon.effect.falling_lava", Collections.singletonList("RED_DYE")),
        GUI_ICON_EFFECT_FALLING_NECTAR("gui-icon.effect.falling_nectar", Collections.singletonList("HONEYCOMB")),
        GUI_ICON_EFFECT_FALLING_WATER("gui-icon.effect.falling_water", Collections.singletonList("BLUE_DYE")),
        GUI_ICON_EFFECT_FIREWORK("gui-icon.effect.firework", Arrays.asList("FIREWORK_ROCKET", "FIREWORK")),
        GUI_ICON_EFFECT_FISHING("gui-icon.effect.fishing", Collections.singletonList("FISHING_ROD")),
        GUI_ICON_EFFECT_FLAME("gui-icon.effect.flame", Collections.singletonList("BLAZE_POWDER")),
        GUI_ICON_EFFECT_FOOTSTEP("gui-icon.effect.footstep", Collections.singletonList("GRASS")),
        GUI_ICON_EFFECT_HAPPY_VILLAGER("gui-icon.effect.happy_villager", Arrays.asList("DARK_OAK_DOOR", "WOOD_DOOR")),
        GUI_ICON_EFFECT_HEART("gui-icon.effect.heart", Arrays.asList("POPPY", "RED_ROSE")),
        GUI_ICON_EFFECT_INSTANT_EFFECT("gui-icon.effect.instant_effect", Arrays.asList("SPLASH_POTION", "POTION")),
        GUI_ICON_EFFECT_ITEM("gui-icon.effect.item", Collections.singletonList("ITEM_FRAME")),
        GUI_ICON_EFFECT_ITEM_SLIME("gui-icon.effect.item_slime", Collections.singletonList("SLIME_BALL")),
        GUI_ICON_EFFECT_ITEM_SNOWBALL("gui-icon.effect.item_snowball", Collections.singletonList("SNOWBALL")),
        GUI_ICON_EFFECT_LARGE_SMOKE("gui-icon.effect.large_smoke", Arrays.asList("COBWEB", "WEB")),
        GUI_ICON_EFFECT_LANDING_HONEY("gui-icon.effect.landing_honey", Collections.singletonList("HONEY_BLOCK")),
        GUI_ICON_EFFECT_LANDING_LAVA("gui-icon.effect.landing_lava", Collections.singletonList("ORANGE_DYE")),
        GUI_ICON_EFFECT_LAVA("gui-icon.effect.lava", Collections.singletonList("MAGMA_CREAM")),
        GUI_ICON_EFFECT_MYCELIUM("gui-icon.effect.mycelium", Arrays.asList("MYCELIUM", "MYCEL")),
        GUI_ICON_EFFECT_NAUTILUS("gui-icon.effect.nautilus", Collections.singletonList("HEART_OF_THE_SEA")),
        GUI_ICON_EFFECT_NOTE("gui-icon.effect.note", Collections.singletonList("NOTE_BLOCK")),
        GUI_ICON_EFFECT_POOF("gui-icon.effect.poof", Arrays.asList("FIREWORK_STAR", "FIREWORK_CHARGE")),
        GUI_ICON_EFFECT_PORTAL("gui-icon.effect.portal", Collections.singletonList("OBSIDIAN")),
        GUI_ICON_EFFECT_RAIN("gui-icon.effect.rain", Arrays.asList("PUFFERFISH_BUCKET", "LAPIS_BLOCK")),
        GUI_ICON_EFFECT_SMOKE("gui-icon.effect.smoke", Collections.singletonList("TORCH")),
        GUI_ICON_EFFECT_SNEEZE("gui-icon.effect.sneeze", Collections.singletonList("BAMBOO")),
        GUI_ICON_EFFECT_SPELL("gui-icon.effect.spell", Arrays.asList("POTION", "GLASS_BOTTLE")),
        GUI_ICON_EFFECT_SPIT("gui-icon.effect.spit", Arrays.asList("LLAMA_SPAWN_EGG", "PUMPKIN_SEEDS")),
        GUI_ICON_EFFECT_SPLASH("gui-icon.effect.splash", Arrays.asList("SALMON", "FISH")),
        GUI_ICON_EFFECT_SQUID_INK("gui-icon.effect.squid_ink", Collections.singletonList("INK_SAC")),
        GUI_ICON_EFFECT_SWEEP_ATTACK("gui-icon.effect.sweep_attack", Arrays.asList("GOLDEN_SWORD", "GOLD_SWORD")),
        GUI_ICON_EFFECT_TOTEM_OF_UNDYING("gui-icon.effect.totem_of_undying", Collections.singletonList("TOTEM")),
        GUI_ICON_EFFECT_UNDERWATER("gui-icon.effect.underwater", Collections.singletonList("TURTLE_HELMET")),
        GUI_ICON_EFFECT_WITCH("gui-icon.effect.witch", Collections.singletonList("CAULDRON")),
        GUI_ICON_STYLE("gui-icon.style", null),
        GUI_ICON_STYLE_ARROWS("gui-icon.style.arrows", Collections.singletonList("BOW")),
        GUI_ICON_STYLE_BATMAN("gui-icon.style.batman", Arrays.asList("BAT_SPAWN_EGG", "COAL")),
        GUI_ICON_STYLE_BEAM("gui-icon.style.beam", Collections.singletonList("POWERED_RAIL")),
        GUI_ICON_STYLE_BLOCKBREAK("gui-icon.style.blockbreak", Collections.singletonList("IRON_PICKAXE")),
        GUI_ICON_STYLE_BLOCKPLACE("gui-icon.style.blockplace", Arrays.asList("OAK_PLANKS", "WOOD")),
        GUI_ICON_STYLE_CELEBRATION("gui-icon.style.celebration", Arrays.asList("FIREWORK_ROCKET", "FIREWORK")),
        GUI_ICON_STYLE_CHAINS("gui-icon.style.chains", Collections.singletonList("TRIPWIRE_HOOK")),
        GUI_ICON_STYLE_COMPANION("gui-icon.style.companion", Collections.singletonList("NAME_TAG")),
        GUI_ICON_STYLE_CUBE("gui-icon.style.cube", Collections.singletonList("STONE")),
        GUI_ICON_STYLE_FEET("gui-icon.style.feet", Collections.singletonList("GRASS")),
        GUI_ICON_STYLE_HALO("gui-icon.style.halo", Arrays.asList("END_PORTAL_FRAME", "ENDER_PORTAL_FRAME")),
        GUI_ICON_STYLE_HURT("gui-icon.style.hurt", Collections.singletonList("CACTUS")),
        GUI_ICON_STYLE_INVOCATION("gui-icon.style.invocation", Arrays.asList("ENDER_EYE", "EYE_OF_ENDER")),
        GUI_ICON_STYLE_MOVE("gui-icon.style.move", Arrays.asList("PISTON", "PISTON_BASE")),
        GUI_ICON_STYLE_NORMAL("gui-icon.style.normal", Collections.singletonList("DIRT")),
        GUI_ICON_STYLE_ORBIT("gui-icon.style.orbit", Arrays.asList("ENCHANTING_TABLE", "ENCHANTMENT_TABLE")),
        GUI_ICON_STYLE_OVERHEAD("gui-icon.style.overhead", Collections.singletonList("GLOWSTONE")),
        GUI_ICON_STYLE_POINT("gui-icon.style.point", Collections.singletonList("STONE_BUTTON")),
        GUI_ICON_STYLE_POPPER("gui-icon.style.popper", Arrays.asList("POPPED_CHORUS_FRUIT", "CHORUS_FRUIT_POPPED")),
        GUI_ICON_STYLE_PULSE("gui-icon.style.pulse", Arrays.asList("REDSTONE_TORCH", "REDSTONE_TORCH_ON")),
        GUI_ICON_STYLE_QUADHELIX("gui-icon.style.quadhelix", Arrays.asList("NAUTILUS_SHELL", "ACTIVATOR_RAIL")),
        GUI_ICON_STYLE_RINGS("gui-icon.style.rings", Collections.singletonList("LEAD")),
        GUI_ICON_STYLE_SPHERE("gui-icon.style.sphere", Arrays.asList("HEART_OF_THE_SEA", "SNOWBALL")),
        GUI_ICON_STYLE_SPIN("gui-icon.style.spin", Collections.singletonList("BEACON")),
        GUI_ICON_STYLE_SPIRAL("gui-icon.style.spiral", Collections.singletonList("HOPPER")),
        GUI_ICON_STYLE_SWORDS("gui-icon.style.swords", Collections.singletonList("IRON_SWORD")),
        GUI_ICON_STYLE_THICK("gui-icon.style.thick", Arrays.asList("COBWEB", "WEB")),
        GUI_ICON_STYLE_TWINS("gui-icon.style.twins", Arrays.asList("OAK_FENCE", "FENCE")),
        GUI_ICON_STYLE_VORTEX("gui-icon.style.vortex", Collections.singletonList("GLOWSTONE_DUST")),
        GUI_ICON_STYLE_WHIRL("gui-icon.style.whirl", Collections.singletonList("FEATHER")),
        GUI_ICON_STYLE_WHIRLWIND("gui-icon.style.whirlwind", Collections.singletonList("STRING")),
        GUI_ICON_STYLE_WINGS("gui-icon.style.wings", Collections.singletonList("ELYTRA"));

        private final String key;
        private final Object defaultValue;
        private final String[] comments;
        private Object value = null;

        Setting(String key, Object defaultValue, String... comments) {
            this.key = key;
            this.defaultValue = defaultValue;
            this.comments = comments != null ? comments : new String[0];
        }

        /**
         * Gets the setting as a boolean
         *
         * @return The setting as a boolean
         */
        public boolean getBoolean() {
            this.loadValue();
            return (boolean) this.value;
        }

        /**
         * @return the setting as an int
         */
        public int getInt() {
            this.loadValue();
            return (int) this.getNumber();
        }

        /**
         * @return the setting as a long
         */
        public long getLong() {
            this.loadValue();
            return (long) this.getNumber();
        }

        /**
         * @return the setting as a double
         */
        public double getDouble() {
            this.loadValue();
            return this.getNumber();
        }

        /**
         * @return the setting as a float
         */
        public float getFloat() {
            this.loadValue();
            return (float) this.getNumber();
        }

        /**
         * @return the setting as a String
         */
        public String getString() {
            this.loadValue();
            return (String) this.value;
        }

        private double getNumber() {
            if (this.value instanceof Integer) {
                return (int) this.value;
            } else if (this.value instanceof Short) {
                return (short) this.value;
            } else if (this.value instanceof Byte) {
                return (byte) this.value;
            } else if (this.value instanceof Float) {
                return (float) this.value;
            }

            return (double) this.value;
        }

        /**
         * @return the setting as a string list
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            this.loadValue();
            return (List<String>) this.value;
        }

        public void setIfNotExists(CommentedFileConfiguration fileConfiguration) {
            this.loadValue();

            if (fileConfiguration.get(this.key) == null) {
                List<String> comments = Stream.of(this.comments).collect(Collectors.toList());
                if (!(this.defaultValue instanceof List) && this.defaultValue != null) {
                    String defaultComment = "Default: ";
                    if (this.defaultValue instanceof String) {
                        if (ParticleUtils.containsConfigSpecialCharacters((String) this.defaultValue)) {
                            defaultComment += "'" + this.defaultValue + "'";
                        } else {
                            defaultComment += this.defaultValue;
                        }
                    } else {
                        defaultComment += this.defaultValue;
                    }
                    comments.add(defaultComment);
                }

                if (this.defaultValue != null) {
                    fileConfiguration.set(this.key, this.defaultValue, comments.toArray(new String[0]));
                } else {
                    fileConfiguration.addComments(comments.toArray(new String[0]));
                }
            }
        }

        /**
         * Resets the cached value
         */
        public void reset() {
            this.value = null;
        }

        /**
         * @return true if this setting is only a section and doesn't contain an actual value
         */
        public boolean isSection() {
            return this.defaultValue == null;
        }

        /**
         * Loads the value from the config and caches it if it isn't set yet
         */
        private void loadValue() {
            if (this.value != null)
                return;

            this.value = PlayerParticles.getInstance().getManager(ConfigurationManager.class).getConfig().get(this.key);
        }
    }

    private CommentedFileConfiguration configuration;

    public ConfigurationManager(PlayerParticles playerParticles) {
        super(playerParticles);
    }

    @Override
    public void reload() {
        File configFile = new File(this.playerParticles.getDataFolder(), "config.yml");
        boolean setHeaderFooter = !configFile.exists();

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.playerParticles, configFile);

        if (setHeaderFooter)
            this.configuration.addComments(HEADER);

        for (Setting setting : Setting.values()) {
            setting.reset();
            setting.setIfNotExists(this.configuration);
        }

        for (GuiIcon icon : GuiIcon.values())
            icon.resetDefault();

        if (setHeaderFooter)
            this.configuration.addComments(FOOTER);

        this.configuration.save();
    }

    @Override
    public void disable() {
        for (Setting setting : Setting.values())
            setting.reset();
    }

    /**
     * @return the config.yml as a CommentedFileConfiguration
     */
    public CommentedFileConfiguration getConfig() {
        return this.configuration;
    }

    /**
     * Used for grabbing/caching configurable GUI Icons from the config.yml
     */
    public enum GuiIcon {
        PARTICLES,
        GROUPS,
        PRESET_GROUPS,
        BACK,
        NEXT_PAGE,
        PREVIOUS_PAGE,
        CREATE,
        EDIT_EFFECT,
        EDIT_STYLE,
        EDIT_DATA,
        RESET,

        EFFECT,
        STYLE;

        private Map<String, Material> materials;

        GuiIcon() {
            this.materials = new HashMap<>();
        }

        /**
         * Gets the Material for this icon from the 'gui-icon.misc' section in the config.yml
         * Tries to get from cache first, otherwise loads it
         *
         * @return The Material for this Icon
         */
        public Material get() {
            return this.getInternal("gui-icon.misc." + this.name().toLowerCase());
        }

        /**
         * Gets the Material for a subsection of this icon in the config.yml
         * Tries to get from cache first, otherwise loads it
         *
         * @param subsection The name of the icon in the section
         * @return The Material for this Icon
         */
        public Material get(String subsection) {
            return this.getInternal("gui-icon." + this.name().toLowerCase() + "." + subsection);
        }

        /**
         * Gets the Material for this icon
         * Tries to get from cache first, otherwise loads it
         *
         * @param configPath Where to look in the config.yml for the Material name
         * @return The path in the config.yml to lookup
         */
        private Material getInternal(String configPath) {
            Material material = this.materials.get(configPath);
            if (material != null)
                return material;

            ConfigurationManager configurationManager = PlayerParticles.getInstance().getManager(ConfigurationManager.class);

            List<String> materials = configurationManager.getConfig().getStringList(configPath);

            for (String name : materials) {
                material = ParticleUtils.closestMatch(name);
                if (material != null)
                    break;
            }

            if (material == null)
                material = Material.BARRIER;

            this.materials.put(configPath, material);

            return material;
        }

        /**
         * Resets the setting's value so it will be fetched from the config the next time it's needed
         */
        private void resetDefault() {
            this.materials = new HashMap<>();
        }
    }

}
