package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.util.StringPlaceholders;
import dev.esophose.playerparticles.util.Updater;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PluginUpdateManager extends Manager implements Listener {

    private String updateVersion;

    public PluginUpdateManager(PlayerParticles playerParticles) {
        super(playerParticles);

        Bukkit.getPluginManager().registerEvents(this, this.playerParticles);
    }

    @Override
    public void reload() {
        if (Setting.CHECK_UPDATES.getBoolean()) {
            Bukkit.getScheduler().runTaskAsynchronously(this.playerParticles, () -> {
                try { // This can throw an exception if the server has no internet connection or if the Curse API is down
                    Updater updater = new Updater(this.playerParticles, 82823, this.playerParticles.getJarFile(), Updater.UpdateType.NO_DOWNLOAD, false);
                    if (Double.parseDouble(updater.getLatestName().replaceAll("PlayerParticles v", "")) > Double.parseDouble(this.playerParticles.getDescription().getVersion())) {
                        this.updateVersion = updater.getLatestName().replaceAll("PlayerParticles v", "");
                        this.playerParticles.getLogger().info("An update (v" + this.updateVersion + ") is available! You are running v" + this.playerParticles.getDescription().getVersion());
                    }
                } catch (Exception e) {
                    this.playerParticles.getLogger().warning("An error occurred checking for an update. There is either no established internet connection or the Curse API is down.");
                }
            });
        }
    }

    @Override
    public void disable() {

    }

    /**
     * Called when a player joins and notifies ops if an update is available
     *
     * @param e The join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (e.getPlayer().isOp() && this.updateVersion != null) {
            PlayerParticles.getInstance().getManager(LocaleManager.class).sendMessage(
                    e.getPlayer(),
                    "update-available",
                    StringPlaceholders.builder("new", this.updateVersion).addPlaceholder("current", this.playerParticles.getDescription().getVersion()).build());
        }
    }

}
