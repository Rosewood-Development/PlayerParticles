package dev.esophose.playerparticles.config;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.rosewood.rosegarden.config.RoseSetting;
import dev.rosewood.rosegarden.config.SettingHolder;
import dev.rosewood.rosegarden.config.SettingSerializer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import static dev.rosewood.rosegarden.config.SettingSerializers.*;

public final class Settings implements SettingHolder {

    public static final SettingHolder INSTANCE = new Settings();
    private static final List<RoseSetting<?>> SETTINGS = new ArrayList<>();

    public static final RoseSetting<Boolean> MESSAGES_ENABLED = create("message-enabled", BOOLEAN, true, "If you're using other plugins to execute commands you may wish to turn off messages");
    public static final RoseSetting<Boolean> USE_MESSAGE_PREFIX = create("use-message-prefix", BOOLEAN, true, "Whether or not to use the message-prefix field when displaying messages");
    public static final RoseSetting<Boolean> GUI_ENABLED = create("gui-enabled", BOOLEAN, true, "If the command /pp gui is enabled", "Disable this if you have your own custom GUI through another plugin");
    public static final RoseSetting<Boolean> GUI_REQUIRE_PERMISSION = create("gui-require-permission", BOOLEAN, false, "If the GUI should require the permission playerparticles.gui to open");
    public static final RoseSetting<Boolean> GUI_REQUIRE_EFFECTS_AND_STYLES = create("gui-require-effects-and-styles", BOOLEAN, true, "If the GUI should require the player to have permission for at least", "one effect and one style to be able to open the GUI");
    public static final RoseSetting<Boolean> GUI_PRESETS_ONLY = create("gui-presets-only", BOOLEAN, false, "If true, only the preset groups will be available in the GUI", "Permissions to open the GUI will change to only open if the user has any preset groups available");
    public static final RoseSetting<Boolean> GUI_PRESETS_HIDE_PARTICLES_DESCRIPTIONS = create("gui-presets-hide-particles-descriptions", BOOLEAN, false, "If true, the particle descriptions in the item lore for preset groups will be hidden");
    public static final RoseSetting<Boolean> GUI_CLOSE_AFTER_GROUP_SELECTED = create("gui-close-after-group-selected", BOOLEAN, true, "If true, the GUI will close after selecting a group (either saved or preset)");
    public static final RoseSetting<Boolean> GUI_BUTTON_SOUND = create("gui-button-sound", BOOLEAN, true, "If clicking a GUI button should make a noise");
    public static final RoseSetting<String> TOGGLE_ON_MOVE = create("toggle-on-move", STRING, "NONE", "Valid values: DISPLAY_FEET, DISPLAY_NORMAL, DISPLAY_OVERHEAD, HIDE, NONE", "DISPLAY_FEET will display particles using the feet style while moving", "DISPLAY_NORMAL will display particles using the normal style while moving", "DISPLAY_OVERHEAD will display particles using the overhead style while moving", "HIDE will hide particles while moving", "NONE will make this setting do nothing", "Note: You can change what styles follow this setting in their individual setting files");
    public static final RoseSetting<Integer> TOGGLE_ON_MOVE_DELAY = create("toggle-on-move-delay", INTEGER, 9, "The time (in ticks) a player has to be standing still before they are considered to be stopped", "This setting has no effect if toggle-on-move is set to false", "The value must be a positive whole number");
    public static final RoseSetting<Boolean> TOGGLE_ON_COMBAT = create("toggle-on-combat", BOOLEAN, false, "If true, particles will be completely disabled while the player is in combat", "Note: You can change what styles follow this setting in their individual setting files");
    public static final RoseSetting<Integer> TOGGLE_ON_COMBAT_DELAY = create("toggle-on-combat-delay", INTEGER, 15, "The time (in seconds) a player has to not be damaged/attacked to be considered out of combat", "This setting has no effect if toggle-on-combat is set to false", "The value must be a positive whole number");
    public static final RoseSetting<Boolean> TOGGLE_ON_COMBAT_INCLUDE_MOBS = create("toggle-on-combat-include-mobs", BOOLEAN, false, "If true, mobs will be included in the combat check in addition to players");
    public static final RoseSetting<List<String>> DISABLED_WORLDS = create("disabled-worlds", STRING_LIST, Collections.singletonList("disabled_world_name"), "A list of worlds that the plugin is disabled in");
    public static final RoseSetting<Boolean> CHECK_PERMISSIONS_ON_LOGIN = create("check-permissions-on-login", BOOLEAN, false, "Should particles a player no longer has permission to use be removed on login?");
    public static final RoseSetting<Integer> MAX_PARTICLES = create("max-particles", INTEGER, 3, "The maximum number of particles a player can apply at once", "The GUI will only display up to 21, don't set this any higher than that");
    public static final RoseSetting<Integer> MAX_GROUPS = create("max-groups", INTEGER, 10, "The maximum number of groups a player can have saved", "The GUI will only display up to 21, don't set this any higher than that");
    public static final RoseSetting<Integer> MAX_FIXED_EFFECTS = create("max-fixed-effects", INTEGER, 5, "Max fixed effects per player");
    public static final RoseSetting<Integer> MAX_FIXED_EFFECT_CREATION_DISTANCE = create("max-fixed-effect-creation-distance", INTEGER, 32, "Max fixed effect creation distance", "Determines how far away a player may create a fixed effect from themselves", "This measurement is in blocks", "Set to 0 for infinite distance");
    public static final RoseSetting<Long> TICKS_PER_PARTICLE = create("ticks-per-particle", LONG, 1L, "How many ticks to wait before spawning more particles", "Increasing this value may cause less lag (if there was any), but will decrease prettiness", "Only use whole numbers greater than or equal to 1", "Going over 3 will likely look terrible");
    public static final RoseSetting<Boolean> DISPLAY_WHEN_INVISIBLE = create("display-when-invisible", BOOLEAN, false, "Should particles be displayed when the player is invisible?", "This includes spectator mode or an invisibility potion");
    public static final RoseSetting<Integer> PARTICLE_RENDER_RANGE_PLAYER = create("particle-render-range-player", INTEGER, 48, "From how many blocks away should a player be able to see the particles from another player?");
    public static final RoseSetting<Integer> PARTICLE_RENDER_RANGE_FIXED_EFFECT = create("particle-render-range-fixed-effect", INTEGER, 192, "From how many blocks away should a player be able to see the particles from a fixed effect?");
    public static final RoseSetting<Integer> RAINBOW_CYCLE_SPEED = create("rainbow-cycle-speed", INTEGER, 2, "How many out of 360 hue ticks to move per game tick", "Higher values make the rainbow cycle faster", "Note: Must be a positive whole number");
    public static final RoseSetting<Float> DUST_SIZE = create("dust-size", FLOAT, 1.0F, "How large should dust particles appear?", "Note: Can include decimals", "Only works in 1.13+");
    public static final RoseSetting<Boolean> TRAIL_MOVE_TO_PLAYER = create("trail-move-to-player", BOOLEAN, false, "Should the 'trail' effect have its target at the player's location?", "This setting only works on 1.20.2+");
    public static final RoseSetting<String> GUI_GROUP_CREATION_MESSAGE_DISPLAY_AREA = create("gui-group-creation-message-display-area", STRING, "ACTION_BAR", "Valid values: ACTION_BAR, TITLE, CHAT", "Where should the GUI group creation countdown message be displayed?", "Note: Server versions less than 1.11.2 will always use CHAT");
    public static final RoseSetting<Boolean> GUI_GROUP_CREATION_BUNGEE_SUPPORT = create("gui-group-creation-bungee-support", BOOLEAN, false, "If true, a message will be displayed in chat telling the player to enter the command", "This might be required for some servers using bungee chat plugins");
    public static final RoseSetting<Boolean> PRESET_GROUPS_ALLOW_OVERLAPPING = create("preset-groups-allow-overlapping", BOOLEAN, false, "If true, applying a preset group will not overwrite the current active particles", "Note: This does not check permissions against the max particles setting, use with caution");
    public static final RoseSetting<Boolean> PRESET_GROUPS_OVERLAPPING_ONE_PER_STYLE = create("preset-groups-overlapping-one-per-style", BOOLEAN, false, "If true, applying a preset group with overlapping enabled will only allow one particle per style", "Existing particles that cause duplicate styles will be overwritten", "This setting has no effect if preset-groups-allow-overlapping is set to false");
    public static final RoseSetting<Boolean> DELETE_INVALID_FIXED_EFFECTS = create("delete-invalid-fixed-effects", BOOLEAN, false, "If true, fixed effects will be deleted if the world, effect, or style does not exist upon a player joining thes erver");

