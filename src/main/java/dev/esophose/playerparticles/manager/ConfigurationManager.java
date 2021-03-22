package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Material;

public class ConfigurationManager extends Manager {

    private static final String[] HEADER = new String[] {
            "     _________ __                           __________                __   __        __",
            "     \\______  \\  | _____  ___ __  __________\\______   \\_____ ________/  |_|__| ____ |  |   ____   ______",
            "     |     ___/  | \\__  \\<   |  |/ __ \\_  __ \\     ___/\\__  \\\\_  __ \\   __\\  |/ ___\\|  | _/ __ \\ /  ___/",
            "     |    |   |  |__/ __ \\\\___  \\  ___/|  | \\/    |     / __ \\|  | \\/|  | |  \\  \\___|  |_\\  ___/ \\___ \\",
            "     |____|   |____(____  / ____|\\___  >__|  |____|    (____  /__|   |__| |__|\\___  >____/\\___  >____  >",
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
        GUI_REQUIRE_PERMISSION("gui-require-permission", false, "If the GUI should require the permission playerparticles.gui to open"),
        GUI_REQUIRE_EFFECTS_AND_STYLES("gui-require-effects-and-styles", true, "If the GUI should require the player to have permission for at least", "one effect and one style to be able to open the GUI"),
        GUI_PRESETS_ONLY("gui-presets-only", false, "If true, only the preset groups will be available in the GUI", "Permissions to open the GUI will change to only open if the user has any preset groups available"),
        GUI_PRESETS_HIDE_PARTICLES_DESCRIPTIONS("gui-presets-hide-particles-descriptions", false, "If true, the particle descriptions in the item lore for preset groups will be hidden"),
        GUI_CLOSE_AFTER_GROUP_SELECTED("gui-close-after-group-selected", true, "If true, the GUI will close after selecting a group (either saved or preset)"),
        GUI_BUTTON_SOUND("gui-button-sound", true, "If clicking a GUI button should make a noise"),
        TOGGLE_ON_MOVE("toggle-on-move", "NONE", "Valid values: DISPLAY_FEET, DISPLAY_NORMAL, DISPLAY_OVERHEAD, HIDE, NONE", "DISPLAY_FEET will display particles using the feet style while moving", "DISPLAY_NORMAL will display particles using the normal style while moving", "DISPLAY_OVERHEAD will display particles using the overhead style while moving", "HIDE will hide particles while moving", "NONE will make this setting do nothing", "Note: You can change what styles follow this setting in their individual setting files"),
        TOGGLE_ON_MOVE_DELAY("toggle-on-move-delay", 9, "The time (in ticks) a player has to be standing still before they are considered to be stopped", "This setting has no effect if toggle-on-move is set to false", "The value must be a positive whole number"),
        TOGGLE_ON_COMBAT("toggle-on-combat", false, "If true, particles will be completely disabled while the player is in combat", "Note: You can change what styles follow this setting in their individual setting files"),
        TOGGLE_ON_COMBAT_DELAY("toggle-on-combat-delay", 15, "The time (in seconds) a player has to not be damaged/attacked to be considered out of combat", "This setting has no effect if toggle-on-combat is set to false", "The value must be a positive whole number"),
        DISABLED_WORLDS("disabled-worlds", Collections.singletonList("disabled_world_name"), "A list of worlds that the plugin is disabled in"),
        CHECK_PERMISSIONS_ON_LOGIN("check-permissions-on-login", false, "Should particles a player no longer has permission to use be removed on login?"),
        MAX_PARTICLES("max-particles", 3, "The maximum number of particles a player can apply at once", "The GUI will only display up to 21, don't set this any higher than that"),
        MAX_GROUPS("max-groups", 10, "The maximum number of groups a player can have saved", "The GUI will only display up to 21, don't set this any higher than that"),
        MAX_FIXED_EFFECTS("max-fixed-effects", 5, "Max fixed effects per player"),
        MAX_FIXED_EFFECT_CREATION_DISTANCE("max-fixed-effect-creation-distance", 32, "Max fixed effect creation distance", "Determines how far away a player may create a fixed effect from themselves", "This measurement is in blocks", "Set to 0 for infinite distance"),
        TICKS_PER_PARTICLE("ticks-per-particle", 1, "How many ticks to wait before spawning more particles", "Increasing this value may cause less lag (if there was any), but will decrease prettiness", "Only use whole numbers greater than or equal to 1", "Going over 3 will likely look terrible"),
        DISPLAY_WHEN_INVISIBLE("display-when-invisible", false, "Should particles be displayed when the player is invisible?", "This includes spectator mode or an invisibility potion"),
        PARTICLE_RENDER_RANGE_PLAYER("particle-render-range-player", 48, "From how many blocks away should a player be able to see the particles from another player?"),
        PARTICLE_RENDER_RANGE_FIXED_EFFECT("particle-render-range-fixed-effect", 192, "From how many blocks away should a player be able to see the particles from a fixed effect?"),
        RAINBOW_CYCLE_SPEED("rainbow-cycle-speed", 2, "How many out of 360 hue ticks to move per game tick", "Higher values make the rainbow cycle faster", "Note: Must be a positive whole number"),
        DUST_SIZE("dust-size", 1.0, "How large should dust particles appear?", "Note: Can include decimals", "Only works in 1.13+"),
        GUI_GROUP_CREATION_MESSAGE_DISPLAY_AREA("gui-group-creation-message-display-area", "ACTION_BAR", "Valid values: ACTION_BAR, TITLE, CHAT", "Where should the GUI group creation countdown message be displayed?", "Note: Server versions less than 1.11.2 will always use CHAT"),
        GUI_GROUP_CREATION_BUNGEE_SUPPORT("gui-group-creation-bungee-support", false, "If true, a message will be displayed in chat telling the player to enter the command", "This might be required for some servers using bungee chat plugins"),

        WORLDGUARD_SETTINGS("worldguard-settings", null, "Settings for WorldGuard", "If WorldGuard is not installed, these settings will do nothing"),
        WORLDGUARD_USE_ALLOWED_REGIONS("worldguard-settings.use-allowed-regions", false, "If true, particles will only be able to spawn if they are in an allowed region and not a disallowed region", "If false, particles will be able to spawn as long as they are not in a disallowed region"),
        WORLDGUARD_ALLOWED_REGIONS("worldguard-settings.allowed-regions", Arrays.asList("example_region_1", "example_region_2"), "Regions that particles will be allowed to spawn in", "WARNING: This setting is deprecated in favor of region flags, and will be removed in a future update."),
        WORLDGUARD_DISALLOWED_REGIONS("worldguard-settings.disallowed-regions", Arrays.asList("example_region_3", "example_region_4"), "Regions that particles will be blocked from spawning in", "This overrides allowed regions if they overlap", "WARNING: This setting is deprecated in favor of region flags, and will be removed in a future update."),
        WORLDGUARD_CHECK_INTERVAL("worldguard-settings.check-interval", 10, "How often to check if a player is in a region that allows spawning particles", "Measured in ticks"),
        WORLDGUARD_ENABLE_BYPASS_PERMISSION("worldguard-settings.enable-bypass-permission", false, "If true, the permission playerparticles.worldguard.bypass will allow", "the player to bypass the region requirements"),

        MYSQL_SETTINGS("mysql-settings", null, "Settings for if you want to use MySQL for data management"),
        MYSQL_ENABLED("mysql-settings.enabled", false, "Enable MySQL", "If false, SQLite will be used instead"),
        MYSQL_HOSTNAME("mysql-settings.hostname", "", "MySQL Database Hostname"),
        MYSQL_PORT("mysql-settings.port", 3306, "MySQL Database Port"),
        MYSQL_DATABASE_NAME("mysql-settings.database-name", "", "MySQL Database Name"),
        MYSQL_USER_NAME("mysql-settings.user-name", "", "MySQL Database User Name"),
        MYSQL_USER_PASSWORD("mysql-settings.user-password", "", "MySQL Database User Password"),
        MYSQL_TABLE_PREFIX("mysql-settings.table-prefix", PlayerParticles.getInstance().getDescription().getName().toLowerCase() + "_", "The prefix of the tables in the database", "Do not change this after tables have already been created or you will have data loss"),
        MYSQL_USE_SSL("mysql-settings.use-ssl", false, "If the database connection should use SSL", "You should enable this if your database supports SSL"),
        MYSQL_CONNECTION_POOL_SIZE("mysql-settings.connection-pool-size", 5, "The size of the connection pool to the database", "Not recommended to go below 2 or above 5"),

        GUI_ICON("gui-icon", null,
                "This configuration option allows you to change the GUI",
                "icons to whatever block/item you want. If you want to change an effect",
                "or style icon, use their respective config files.",
                "Notes: If any of the block/item names are invalid the icon in the GUI",
                "will be the barrier icon to show that it failed to load.",
                "You MUST use the Spigot-given name for it to work. You can see",
                "all the Spigot-given names at the link below:",
                "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html",
                "If multiple icons are listed, they are used for older MC versions"
        ),
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
        GUI_ICON_MISC_RESET("gui-icon.misc.reset", Collections.singletonList("BARRIER"));

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
            return String.valueOf(this.value);
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

        public boolean setIfNotExists(CommentedFileConfiguration fileConfiguration) {
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

                return true;
            }

            return false;
        }

