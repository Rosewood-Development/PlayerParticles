package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.ParticleUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.manager.AbstractConfigurationManager;
import org.bukkit.Material;

public class ConfigurationManager extends AbstractConfigurationManager {

    public ConfigurationManager(RosePlugin playerParticles) {
        super(playerParticles, Setting.class);
    }
    
    public enum Setting implements RoseSetting {
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
        TOGGLE_ON_COMBAT_INCLUDE_MOBS("toggle-on-combat-include-mobs", false, "If true, mobs will be included in the combat check in addition to players"),
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
        PRESET_GROUPS_ALLOW_OVERLAPPING("preset-groups-allow-overlapping", false, "If true, applying a preset group will not overwrite the current active particles", "Note: This does not check permissions against the max particles setting, use with caution"),
        PRESET_GROUPS_OVERLAPPING_ONE_PER_STYLE("preset-groups-overlapping-one-per-style", false, "If true, applying a preset group with overlapping enabled will only allow one particle per style", "Existing particles that cause duplicate styles will be overwritten", "This setting has no effect if preset-groups-allow-overlapping is set to false"),

        GUI_GLASS_COLORS("gui-border-colors", null, "The colors of the glass in the GUI", "Valid colors: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/DyeColor.html"),
        GUI_GLASS_COLORS_DEFAULT("gui-border-colors.default", "WHITE"),
        GUI_GLASS_COLORS_EDIT_DATA("gui-border-colors.edit-data", "MAGENTA"),
        GUI_GLASS_COLORS_EDIT_EFFECT("gui-border-colors.edit-effect", "LIGHT_BLUE"),
        GUI_GLASS_COLORS_EDIT_PARTICLE("gui-border-colors.edit-particle", "RED"),
        GUI_GLASS_COLORS_EDIT_STYLE("gui-border-colors.edit-style", "BLUE"),
        GUI_GLASS_COLORS_MANAGE_GROUPS("gui-border-colors.manage-groups", "BROWN"),
        GUI_GLASS_COLORS_MANAGE_PARTICLES("gui-border-colors.manage-particles", "ORANGE"),

        WORLDGUARD_SETTINGS("worldguard-settings", null, "Settings for WorldGuard", "If WorldGuard is not installed, these settings will do nothing"),
        WORLDGUARD_USE_ALLOWED_REGIONS("worldguard-settings.use-allowed-regions", false, "If true, particles will only be able to spawn if they are in an allowed region and not a disallowed region", "If false, particles will be able to spawn as long as they are not in a disallowed region"),
        WORLDGUARD_ALLOWED_REGIONS("worldguard-settings.allowed-regions", Arrays.asList("example_region_1", "example_region_2"), "Regions that particles will be allowed to spawn in", "WARNING: This setting is deprecated in favor of region flags, and will be removed in a future update."),
        WORLDGUARD_DISALLOWED_REGIONS("worldguard-settings.disallowed-regions", Arrays.asList("example_region_3", "example_region_4"), "Regions that particles will be blocked from spawning in", "This overrides allowed regions if they overlap", "WARNING: This setting is deprecated in favor of region flags, and will be removed in a future update."),
        WORLDGUARD_CHECK_INTERVAL("worldguard-settings.check-interval", 10, "How often to check if a player is in a region that allows spawning particles", "Measured in ticks"),
        WORLDGUARD_ENABLE_BYPASS_PERMISSION("worldguard-settings.enable-bypass-permission", false, "If true, the permission playerparticles.worldguard.bypass will allow", "the player to bypass the region requirements"),

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

        @Override
        public String getKey() {
            return this.key;
        }

        @Override
        public Object getDefaultValue() {
            return this.defaultValue;
        }

        @Override
        public String[] getComments() {
            return this.comments;
        }

        @Override
        public Object getCachedValue() {
            return this.value;
        }

        @Override
        public void setCachedValue(Object value) {
            this.value = value;
        }

        @Override
        public CommentedFileConfiguration getBaseConfig() {
            return PlayerParticles.getInstance().getManager(ConfigurationManager.class).getConfig();
        }

    }

    @Override
    protected String[] getHeader() {
        return new String[] {
                "     _________ __                           __________                __   __        __",
                "     \\______  \\  | _____  ___ __  __________\\______   \\_____ ________/  |_|__| ____ |  |   ____   ______",
                "     |     ___/  | \\__  \\<   |  |/ __ \\_  __ \\     ___/\\__  \\\\_  __ \\   __\\  |/ ___\\|  | _/ __ \\ /  ___/",
                "     |    |   |  |__/ __ \\\\___  \\  ___/|  | \\/    |     / __ \\|  | \\/|  | |  \\  \\___|  |_\\  ___/ \\___ \\",
                "     |____|   |____(____  / ____|\\___  >__|  |____|    (____  /__|   |__| |__|\\___  >____/\\___  >____  >",
                "                        \\/\\/         \\/                     \\/                    \\/          \\/     \\/"
        };
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
