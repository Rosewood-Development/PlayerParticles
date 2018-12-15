package com.esophose.playerparticles.updater;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;

public class PluginUpdateListener implements Listener {

    /**
     * Called when a player joins and notifies ops if an update is available
     * 
     * @param e The join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().isOp() && PlayerParticles.updateVersion != null) {
            LangManager.sendCommandSenderMessage(e.getPlayer(), Lang.UPDATE_AVAILABLE, PlayerParticles.updateVersion, PlayerParticles.getPlugin().getDescription().getVersion());
        }
    }

}
