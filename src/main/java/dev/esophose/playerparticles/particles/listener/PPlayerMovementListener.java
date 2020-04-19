package dev.esophose.playerparticles.particles.listener;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public class PPlayerMovementListener implements Listener {
    
    private static final int CHECK_INTERVAL = 3;
    private Map<UUID, Integer> timeSinceLastMovement;
    private Map<UUID, Vector> previousVectors;
    
    public PPlayerMovementListener() {
        DataManager dataManager = PlayerParticles.getInstance().getManager(DataManager.class);
        this.timeSinceLastMovement = new HashMap<>();
        this.previousVectors = new HashMap<>();

        Bukkit.getScheduler().runTaskTimer(PlayerParticles.getInstance(), () -> {
            for (Player player : Bukkit.getOnlinePlayers()) {
                UUID playerUUID = player.getUniqueId();
                Vector previousVector = this.previousVectors.get(playerUUID);
                Location currentLocation = player.getLocation();
                Vector currentVector = new Vector(currentLocation.getBlockX(), currentLocation.getBlockY(), currentLocation.getBlockZ());
                this.previousVectors.put(playerUUID, currentVector);

                if (previousVector == null || !previousVector.equals(currentVector)) {
                    if (!this.timeSinceLastMovement.containsKey(playerUUID)) {
                        this.timeSinceLastMovement.put(playerUUID, 0);
                    } else {
                        this.timeSinceLastMovement.replace(playerUUID, 0);
                    }
                }
            }

            List<UUID> toRemove = new ArrayList<>();

            for (UUID uuid : this.timeSinceLastMovement.keySet()) {
                PPlayer pplayer = dataManager.getPPlayer(uuid);
                if (pplayer == null) {
                    toRemove.add(uuid);
                } else {
                    int standingTime = this.timeSinceLastMovement.get(uuid);
                    pplayer.setMoving(standingTime < Setting.TOGGLE_ON_MOVE_DELAY.getInt());
                    if (pplayer.isMoving())
                        this.timeSinceLastMovement.replace(uuid, standingTime + CHECK_INTERVAL);
                }
            }

            for (UUID uuid : toRemove)
                this.timeSinceLastMovement.remove(uuid);
        }, 0, CHECK_INTERVAL);
    }

}
