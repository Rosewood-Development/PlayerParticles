package com.esophose.playerparticles.updater;

import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.LangManager;

public class PluginUpdateListener implements Listener {

    /**
     * Called when a player joins and notifies ops if an update is available
     * 
     * @param e The join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().isOp()) {
            if (PlayerParticles.updateVersion != null) { // @formatter:off
                LangManager.sendCustomMessage(e.getPlayer(), ChatColor.YELLOW + "An update (" + ChatColor.AQUA + "v" + PlayerParticles.updateVersion + ChatColor.YELLOW + ") is available! " + 
                                                                                "You are running " + ChatColor.AQUA + "v" + PlayerParticles.getPlugin().getDescription().getVersion() + ChatColor.YELLOW + 
                                                                                ". https://dev.bukkit.org/projects/playerparticles");
			} // @formatter:on
        }
    }

}
