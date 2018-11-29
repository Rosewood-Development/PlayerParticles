package com.esophose.playerparticles.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.SettingManager.PSetting;
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
        COMMAND_DESCRIPTIONS,
        COMMAND_DESCRIPTIONS_USAGE,
        COMMAND_DESCRIPTIONS_HELP_1,
        COMMAND_DESCRIPTIONS_HELP_2,
        COMMAND_DESCRIPTION_ADD,
        COMMAND_DESCRIPTION_DATA,
        COMMAND_DESCRIPTION_DEFAULT,
        COMMAND_DESCRIPTION_EDIT,
        COMMAND_DESCRIPTION_EFFECTS,
        COMMAND_DESCRIPTION_FIXED,
        COMMAND_DESCRIPTION_GROUP,
        COMMAND_DESCRIPTION_GUI,
        COMMAND_DESCRIPTION_HELP,
        COMMAND_DESCRIPTION_INFO,
        COMMAND_DESCRIPTION_LIST,
        COMMAND_DESCRIPTION_RELOAD,
        COMMAND_DESCRIPTION_REMOVE,
        COMMAND_DESCRIPTION_RESET,
        COMMAND_DESCRIPTION_STYLES,
        COMMAND_DESCRIPTION_TOGGLE,
        COMMAND_DESCRIPTION_VERSION,
        COMMAND_DESCRIPTION_WORLDS,
        
        // Sub-Command Usage
        COMMAND_DESCRIPTION_FIXED_CREATE,
        COMMAND_DESCRIPTION_FIXED_EDIT,
        COMMAND_DESCRIPTION_FIXED_REMOVE,
        COMMAND_DESCRIPTION_FIXED_LIST,
        COMMAND_DESCRIPTION_FIXED_INFO,
        COMMAND_DESCRIPTION_FIXED_CLEAR,
        COMMAND_DESCRIPTION_GROUP_SAVE,
        COMMAND_DESCRIPTION_GROUP_LOAD,
        COMMAND_DESCRIPTION_GROUP_REMOVE,
        COMMAND_DESCRIPTION_GROUP_LIST,
        COMMAND_DESCRIPTION_GROUP_INFO,
        
        // ID Lookup
        ID_INVALID,
        ID_UNKNOWN,
        
        // Add Command
        ADD_REACHED_MAX,
        ADD_PARTICLE_APPLIED,
        
        // Data Command
        DATA_NO_ARGS,
        
        // Edit Command
        EDIT_INVALID_PROPERTY,
        EDIT_SUCCESS_EFFECT,
        EDIT_SUCCESS_STYLE,
        EDIT_SUCCESS_DATA,
        
        // Group Command
        GROUP_INVALID,
        GROUP_PRESET_NO_PERMISSION,
        GROUP_RESERVED,
        GROUP_NO_NAME,
        GROUP_SAVE_REACHED_MAX,
        GROUP_SAVE_NO_PARTICLES,
        GROUP_SAVE_SUCCESS,
        GROUP_SAVE_SUCCESS_OVERWRITE,
        GROUP_LOAD_SUCCESS,
        GROUP_LOAD_PRESET_SUCCESS,
        GROUP_REMOVE_PRESET,
        GROUP_REMOVE_SUCCESS,
        GROUP_INFO_HEADER,
        GROUP_LIST_NONE,
        GROUP_LIST_OUTPUT,
        GROUP_LIST_PRESETS,
        
        // Reload Command
        RELOAD_SUCCESS,
        RELOAD_NO_PERMISSION,
        
        // Remove Command
        REMOVE_NO_ARGS,
        REMOVE_SUCCESS,
        
        // List Command
        LIST_NONE,
        LIST_YOU_HAVE,
        LIST_OUTPUT,
        
        // Toggle Command
        TOGGLE_ON,
        TOGGLE_OFF,
        
        // Rainbow
        RAINBOW,
        
        // Random
        RANDOM,
        
        // Effects
        EFFECT_NO_PERMISSION,
        EFFECT_INVALID,
        EFFECT_LIST,
        EFFECT_LIST_EMPTY,
        
        // Styles
        STYLE_NO_PERMISSION,
        STYLE_EVENT_SPAWNING_INFO,
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
        FIXED_CREATE_LOOKING_TOO_FAR,
        FIXED_CREATE_EFFECT_INVALID,
        FIXED_CREATE_EFFECT_NO_PERMISSION,
        FIXED_CREATE_STYLE_INVALID,
        FIXED_CREATE_STYLE_NO_PERMISSION,
        FIXED_CREATE_STYLE_NON_FIXABLE,
        FIXED_CREATE_DATA_ERROR,
        FIXED_CREATE_SUCCESS,
        
        FIXED_EDIT_MISSING_ARGS,
        FIXED_EDIT_INVALID_ID,
        FIXED_EDIT_INVALID_PROPERTY,
        FIXED_EDIT_INVALID_COORDS,
        FIXED_EDIT_OUT_OF_RANGE,
        FIXED_EDIT_LOOKING_TOO_FAR,
        FIXED_EDIT_EFFECT_INVALID,
        FIXED_EDIT_EFFECT_NO_PERMISSION,
        FIXED_EDIT_STYLE_INVALID,
        FIXED_EDIT_STYLE_NO_PERMISSION,
        FIXED_EDIT_STYLE_NON_FIXABLE,
        FIXED_EDIT_DATA_ERROR,
        FIXED_EDIT_DATA_NONE,
        FIXED_EDIT_SUCCESS,
        
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
        GUI_COLOR_ICON_NAME,
        GUI_COLOR_INFO,
        GUI_COLOR_SUBTEXT,
        GUI_COLOR_UNAVAILABLE,
        GUI_COMMANDS_INFO,
        GUI_BACK_BUTTON,
        GUI_CLICK_TO_LOAD,
        GUI_SHIFT_CLICK_TO_DELETE,
        GUI_PARTICLE_INFO,
        GUI_PLAYERPARTICLES,
        GUI_ACTIVE_PARTICLES,
        GUI_SAVED_GROUPS,
        GUI_FIXED_EFFECTS,
        GUI_MANAGE_YOUR_PARTICLES,
        GUI_MANAGE_YOUR_PARTICLES_DESCRIPTION,
        GUI_MANAGE_YOUR_GROUPS,
        GUI_MANAGE_YOUR_GROUPS_DESCRIPTION,
        GUI_LOAD_A_PRESET_GROUP,
        GUI_LOAD_A_PRESET_GROUP_DESCRIPTION,
        GUI_SAVE_GROUP,
        GUI_SAVE_GROUP_DESCRIPTION,
        GUI_SAVE_GROUP_DESCRIPTION_2,
        GUI_RESET_PARTICLES,
        GUI_RESET_PARTICLES_DESCRIPTION,
        GUI_PARTICLE_NAME,
        GUI_CLICK_TO_EDIT_PARTICLE,
        GUI_EDITING_PARTICLE,
        GUI_EDIT_EFFECT,
        GUI_EDIT_EFFECT_DESCRIPTION,
        GUI_EDIT_STYLE,
        GUI_EDIT_STYLE_DESCRIPTION,
        GUI_EDIT_DATA,
        GUI_EDIT_DATA_DESCRIPTION,
        GUI_EDIT_DATA_UNAVAILABLE,
        GUI_DATA_NONE,
        GUI_CREATE_PARTICLE,
        GUI_CREATE_PARTICLE_DESCRIPTION,
        GUI_CREATE_PARTICLE_UNAVAILABLE,
        GUI_SELECT_EFFECT,
        GUI_SELECT_EFFECT_DESCRIPTION,
        GUI_SELECT_STYLE,
        GUI_SELECT_STYLE_DESCRIPTION,
        GUI_SELECT_DATA,
        GUI_SELECT_DATA_DESCRIPTION,
        GUI_SELECT_DATA_NOTE,
        GUI_SELECT_DATA_RANDOMIZE_ITEMS,
        GUI_SELECT_DATA_RANDOMIZE_ITEMS_DESCRIPTION,
        GUI_SELECT_DATA_RANDOMIZE_BLOCKS,
        GUI_SELECT_DATA_RANDOMIZE_BLOCKS_DESCRIPTION,
        GUI_EDIT_DATA_COLOR_RED,
        GUI_EDIT_DATA_COLOR_ORANGE,
        GUI_EDIT_DATA_COLOR_YELLOW,
        GUI_EDIT_DATA_COLOR_LIME_GREEN,
        GUI_EDIT_DATA_COLOR_GREEN,
        GUI_EDIT_DATA_COLOR_BLUE,
        GUI_EDIT_DATA_COLOR_CYAN,
        GUI_EDIT_DATA_COLOR_LIGHT_BLUE,
        GUI_EDIT_DATA_COLOR_PURPLE,
        GUI_EDIT_DATA_COLOR_MAGENTA,
        GUI_EDIT_DATA_COLOR_PINK,
        GUI_EDIT_DATA_COLOR_BROWN,
        GUI_EDIT_DATA_COLOR_BLACK,
        GUI_EDIT_DATA_COLOR_GRAY,
        GUI_EDIT_DATA_COLOR_LIGHT_GRAY,
        GUI_EDIT_DATA_COLOR_WHITE;
        // @formatter:on

        private String message;

        /**
         * Gets the name of the message in the config
         * 
         * @return The location in the config that this message is located
         */
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
     * The current lang file name
     */
    private static String langFileName;
    
    private LangManager() {
        
    }

    /**
     * Used to set up the LangManager
     * This should only get called once by the PlayerParticles class, however
     * calling it multiple times wont affect anything negatively
     * 
     * @param resetLangFile If the default.lang file should be updated to the latest version
     */
    public static void reload(boolean resetLangFile) {
        YamlConfiguration lang = configureLangFile(resetLangFile);
        for (Lang messageType : Lang.values())
            messageType.setMessage(lang);
    }

    /**
     * Loads the target .lang file as defined in the config and grabs its YamlConfiguration
     * If it doesn't exist, default to default.lang
     * If default.lang doesn't exist, copy the file from this .jar to the target directory
     * 
     * @param resetLangFile If the default.lang file should be updated to the latest version
     * @return The YamlConfiguration of the target .lang file
     */
    private static YamlConfiguration configureLangFile(boolean resetLangFile) {
        File pluginDataFolder = PlayerParticles.getPlugin().getDataFolder();
        langFileName = PSetting.LANG_FILE.getString();
        File targetLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + langFileName);
        
        if (resetLangFile) {
            File defaultLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/default.lang");
            if (defaultLangFile.exists()) {
                defaultLangFile.delete();
                PlayerParticles.getPlugin().getLogger().warning("default.lang has been reset!");
            }
        }

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
                    PlayerParticles.getPlugin().getLogger().severe("Unable to write default.lang to disk! You and your players will be seeing lots of error messages!");
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
        if (!PSetting.MESSAGES_ENABLED.getBoolean()) return;

        String message = messageType.get(replacements);

        if (message.length() == 0) return;

        if (PSetting.USE_MESSAGE_PREFIX.getBoolean()) {
            message = parseColors(PSetting.MESSAGE_PREFIX.getString()) + " " + message;
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
     * @param includePrefix If the prefix should be included
     */
    public static void sendCustomMessage(Player player, String message) {
        if (!PSetting.MESSAGES_ENABLED.getBoolean()) return;

        if (message.trim().length() == 0) return;

        if (PSetting.USE_MESSAGE_PREFIX.getBoolean()) {
            message = parseColors(PSetting.MESSAGE_PREFIX.getString()) + " " + message;
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
     * Sends a message to a Player without the prefix
     * 
     * @param player The plaeyr to send the message to
     * @param messageType The message type to send the player
     * @param replacements The replacements for the message
     */
    public static void sendSimpleMessage(Player player, Lang messageType, Object... replacements) {
        if (!PSetting.MESSAGES_ENABLED.getBoolean()) return;

        String message = messageType.get(replacements);

        if (message.length() == 0) return;

        if (message.trim().equals("")) return;

        player.sendMessage(message);
    }
    
    /**
     * Sends a message to a PPlayer without the prefix
     * 
     * @param player The plaeyr to send the message to
     * @param messageType The message type to send the player
     * @param replacements The replacements for the message
     */
    public static void sendSimpleMessage(PPlayer pplayer, Lang messageType, Object... replacements) {
        sendSimpleMessage(pplayer.getPlayer(), messageType, replacements);
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
