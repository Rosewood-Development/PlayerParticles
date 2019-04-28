package com.esophose.playerparticles.manager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.SettingManager.PSetting;
import com.esophose.playerparticles.particles.PPlayer;

public class LangManager {

    /**
     * Contains the location in the .lang file of every chat message
     */
    public enum Lang { // @formatter:off
        
        // Command Errors
        COMMAND_ERROR_NO_EFFECTS,
        COMMAND_ERROR_UNKNOWN,

        // Command Descriptions
        COMMAND_DESCRIPTIONS,
        COMMAND_DESCRIPTIONS_USAGE,
        COMMAND_DESCRIPTIONS_HELP_1,
        COMMAND_DESCRIPTIONS_HELP_2,
        COMMAND_DESCRIPTIONS_HELP_OTHER,
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
        
        // Other Command
        OTHER_NO_PERMISSION,
        OTHER_MISSING_ARGS,
        OTHER_UNKNOWN_PLAYER,
        OTHER_UNKNOWN_COMMAND,
        OTHER_SUCCESS,
        
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
        GROUP_NO_PERMISSION,
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
        REMOVE_ID_SUCCESS,
        REMOVE_EFFECT_SUCCESS,
        REMOVE_EFFECT_NONE,
        REMOVE_STYLE_SUCCESS,
        REMOVE_STYLE_NONE,
        REMOVE_UNKNOWN,
        
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
        
        // Update Available
        UPDATE_AVAILABLE,
        
        // GUI
        GUI_DISABLED,
        GUI_COLOR_ICON_NAME,
        GUI_COLOR_INFO,
        GUI_COLOR_SUBTEXT,
        GUI_COLOR_UNAVAILABLE,
        GUI_COMMANDS_INFO,
        GUI_BACK_BUTTON,
        GUI_NEXT_PAGE_BUTTON,
        GUI_PREVIOUS_PAGE_BUTTON,
        GUI_CLICK_TO_LOAD,
        GUI_SHIFT_CLICK_TO_DELETE,
        GUI_PARTICLE_INFO,
        GUI_PLAYERPARTICLES,
        GUI_ACTIVE_PARTICLES,
        GUI_SAVED_GROUPS,
        GUI_FIXED_EFFECTS,
        GUI_EDIT_PRIMARY_EFFECT,
        GUI_EDIT_PRIMARY_EFFECT_DESCRIPTION,
        GUI_EDIT_PRIMARY_STYLE,
        GUI_EDIT_PRIMARY_STYLE_MISSING_EFFECT,
        GUI_EDIT_PRIMARY_STYLE_DESCRIPTION,
        GUI_EDIT_PRIMARY_DATA,
        GUI_EDIT_PRIMARY_DATA_MISSING_EFFECT,
        GUI_EDIT_PRIMARY_DATA_UNAVAILABLE,
        GUI_EDIT_PRIMARY_DATA_DESCRIPTION,
        GUI_MANAGE_YOUR_PARTICLES,
        GUI_MANAGE_YOUR_PARTICLES_DESCRIPTION,
        GUI_MANAGE_YOUR_GROUPS,
        GUI_MANAGE_YOUR_GROUPS_DESCRIPTION,
        GUI_LOAD_A_PRESET_GROUP,
        GUI_LOAD_A_PRESET_GROUP_DESCRIPTION,
        GUI_SAVE_GROUP,
        GUI_SAVE_GROUP_DESCRIPTION,
        GUI_SAVE_GROUP_FULL,
        GUI_SAVE_GROUP_NO_PARTICLES,
        GUI_SAVE_GROUP_HOTBAR_MESSAGE,
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
        GUI_EDIT_DATA_COLOR_WHITE; // @formatter:on

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
     * @param resetLangFile If the default lang files should be updated to the latest version
     */
    public static void reload(boolean resetLangFile) {
        YamlConfiguration lang = configureLangFile(resetLangFile);
        for (Lang messageType : Lang.values())
            messageType.setMessage(lang);
    }

    /**
     * Loads the target .lang file as defined in the config and grabs its YamlConfiguration
     * If it doesn't exist, default to en_US.lang
     * If the default lang files don't exist, copy the files from this .jar to the target directory
     * 
     * @param resetLangFiles If the default lang files should be updated to the latest version
     * @return The YamlConfiguration of the target .lang file
     */
    private static YamlConfiguration configureLangFile(boolean resetLangFiles) {
        File pluginDataFolder = PlayerParticles.getPlugin().getDataFolder();
        langFileName = PSetting.LANG_FILE.getString();
        File targetLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + langFileName);
        
        // TODO: Move this somewhere else
        Set<String> defaultLangFileNames = new HashSet<>();
        defaultLangFileNames.add("en_US.lang");
        defaultLangFileNames.add("fr_FR.lang");
        defaultLangFileNames.add("vi_VN.lang");
        