        /**
         * Resets the cached value
         */
        public void reset() {
            this.value = null;
        }

        /**
         * @return true if a setting is still its default value, otherwise false
         */
        public boolean isDefault() {
            this.loadValue();

            // Don't care about list ordering
            if (this.defaultValue instanceof Collection && this.value instanceof Collection)
                return (((Collection<?>) this.defaultValue).containsAll((Collection<?>) this.value));

            return Objects.equals(this.defaultValue, this.value);
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
        boolean changed = setHeaderFooter;

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.playerParticles, configFile);

        if (setHeaderFooter)
            this.configuration.addComments(HEADER);

        for (Setting setting : Setting.values()) {
            setting.reset();
            changed |= setting.setIfNotExists(this.configuration);
        }

        for (GuiIcon icon : GuiIcon.values())
            icon.resetDefault();

        if (setHeaderFooter)
            this.configuration.addComments(FOOTER);

        if (changed)
            this.configuration.save();
        
        // Legacy nag: WorldGuard Regions
        if (!(Setting.WORLDGUARD_ALLOWED_REGIONS.isDefault() && Setting.WORLDGUARD_DISALLOWED_REGIONS.isDefault())) {
            Arrays.asList(
                    "It looks like you're using the 'allowed-regions' or 'disallowed-regions' settings.",
                    "These settings are deprecated and will be removed in a future update.",
                    "As an alternative, consider using the newer and more flexible 'player-particles' WorldGuard region flag."
            ).forEach(PlayerParticles.getInstance().getLogger()::warning);
        }
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
        RESET;

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
                material = ParticleUtils.FALLBACK_MATERIAL;

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
