package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Arrays;
import java.util.regex.Pattern;
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
                try {
                    String latestVersion = this.getLatestVersion();
                    String currentVersion = this.playerParticles.getDescription().getVersion();

                    if (this.isUpdateAvailable(latestVersion, currentVersion)) {
                        this.updateVersion = latestVersion;
                        this.playerParticles.getLogger().info("An update (v" + this.updateVersion + ") is available! You are running v" + currentVersion);
                    }
                } catch (Exception e) {
                    this.playerParticles.getLogger().warning("An error occurred checking for an update. There is either no established internet connection or the Spigot API is down.");
                }
            });
        }
    }

    @Override
    public void disable() {

    }

    /**
     * Checks if there is an update available
     *
     * @param latest The latest version of the plugin from Spigot
     * @param current The currently installed version of the plugin
     * @return true if available, otherwise false
     */
    private boolean isUpdateAvailable(String latest, String current) {
        // Break versions into individual numerical pieces separated by periods
        int[] latestSplit = Arrays.stream(latest.replaceAll("[^0-9.]", "").split(Pattern.quote("."))).mapToInt(Integer::parseInt).toArray();
        int[] currentSplit = Arrays.stream(current.replaceAll("[^0-9.]", "").split(Pattern.quote("."))).mapToInt(Integer::parseInt).toArray();

        // Make sure both arrays are the same length
        if (latestSplit.length > currentSplit.length) {
            currentSplit = Arrays.copyOf(currentSplit, latestSplit.length);
        } else if (currentSplit.length > latestSplit.length) {
            latestSplit = Arrays.copyOf(latestSplit, currentSplit.length);
        }

        // Compare pieces from most significant to least significant
        for (int i = 0; i < latestSplit.length; i++) {
            if (latestSplit[i] > currentSplit[i]) {
                return true;
            } else if (currentSplit[i] > latestSplit[i]) {
                break;
            }
        }
        
        return false;
    }

    /**
     * Gets the latest version of the plugin from the Spigot Web API
     *
     * @return the latest version of the plugin from Spigot
     * @throws IOException if a network error occurs
     */
    private String getLatestVersion() throws IOException {
        URL spigot = new URL("https://api.spigotmc.org/legacy/update.php?resource=40261");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(spigot.openStream()))) {
            return reader.readLine();
        }
    }

    /**
     * Called when a player joins and notifies ops if an update is available
     *
     * @param e The join event
     */
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (this.updateVersion != null && e.getPlayer().isOp()) {
            PlayerParticles.getInstance().getManager(LocaleManager.class).sendMessage(
                    e.getPlayer(),
                    "update-available",
                    StringPlaceholders.builder("new", this.updateVersion).addPlaceholder("current", this.playerParticles.getDescription().getVersion()).build());
        }
    }

}