        targetLangFile.getParentFile().mkdir(); // Make sure the directory always exists
        
        if (resetLangFiles) {
            for (String fileName : defaultLangFileNames) {
                File file = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + fileName);
                
                // Delete the file if it already exists
                if (file.exists()) {
                    file.delete(); 
                }
                
                // Generate the new file
                try (InputStream stream = PlayerParticles.getPlugin().getResource("lang/" + fileName)) {
                    Files.copy(stream, Paths.get(file.getAbsolutePath()));
                } catch (IOException ex) {
                    ex.printStackTrace();
                    PlayerParticles.getPlugin().getLogger().severe("Unable to write " + fileName + " to disk! This wasn't supposed to happen!");
                }
            }
            
            PlayerParticles.getPlugin().getLogger().warning("The default lang files have been reset!");
        } else {
            // Make sure the default lang files still exist, if not, create them
            boolean foundMissingFile = false;
            for (String fileName : defaultLangFileNames) {
                File file = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + fileName);
                
                // Generate the new file if it doesn't exist
                if (!file.exists()) {
                    foundMissingFile = true;
                    try (InputStream stream = PlayerParticles.getPlugin().getResource("lang/" + fileName)) {
                        Files.copy(stream, Paths.get(file.getAbsolutePath()));
                    } catch (IOException ex) {
                        ex.printStackTrace();
                        PlayerParticles.getPlugin().getLogger().severe("Unable to write " + fileName + " to disk! This wasn't supposed to happen!");
                    }
                }
            }
            
            if (foundMissingFile) 
                PlayerParticles.getPlugin().getLogger().warning("One or more default lang files were missing, recreated them!");
        }

        if (!targetLangFile.exists()) { // Target .lang file didn't exist, default to en_US.lang
            if (!langFileName.equals("en_US.lang")) {
                PlayerParticles.getPlugin().getLogger().warning("Couldn't find lang file '" + langFileName + "', defaulting to en_US.lang");
            }
            langFileName = "en_US.lang";
            targetLangFile = new File(pluginDataFolder.getAbsolutePath() + "/lang/" + langFileName);
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
     * Sends a message to the given PPlayer
     * 
     * @param pplayer The player to send the message to
     * @param messageType The message to send to the player
     * @param replacements The replacements for the message
     */
    public static void sendMessage(PPlayer pplayer, Lang messageType, Object... replacements) {
        if (!PSetting.MESSAGES_ENABLED.getBoolean()) return;

        String message = messageType.get(replacements);

        if (message.length() == 0) return;

        if (PSetting.USE_MESSAGE_PREFIX.getBoolean()) {
            message = parseColors(PSetting.MESSAGE_PREFIX.getString()) + " " + message;
        }

        if (message.trim().equals("")) return;

        pplayer.getMessageDestination().sendMessage(message);
    }

    /**
     * Sends a custom message to a PPlayer
     * Used in cases of string building
     * 
     * @param pplayer The player to send the message to
     * @param message The message to send to the player
     */
    public static void sendCustomMessage(PPlayer pplayer, String message) {
        if (!PSetting.MESSAGES_ENABLED.getBoolean()) return;

        if (message.trim().length() == 0) return;

        if (PSetting.USE_MESSAGE_PREFIX.getBoolean()) {
            message = parseColors(PSetting.MESSAGE_PREFIX.getString()) + " " + message;
        }

        pplayer.getMessageDestination().sendMessage(message);
    }
    
    /**
     * Sends a message to a PPlayer without the prefix
     * 
     * @param pplayer The player to send the message to
     * @param messageType The message type to send the player
     * @param replacements The replacements for the message
     */
    public static void sendSimpleMessage(PPlayer pplayer, Lang messageType, Object... replacements) {
        if (!PSetting.MESSAGES_ENABLED.getBoolean()) return;

        String message = messageType.get(replacements);

        if (message.length() == 0) return;

        if (message.trim().equals("")) return;

        pplayer.getMessageDestination().sendMessage(message);
    }
    
    /**
     * Sends a message to a CommandSender
     * 
     * @param sender The CommandSender to send the message to
     * @param messageType The message type to send the player
     * @param replacements The replacements for the message
     */
    public static void sendCommandSenderMessage(CommandSender sender, Lang messageType, Object... replacements) {
        if (!PSetting.MESSAGES_ENABLED.getBoolean()) return;

        String message = messageType.get(replacements);

        if (message.length() == 0) return;

        if (PSetting.USE_MESSAGE_PREFIX.getBoolean()) {
            message = parseColors(PSetting.MESSAGE_PREFIX.getString()) + " " + message;
        }

        if (message.trim().equals("")) return;

        sender.sendMessage(message);
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
