package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigurationManager extends Manager {

    private static final String[] HEADER = new String[] {
            "     _________ __                           __________                __   __        __                    _________",
            "     \\______  \\  | _____  ___ __  __________\\______   \\_____ ________/  |_|__| ____ |  |   ____   ______   \\______  \\",
            "     |     ___/  | \\__  \\<   |  |/ __ \\_  __ \\     ___/\\__  \\\\_  __ \\   __\\  |/ ___\\|  | _/ __ \\ /  ___/       /    /",
            "     |    |   |  |__/ __ \\\\___  \\  ___/|  | \\/    |     / __ \\|  | \\/|  | |  \\  \\___|  |_\\  ___/ \\___ \\       /    /",
            "     |____|   |____(____  / ____|\\___  >__|  |____|    (____  /__|   |__| |__|\\___  >____/\\___  >____  >     /____/",
            "                        \\/\\/         \\/                     \\/                    \\/          \\/     \\/"
    };

    public enum Setting {
        CHECK_UPDATES("check-updates", true, "Check for new versions of the plugin"),
        SEND_METRICS("send-metrics", true, "If metrics should be sent to bStats", "I would appreciate it if you left this enabled, it helps me get statistics on how the plugin is used"),
        LOCALE("locale", "en_US", "The locale to use in the /locale folder"),
        MESSAGES_ENABLED("message-enabled", true, "If you're using other plugins to execute commands you may wish to turn off messages"),
        USE_MESSAGE_PREFIX("use-message-prefix", true, "Whether or not to use the message-prefix field when displaying messages"),
        MESSAGE_PREFIX("message-prefix", "&7[&3PlayerParticles&7]", "The prefix to use for all PlayerParticles messages", "This is useless if use-message-prefix is set to false"),
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

        MYSQL_SETTINGS("mysql-settings", null, "Settings for if you want to use MySQL for data management"),
        MYSQL_ENABLED("mysql-settings.enabled", false, "Enable MySQL", "If false, SQLite will be used instead"),
        MYSQL_HOSTNAME("mysql-settings.hostname", "", "MySQL Database Hostname"),
        MYSQL_PORT("mysql-settings.port", 3306, "MySQL Database Port"),
        MYSQL_DATABASE_NAME("mysql-settings.database-name", "", "MySQL Database Name"),
        MYSQL_USER_NAME("mysql-settings.user-name", "", "MySQL Database User Name"),
        MYSQL_USER_PASSWORD("mysql-settings.user-password", "", "MySQL Database User Password"),
        MYSQL_USE_SSL("mysql-settings.use-ssl", false, "If the database connection should use SSL", "You should enable this if your database supports SSL");

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
                        defaultComment += "'" + this.defaultValue + "'";
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
        boolean setHeader = !configFile.exists();

        this.configuration = CommentedFileConfiguration.loadConfiguration(this.playerParticles, configFile);

        if (setHeader)
            this.configuration.addComments(HEADER);

        for (Setting setting : Setting.values()) {
            setting.reset();
            setting.setIfNotExists(this.configuration);
        }

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

}