    public static final RoseSetting<ConfigurationSection> GUI_GLASS_COLORS = create("gui-border-colors", "The colors of the glass in the GUI", "Valid colors: https://hub.spigotmc.org/javadocs/spigot/org/bukkit/DyeColor.html");
    public static final RoseSetting<String> GUI_GLASS_COLORS_DEFAULT = create("gui-border-colors.default", STRING, "WHITE");
    public static final RoseSetting<String> GUI_GLASS_COLORS_EDIT_DATA = create("gui-border-colors.edit-data", STRING, "MAGENTA");
    public static final RoseSetting<String> GUI_GLASS_COLORS_EDIT_EFFECT = create("gui-border-colors.edit-effect", STRING, "LIGHT_BLUE");
    public static final RoseSetting<String> GUI_GLASS_COLORS_EDIT_PARTICLE = create("gui-border-colors.edit-particle", STRING, "RED");
    public static final RoseSetting<String> GUI_GLASS_COLORS_EDIT_STYLE = create("gui-border-colors.edit-style", STRING, "BLUE");
    public static final RoseSetting<String> GUI_GLASS_COLORS_MANAGE_GROUPS = create("gui-border-colors.manage-groups", STRING, "BROWN");
    public static final RoseSetting<String> GUI_GLASS_COLORS_MANAGE_PARTICLES = create("gui-border-colors.manage-particles", STRING, "ORANGE");
    public static final RoseSetting<String> GUI_GLASS_COLORS_LOAD_PRESET_GROUPS = create("gui-border-colors.load-preset-groups", STRING, "GREEN");

