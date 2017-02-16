/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.manager;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.PlayerParticles;

public class MessageManager {

	/**
	 * Contains the location in the config of every chat message
	 */
	public static enum MessageType {

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
		USE_STYLE("message-use-style"),
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
		NO_PERMISSION_FIXED("message-no-permission-fixed"),
		MAX_FIXED_EFFECTS_REACHED("message-max-fixed-effects-reached"),
		INVALID_FIXED_COMMAND("message-invalid-fixed-command"),
		FIXED_COMMAND_DESC_CREATE("message-fixed-command-desc-create"),
		FIXED_COMMAND_DESC_REMOVE("message-fixed-command-desc-remove"),
		FIXED_COMMAND_DESC_LIST("message-fixed-command-desc-list"),
		FIXED_COMMAND_DESC_INFO("message-fixed-command-desc-info"),
		
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

		public String configLocation;

		MessageType(String configLocation) {
			this.configLocation = configLocation;
		}

		/**
		 * Gets the message with the given config path
		 * 
		 * @return The message from the config
		 */
		public String getMessage() {
			return ChatColor.translateAlternateColorCodes('&', config.getString(this.configLocation));
		}
	}

	/**
	 * Stores the main config for quick access
	 */
	private static FileConfiguration config;
	/**
	 * Stores if messages and their prefixes should be displayed
	 */
	private static boolean messagesEnabled, prefixEnabled;
	/**
	 * The prefix to place before all sent messages contained in the config
	 */
	private static String messagePrefix;

	/**
	 * Used to set up the MessageManager
	 * This should only get called once by the PlayerParticles class, however
	 * calling it multiple times wont affect anything negatively
	 */
	public static void setup() {
		config = PlayerParticles.getPlugin().getConfig();
		messagesEnabled = config.getBoolean("messages-enabled");
		prefixEnabled = config.getBoolean("use-message-prefix");
		messagePrefix = parseColors(config.getString("message-prefix"));
	}

	/**
	 * Sends a message to the given player
	 * 
	 * @param player The player to send the message to
	 * @param messageType The message to send to the player
	 */
	public static void sendMessage(Player player, MessageType messageType) {
		if (!messagesEnabled) return;
		
		String message = messageType.getMessage();
		if (prefixEnabled) {
			message = messagePrefix + " " + message;
		}
		player.sendMessage(message);
	}
	
	/**
	 * Sends a message to the given player and allows for replacing {TYPE}
	 * @param player The player to send the message to
	 * @param messageType The message to send to the player
	 * @param typeReplacement What {TYPE} should be replaced with
	 */
	public static void sendMessage(Player player, MessageType messageType, String typeReplacement) {
		if (!messagesEnabled) return;
		
		String message = messageType.getMessage().replaceAll("\\{TYPE\\}", typeReplacement);
		if (prefixEnabled) {
			message = messagePrefix + " " + message;
		}
		player.sendMessage(message);
	}
	
	/**
	 * Sends a custom message
	 * Used in cases of string building
	 * 
	 * @param player The player to send the message to
	 * @param message The message to send to the player
	 */
	public static void sendCustomMessage(Player player, String message) {
		if (!messagesEnabled) return;

		if (prefixEnabled) {
			message = messagePrefix + " " + message;
		}
		player.sendMessage(message);
	}

	/**
	 * Translates all ampersand symbols into the Minecraft chat color symbol
	 * 
	 * @param message The input
	 * @return The output, parsed
	 */
	public static String parseColors(String message) {
		return ChatColor.translateAlternateColorCodes('&', message);
	}

}
