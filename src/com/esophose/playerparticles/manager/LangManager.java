/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.particles.PPlayer;

public class LangManager {

    /**
     * Contains the location in the .lang file of every chat message
     */
    public static enum Lang {

        // Command Descriptions
        ADD_COMMAND_DESCRIPTION("add-command-description"),
        DATA_COMMAND_DESCRIPTION("data-command-description"),
        DEFAULT_COMMAND_DESCRIPTION("default-command-description"),
        EDIT_COMMAND_DESCRIPTION("edit-command-description"),
        EFFECT_COMMAND_DESCRIPTION("effect-command-description"),
        EFFECTS_COMMAND_DESCRIPTION("effects-command-description"),
        FIXED_COMMAND_DESCRIPTION("fixed-command-description"),
        GROUP_COMMAND_DESCRIPTION("group-command-description"),
        GUI_COMMAND_DESCRIPTION("gui-command-description"),
        HELP_COMMAND_DESCRIPTION("help-command-description"),
        INFO_COMMAND_DESCRIPTION("info-command-description"),
        LIST_COMMAND_DESCRIPTION("list-command-description"),
        REMOVE_COMMAND_DESCRIPTION("remove-command-description"),
        RESET_COMMAND_DESCRIPTION("reset-command-description"),
        STYLE_COMMAND_DESCRIPTION("style-command-description"),
        STYLES_COMMAND_DESCRIPTION("styles-command-description"),
        VERSION_COMMAND_DESCRIPTION("version-command-description"),
        WORLDS_COMMAND_DESCRIPTION("worlds-command-description"),

        COMMAND_REMOVED("command-removed"),

        // Particles
        NO_PERMISSION("message-no-permission"),
        NO_PARTICLES("message-no-particles"),
        NOW_USING("message-now-using"),
        CLEARED_PARTICLES("message-cleared-particles"),
        INVALID_TYPE("message-invalid-type"),
        PARTICLE_USAGE("message-particle-usage"),

        // Styles
        NO_PERMISSION_STYLE("message-no-permission-style"),
        NO_STYLES("message-no-styles"),
        NOW_USING_STYLE("message-now-using-style"),
        CLEARED_STYLE("message-cleared-style"),
        INVALID_TYPE_STYLE("message-invalid-type-style"),
        STYLE_USAGE("message-style-usage"),

        // Data
        DATA_USAGE("message-data-usage"),
        NO_DATA_USAGE("message-no-data-usage"),
        DATA_APPLIED("message-data-applied"),
        DATA_INVALID_ARGUMENTS("message-data-invalid-arguments"),
        DATA_MATERIAL_UNKNOWN("message-data-material-unknown"),
        DATA_MATERIAL_MISMATCH("message-data-material-mismatch"),
        NOTE_DATA_USAGE("message-note-data-usage"),
        COLOR_DATA_USAGE("message-color-data-usage"),
        ITEM_DATA_USAGE("message-item-data-usage"),
        BLOCK_DATA_USAGE("message-block-data-usage"),

        // Fixed Effects
        FIXED_COMMAND_DESC_CREATE("message-fixed-command-desc-create"),
        FIXED_COMMAND_DESC_REMOVE("message-fixed-command-desc-remove"),
        FIXED_COMMAND_DESC_LIST("message-fixed-command-desc-list"),
        FIXED_COMMAND_DESC_INFO("message-fixed-command-desc-info"),
        FIXED_COMMAND_DESC_CLEAR("message-fixed-command-desc-clear"),
        CREATE_FIXED_MISSING_ARGS("message-create-fixed-missing-args"),
        CREATE_FIXED_INVALID_COORDS("message-create-fixed-invalid-coords"),
        CREATE_FIXED_OUT_OF_RANGE("message-create-fixed-out-of-range"),
        CREATE_FIXED_INVALID_EFFECT("message-create-fixed-invalid-effect"),
        CREATE_FIXED_NO_PERMISSION_EFFECT("message-create-fixed-no-permission-effect"),
        CREATE_FIXED_INVALID_STYLE("message-create-fixed-invalid-style"),
        CREATE_FIXED_NO_PERMISSION_STYLE("message-create-fixed-no-permission-style"),
        CREATE_FIXED_NON_FIXABLE_STYLE("message-create-fixed-non-fixable-style"),
        CREATE_FIXED_DATA_ERROR("message-create-fixed-data-error"),
        CREATE_FIXED_SUCCESS("message-create-fixed-success"),
        REMOVE_FIXED_NONEXISTANT("message-remove-fixed-nonexistant"),
        REMOVE_FIXED_NO_ARGS("message-remove-fixed-no-args"),
        REMOVE_FIXED_INVALID_ARGS("message-remove-fixed-invalid-args"),
        REMOVE_FIXED_SUCCESS("message-remove-fixed-success"),
        LIST_FIXED_SUCCESS("message-list-fixed-success"),
        LIST_FIXED_NONE("message-list-fixed-none"),
        INFO_FIXED_NONEXISTANT("message-info-fixed-nonexistant"),
        INFO_FIXED_NO_ARGS("message-info-fixed-no-args"),
        INFO_FIXED_INVALID_ARGS("message-info-fixed-invalid-args"),
        INFO_FIXED_INFO("message-info-fixed-info"),
        CLEAR_FIXED_NO_PERMISSION("message-clear-no-permission"),
        CLEAR_FIXED_NO_ARGS("message-clear-no-args"),
        CLEAR_FIXED_INVALID_ARGS("message-clear-invalid-args"),
        CLEAR_FIXED_SUCCESS("message-clear-success"),
        NO_PERMISSION_FIXED("message-no-permission-fixed"),
        MAX_FIXED_EFFECTS_REACHED("message-max-fixed-effects-reached"),
        INVALID_FIXED_COMMAND("message-invalid-fixed-command"),

