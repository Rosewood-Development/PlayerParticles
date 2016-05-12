/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MessageManager {

	/**
	 * The instance of the MessageManager, we only need one of these
	 */
	private static MessageManager instance = new MessageManager();
	/**
	 * Values contained in the config used for custom messages
	 */
	private boolean messagesEnabled, prefix;
	/**
	 * The prefix to place before all sent messages contained in the config
	 */
	private String messagePrefix;
	
	/**
	 * Sets up all the above variables with values from the plugin config
	 */
	private MessageManager() {
		messagesEnabled = PlayerParticles.getPlugin().getConfig().getBoolean("messages-enabled");
		prefix = PlayerParticles.getPlugin().getConfig().getBoolean("use-message-prefix");
		messagePrefix = PlayerParticles.getPlugin().getConfig().getString("message-prefix");
		messagePrefix = messagePrefix.replace("&", "§");
	}
	
	/**
	 * Gets the instance of the MessageManager
	 * 
	 * @return The instance of the MessageManager
	 */
	public static MessageManager getInstance() {
		return instance;
	}
	
	/**
	 * Sends a message to a player
	 * 
	 * @param player The player to send the message to
	 * @param message The message to send to the player
	 * @param color The chat color to put before the message
	 */
	public void sendMessage(Player player, String message, ChatColor color) {
		if(!messagesEnabled) return;
		if(this.prefix){
			message = messagePrefix + color + " " + message;
		}else{
			message = color + message;
		}
		player.sendMessage(message);
	}
	
}
