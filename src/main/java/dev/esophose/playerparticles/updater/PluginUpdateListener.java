package dev.esophose.playerparticles.updater;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginUpdateListener implements Listener {

    /**
     * Called when a player joins and notifies ops if an update is available
     *
     * @param e The join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().isOp() && PlayerParticles.getPlugin().getUpdateVersion() != null) {
            LangManager.sendCommandSenderMessage(
                    e.getPlayer(),
                    Lang.UPDATE_AVAILABLE,
                    PlayerParticles.getPlugin().getUpdateVersion(),
                    PlayerParticles.getPlugin().getDescription().getVersion());
        }
    }

}