    public static final RoseSetting<ConfigurationSection> WORLDGUARD_SETTINGS = create("worldguard-settings", "Settings for WorldGuard", "If WorldGuard is not installed, these settings will do nothing", "Use the WorldGuard flag 'player-particles' to allow/deny all particles in a region", "Deny the flag 'player-particles-limited' to only spawn a limited subset of particles in a region", "You can edit the limited region effects and styles within their individual effect/style config files");
    public static final RoseSetting<Long> WORLDGUARD_CHECK_INTERVAL = create("worldguard-settings.check-interval", LONG, 10L, "How often to check if a player is in a region that allows spawning particles", "Measured in ticks");
    public static final RoseSetting<Boolean> WORLDGUARD_ENABLE_BYPASS_PERMISSION = create("worldguard-settings.enable-bypass-permission", BOOLEAN, false, "If true, the permission playerparticles.worldguard.bypass will allow", "the player to bypass the region requirements");

    public static final RoseSetting<ConfigurationSection> GUI_ICON = create("gui-icon",
                     "This configuration option allows you to change the GUI",
                     "icons to whatever block/item you want. If you want to change an effect",
                     "or style icon, use their respective config files.",
                     "Notes: If any of the block/item names are invalid the icon in the GUI",
                     "will be the barrier icon to show that it failed to load.",
                     "You MUST use the Spigot-given name for it to work. You can see",
                     "all the Spigot-given names at the link below:",
                     "https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html",
                     "If multiple icons are listed, they are used for older MC versions"
    );
    public static final RoseSetting<List<String>> GUI_ICON_MISC_PARTICLES = create("gui-icon.misc.particles", STRING_LIST, Collections.singletonList("BLAZE_POWDER"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_GROUPS = create("gui-icon.misc.groups", STRING_LIST, Collections.singletonList("CHEST"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_PRESET_GROUPS = create("gui-icon.misc.preset_groups", STRING_LIST, Collections.singletonList("ENDER_CHEST"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_BACK = create("gui-icon.misc.back", STRING_LIST, Collections.singletonList("ARROW"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_NEXT_PAGE = create("gui-icon.misc.next_page", STRING_LIST, Collections.singletonList("PAPER"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_PREVIOUS_PAGE = create("gui-icon.misc.previous_page", STRING_LIST, Collections.singletonList("PAPER"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_CREATE = create("gui-icon.misc.create", STRING_LIST, Arrays.asList("WRITABLE_BOOK", "BOOK_AND_QUILL"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_EDIT_EFFECT = create("gui-icon.misc.edit_effect", STRING_LIST, Arrays.asList("FIREWORK_ROCKET", "FIREWORK"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_EDIT_STYLE = create("gui-icon.misc.edit_style", STRING_LIST, Collections.singletonList("NETHER_STAR"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_EDIT_DATA = create("gui-icon.misc.edit_data", STRING_LIST, Collections.singletonList("BOOK"));
    public static final RoseSetting<List<String>> GUI_ICON_MISC_RESET = create("gui-icon.misc.reset", STRING_LIST, Collections.singletonList("BARRIER"));

    private Settings() {}

    @Override
    public List<RoseSetting<?>> get() {
        return Collections.unmodifiableList(SETTINGS);
    }

    private static <T> RoseSetting<T> create(String key, SettingSerializer<T> serializer, T defaultValue, String... comments) {
        RoseSetting<T> setting = RoseSetting.ofBackedValue(key, PlayerParticles.getInstance(), serializer, defaultValue, comments);
        SETTINGS.add(setting);
        return setting;
    }

    private static RoseSetting<ConfigurationSection> create(String key, String... comments) {
        RoseSetting<ConfigurationSection> setting = RoseSetting.ofBackedSection(key, PlayerParticles.getInstance(), comments);
        SETTINGS.add(setting);
        return setting;
    }

    /**
     * Used for grabbing/caching configurable GUI Icons from the config.yml
     */
    public enum GuiIcon {
        PARTICLES(Settings.GUI_ICON_MISC_PARTICLES),
        GROUPS(Settings.GUI_ICON_MISC_GROUPS),
        PRESET_GROUPS(Settings.GUI_ICON_MISC_PRESET_GROUPS),
        BACK(Settings.GUI_ICON_MISC_BACK),
        NEXT_PAGE(Settings.GUI_ICON_MISC_NEXT_PAGE),
        PREVIOUS_PAGE(Settings.GUI_ICON_MISC_PREVIOUS_PAGE),
        CREATE(Settings.GUI_ICON_MISC_CREATE),
        EDIT_EFFECT(Settings.GUI_ICON_MISC_EDIT_EFFECT),
        EDIT_STYLE(Settings.GUI_ICON_MISC_EDIT_STYLE),
        EDIT_DATA(Settings.GUI_ICON_MISC_EDIT_DATA),
        RESET(Settings.GUI_ICON_MISC_RESET);

        private final RoseSetting<List<String>> guiSetting;
        private Material material;

        GuiIcon(RoseSetting<List<String>> guiSetting) {
            this.guiSetting = guiSetting;
        }

        /**
         * Gets the Material for this icon from the 'gui-icon.misc' section in the config.yml
         * Tries to get from cache first, otherwise loads it
         *
         * @return The Material for this Icon
         */
        public Material get() {
            if (this.material != null)
                return this.material;

            List<String> materials = this.guiSetting.get();
            for (String name : materials) {
                this.material = ParticleUtils.closestMatch(name);
                if (this.material != null)
                    break;
            }

            if (this.material == null)
                this.material = ParticleUtils.FALLBACK_MATERIAL;

            return this.material;
        }

        /**
         * Resets the cache for gui icons
         */
        public static void reset() {
            for (GuiIcon icon : GuiIcon.values())
                icon.material = null;
        }

    }

}