        // GUI
        GUI_DISABLED("message-gui-disabled"),
        GUI_BY_DEFAULT("message-gui-by-default"),
        GUI_BACK_BUTTON("message-gui-back-button"),
        GUI_ICON_NAME_COLOR("message-gui-icon-name-color"),
        GUI_ICON_CURRENT_ACTIVE("message-gui-icon-current-active"),
        GUI_ICON_SETS_TO("message-gui-icon-sets-to"),
        GUI_ICON_SET_YOUR("message-gui-icon-set-your"),
        GUI_NO_ACCESS_TO("message-gui-no-access-to"),
        GUI_NO_DATA("message-gui-no-data"),

        // Prefixes
        USE("message-use"),
        USAGE("message-usage"),
        RESET("message-reset"),

        // Other
        INVALID_ARGUMENTS("message-invalid-arguments"),
        AVAILABLE_COMMANDS("message-available-commands"),
        DISABLED_WORLDS_NONE("message-disabled-worlds-none"),
        DISABLED_WORLDS("message-disabled-worlds"),
        COMMAND_USAGE("message-command-usage"),
        EXECUTED_FOR_PLAYER("message-executed-for-player"),
        FAILED_EXECUTE_NOT_FOUND("message-failed-execute-not-found"),
        FAILED_EXECUTE_NO_PERMISSION("message-failed-execute-no-permission");

        private String fileLocation;
        private String message;

        Lang(String fileLocation) {
            this.fileLocation = fileLocation;
        }

        /**
         * Sets the message from the lang file
         * 
         * @param langFile The lang file to pull the message from
         */
        protected void setMessage(YamlConfiguration langFile) {
            String langMessage = langFile.getString(this.fileLocation);
            if (langMessage == null) {
                langMessage = "&cMissing message in " + langFileName + ": " + this.fileLocation + ". Contact a server administrator.";
                PlayerParticles.getPlugin().getLogger().warning("Missing message in " + langFileName + ": " + this.fileLocation);
            }
            this.message = parseColors(langMessage);
        }

        /**
         * Gets the message this enum represents
         * 
         * @return The message
         */
        public String get(String... replacements) {
            return String.format(this.message, (Object[]) replacements);
        }

        /**
         * Gets the message this enum represents and replace {TYPE} with a replacement value
         * 
         * @param replacement The text to replace {TYPE} with
         * @return The message with {TYPE} replaced with the replacement value
         */
        public String getMessageReplaced(String replacement) {
            return this.message.replaceAll("\\{TYPE\\}", replacement);
        }
    }

    /**
     * Stores if messages and their prefixes should be displayed
     */
    private static boolean messagesEnabled, prefixEnabled;
    /**
     * The prefix to place before all sent messages contained in the config
     */
    private static String messagePrefix;
    /**
     * The current lang file name
     */
    private static String langFileName;

    /**
     * Used to set up the LangManager
     * This should only get called once by the PlayerParticles class, however
     * calling it multiple times wont affect anything negatively
     */
    public static void setup() {
        FileConfiguration config = PlayerParticles.getPlugin().getConfig();
        messagesEnabled = config.getBoolean("messages-enabled");
        prefixEnabled = config.getBoolean("use-message-prefix");
        messagePrefix = parseColors(config.getString("message-prefix"));

        YamlConfiguration lang = configureLangFile(config);
        if (lang == null) {
            messagesEnabled = false;
        } else {
            for (Lang messageType : Lang.values())
                messageType.setMessage(lang);
        }
    }

