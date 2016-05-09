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

	private static MessageManager instance = new MessageManager();
	private boolean messagesEnabled, prefix;
	private String messagePrefix;
	
	private MessageManager() {
		messagesEnabled = PlayerParticles.getPlugin().getConfig().getBoolean("messages-enabled");
		prefix = PlayerParticles.getPlugin().getConfig().getBoolean("use-message-prefix");
		messagePrefix = PlayerParticles.getPlugin().getConfig().getString("message-prefix");
		messagePrefix = messagePrefix.replace("&", "§");
	}
	
	public static MessageManager getInstance() {
		return instance;
	}
	
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
