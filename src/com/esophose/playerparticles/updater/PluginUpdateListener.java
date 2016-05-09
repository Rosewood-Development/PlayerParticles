package com.esophose.playerparticles.updater;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.esophose.playerparticles.MessageManager;
import com.esophose.playerparticles.PlayerParticles;

public class PluginUpdateListener implements Listener {

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