    /**
     * Loads the target .lang file as defined in the config and grabs its YamlConfiguration
     * If it doesn't exist, default to en_US.lang
     * If en_US.lang doesn't exist, copy the file from this .jar to the target directory
     * 
     * @param config The plugin's configuration file
     * @return The YamlConfiguration of the target .lang file
     */
    private static YamlConfiguration configureLangFile(FileConfiguration config) {
        File pluginDataFolder = PlayerParticles.getPlugin().getDataFolder();
        langFileName = config.getString("lang-file");
        File targetLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + langFileName);

        if (!targetLangFile.exists()) { // Target .lang file didn't exist, default to en_US.lang
            if (!langFileName.equals("en_US.lang")) {
                PlayerParticles.getPlugin().getLogger().warning("Couldn't find lang file '" + langFileName + "', defaulting to en_US.lang");
            }
            langFileName = "en_US.lang";

            targetLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + langFileName);
            if (!targetLangFile.exists()) { // en_US.lang didn't exist, create it
                try (InputStream stream = PlayerParticles.getPlugin().getResource("lang/en_US.lang")) {
                    targetLangFile.getParentFile().mkdir(); // Make sure the directory always exists
                    Files.copy(stream, Paths.get(targetLangFile.getAbsolutePath()));
                    return YamlConfiguration.loadConfiguration(targetLangFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    PlayerParticles.getPlugin().getLogger().severe("Unable to write en_US.lang to disk! All messages for the plugin have been disabled until this is fixed!");
                    return null;
                }
            }
        }

        return YamlConfiguration.loadConfiguration(targetLangFile);
    }

    /**
     * Sends a message to the given player
     * 
     * @param player The player to send the message to
     * @param messageType The message to send to the player
     */
    public static void sendMessage(Player player, Lang messageType) {
        if (!messagesEnabled) return;

        String message = messageType.get();
        if (message.length() == 0) return;

        if (prefixEnabled) {
            message = messagePrefix + " " + message;
        }

        if (message.trim().equals("")) return;

        player.sendMessage(message);
    }

    /**
     * Sends a message to the given PPlayer
     * 
     * @param pplayer The player to send the message to
     * @param messageType The message to send to the player
     */
    public static void sendMessage(PPlayer pplayer, Lang messageType) {
        sendMessage(pplayer.getPlayer(), messageType);
    }

    /**
     * Sends a message to the given player and allows for replacing {TYPE}
     * 
     * @param player The player to send the message to
     * @param messageType The message to send to the player
     * @param typeReplacement What {TYPE} should be replaced with
     */
    public static void sendMessage(Player player, Lang messageType, String typeReplacement) {
        if (!messagesEnabled) return;

        String message = messageType.getMessageReplaced(typeReplacement);
        if (message.trim().length() == 0) return;

        if (prefixEnabled) {
            message = messagePrefix + " " + message;
        }

        player.sendMessage(message);
    }

    /**
     * Sends a message to the given PPlayer and allows for replacing {TYPE}
     * 
     * @param pplayer The player to send the message to
     * @param messageType The message to send to the player
     * @param typeReplacement What {TYPE} should be replaced with
     */
    public static void sendMessage(PPlayer pplayer, Lang messageType, String typeReplacement) {
        sendMessage(pplayer.getPlayer(), messageType, typeReplacement);
    }

    /**
     * Sends a custom message to a player
     * Used in cases of string building
     * 
     * @param player The player to send the message to
     * @param message The message to send to the player
     */
    public static void sendCustomMessage(Player player, String message) {
        if (!messagesEnabled) return;

        if (message.trim().length() == 0) return;

        if (prefixEnabled) {
            message = messagePrefix + " " + message;
        }

        player.sendMessage(message);
    }

    /**
     * Sends a custom message to a PPlayer
     * Used in cases of string building
     * 
     * @param pplayer The player to send the message to
     * @param message The message to send to the player
     */
    public static void sendCustomMessage(PPlayer pplayer, String message) {
        sendCustomMessage(pplayer.getPlayer(), message);
    }

    /**
     * Translates all ampersand symbols into the Minecraft chat color symbol
     * 
     * @param message The input string
     * @return The output string, parsed
     */
    public static String parseColors(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

}
