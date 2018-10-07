package com.esophose.playerparticles.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

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
    public static enum Lang { // @formatter:off
        
        // Command Errors
        COMMAND_ERROR_NO_EFFECTS,
        COMMAND_ERROR_UNKNOWN,

        // Command Descriptions
        COMMAND_DESCRIPTION_ADD,
        COMMAND_DESCRIPTION_DATA,
        COMMAND_DESCRIPTION_DEFAULT,
        COMMAND_DESCRIPTION_EDIT,
        COMMAND_DESCRIPTION_EFFECT,
        COMMAND_DESCRIPTION_EFFECTS,
        COMMAND_DESCRIPTION_FIXED,
        COMMAND_DESCRIPTION_GROUP,
        COMMAND_DESCRIPTION_GUI,
        COMMAND_DESCRIPTION_HELP,
        COMMAND_DESCRIPTION_INFO,
        COMMAND_DESCRIPTION_LIST,
        COMMAND_DESCRIPTION_REMOVE,
        COMMAND_DESCRIPTION_RESET,
        COMMAND_DESCRIPTION_STYLE,
        COMMAND_DESCRIPTION_STYLES,
        COMMAND_DESCRIPTION_VERSION,
        COMMAND_DESCRIPTION_WORLDS,
        
        // Sub-Command Usage
        COMMAND_DESCRIPTION_FIXED_CREATE,
        COMMAND_DESCRIPTION_FIXED_REMOVE,
        COMMAND_DESCRIPTION_FIXED_LIST,
        COMMAND_DESCRIPTION_FIXED_INFO,
        COMMAND_DESCRIPTION_FIXED_CLEAR,
        COMMAND_DESCRIPTION_GROUP_SAVE,
        COMMAND_DESCRIPTION_GROUP_LOAD,
        COMMAND_DESCRIPTION_GROUP_REMOVE,
        COMMAND_DESCRIPTION_GROUP_LIST,
        COMMAND_DESCRIPTION_GROUP_INFO,
        
        // Add Command
        COMMAND_ADD_PARTICLE_APPLIED,
        
        // Data Command
        COMMAND_DATA_NO_ARGS,
        
        // Remove Command
        COMMAND_REMOVE_NO_ARGS,
        COMMAND_REMOVE_ARGS_INVALID,
        COMMAND_REMOVE_INVALID_ID,
        COMMAND_REMOVE_SUCCESS,
        
        // List Command
        COMMAND_LIST_NONE,
        COMMAND_LIST_YOU_HAVE,
        COMMAND_LIST_OUTPUT,
        
        // Rainbow
        RAINBOW,
        
        // Effects
        EFFECT_NO_PERMISSION,
        EFFECT_INVALID,
        EFFECT_LIST,
        EFFECT_LIST_EMPTY,
        
        // Styles
        STYLE_NO_PERMISSION,
        STYLE_INVALID,
        STYLE_LIST,
        
        // Data
        DATA_USAGE_NONE,
        DATA_USAGE_BLOCK,
        DATA_USAGE_ITEM,
        DATA_USAGE_COLOR,
        DATA_USAGE_NOTE,
        DATA_INVALID_BLOCK,
        DATA_INVALID_ITEM,
        DATA_INVALID_COLOR,
        DATA_INVALID_NOTE,
        DATA_INVALID_MATERIAL_NOT_ITEM,
        DATA_INVALID_MATERIAL_NOT_BLOCK,
        DATA_INVALID_MATERIAL_ITEM,
        DATA_INVALID_MATERIAL_BLOCK,
        
        // Worlds
        DISABLED_WORLDS,
        DISABLED_WORLDS_NONE,
        
        // Reset
        RESET_SUCCESS,
        
        // Fixed Effects
        FIXED_CREATE_MISSING_ARGS,
        FIXED_CREATE_INVALID_COORDS,
        FIXED_CREATE_OUT_OF_RANGE,
        FIXED_CREATE_EFFECT_INVALID,
        FIXED_CREATE_EFFECT_NO_PERMISSION,
        FIXED_CREATE_STYLE_INVALID,
        FIXED_CREATE_STYLE_NO_PERMISSION,
        FIXED_CREATE_STYLE_NON_FIXABLE,
        FIXED_CREATE_DATA_ERROR,
        FIXED_CREATE_SUCCESS,
        FIXED_REMOVE_INVALID,
        FIXED_REMOVE_NO_ARGS,
        FIXED_REMOVE_ARGS_INVALID,
        FIXED_REMOVE_SUCCESS,
        FIXED_LIST_NONE,
        FIXED_LIST_SUCCESS,
        FIXED_INFO_INVALID,
        FIXED_INFO_NO_ARGS,
        FIXED_INFO_INVALID_ARGS,
        FIXED_INFO_SUCCESS,
        FIXED_CLEAR_NO_PERMISSION,
        FIXED_CLEAR_NO_ARGS,
        FIXED_CLEAR_INVALID_ARGS,
        FIXED_CLEAR_SUCCESS,
        FIXED_NO_PERMISSION,
        FIXED_MAX_REACHED,
        FIXED_INVALID_COMMAND,
        
        // GUI
        GUI_DISABLED,
        GUI_BY_DEFAULT,
        GUI_BACK_BUTTON,
        GUI_ICON_NAME_COLOR,
        GUI_ICON_CURRENT_ACTIVE,
        GUI_ICON_SETS_TO,
        GUI_ICON_SET_YOUR,
        GUI_NO_ACCESS_TO,
        GUI_NO_DATA; // @formatter:on

        private String message;

        private String getConfigName() {
            return this.name().toLowerCase().replaceAll("_", "-");
        }

        /**
         * Sets the message from the lang file
         * 
         * @param langFile The lang file to pull the message from
         */
        private void setMessage(YamlConfiguration langFile) {
            String fileLocation = this.getConfigName();
            String langMessage = langFile.getString(fileLocation);
            if (langMessage == null) {
                langMessage = "&cMissing message in " + langFileName + ": " + fileLocation + ". Contact a server administrator.";
                PlayerParticles.getPlugin().getLogger().warning("Missing message in " + langFileName + ": " + fileLocation);
            }
            this.message = parseColors(langMessage);
        }

        /**
         * Gets the message this enum represents
         * 
         * @param replacements The replacements for the message
         * @return The message with the replacements applied
         */
        private String get(Object... replacements) {
            return new MessageFormat(this.message).format(replacements);
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
     * If it doesn't exist, default to default.lang
     * If default.lang doesn't exist, copy the file from this .jar to the target directory
     * 
     * @param config The plugin's configuration file
     * @return The YamlConfiguration of the target .lang file
     */
    private static YamlConfiguration configureLangFile(FileConfiguration config) {
        File pluginDataFolder = PlayerParticles.getPlugin().getDataFolder();
        langFileName = config.getString("lang-file");
        File targetLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + langFileName);

        if (!targetLangFile.exists()) { // Target .lang file didn't exist, default to default.lang
            if (!langFileName.equals("default.lang")) {
                PlayerParticles.getPlugin().getLogger().warning("Couldn't find lang file '" + langFileName + "', defaulting to default.lang");
            }
            langFileName = "default.lang";

            targetLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + langFileName);
            if (!targetLangFile.exists()) { // default.lang didn't exist, create it
                try (InputStream stream = PlayerParticles.getPlugin().getResource("lang/default.lang")) {
                    targetLangFile.getParentFile().mkdir(); // Make sure the directory always exists
                    Files.copy(stream, Paths.get(targetLangFile.getAbsolutePath()));
                    return YamlConfiguration.loadConfiguration(targetLangFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                    PlayerParticles.getPlugin().getLogger().severe("Unable to write default.lang to disk! All messages for the plugin have been disabled until this is fixed!");
                    return null;
                }
            }
        }

        return YamlConfiguration.loadConfiguration(targetLangFile);
    }

    /**
     * Gets a formatted and replaced message
     * 
     * @param messageType The message type to get
     * @param replacements The replacements fot the message
     * @return The Lang in text form with its replacements applied
     */
    public static String getText(Lang messageType, Object... replacements) {
        return messageType.get(replacements);
    }

    /**
     * Sends a message to the given player
     * 
     * @param player The player to send the message to
     * @param messageType The message to send to the player
     * @param replacements The replacements for the message
     */
    public static void sendMessage(Player player, Lang messageType, Object... replacements) {
        if (!messagesEnabled) return;

        String message = messageType.get(replacements);

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
     * @param replacements The replacements for the message
     */
    public static void sendMessage(PPlayer pplayer, Lang messageType, Object... replacements) {
        sendMessage(pplayer.getPlayer(), messageType, replacements);
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
