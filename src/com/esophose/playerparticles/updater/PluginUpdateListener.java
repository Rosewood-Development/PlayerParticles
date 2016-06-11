/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.updater;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.MessageManager;

public class PluginUpdateListener implements Listener {

	/**
	 * Called when a player joins and notifies ops if an update is available
	 * 
	 * @param e The event
	 */
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		if(e.getPlayer().isOp()) {
			if(PlayerParticles.updateVersion != null) {
				MessageManager.getInstance().sendMessage(e.getPlayer(), "An update (" + ChatColor.AQUA + "v" + PlayerParticles.updateVersion + ChatColor.YELLOW + ") is available! You are running " + ChatColor.AQUA + "v" + PlayerParticles.getPlugin().getDescription().getVersion(), ChatColor.YELLOW);
				MessageManager.getInstance().sendMessage(e.getPlayer(), "Download from: http://dev.bukkit.org/bukkit-plugins/playerparticles/", ChatColor.YELLOW);
			}
		}
	}
	
}
