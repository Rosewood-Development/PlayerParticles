package com.esophose.playerparticles.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.SettingManager.PSetting;

public class PPlayerMovementListener implements Listener {
    
    private static final int CHECK_INTERVAL = 3;
    private Map<UUID, Integer> timeSinceLastMovement = new HashMap<UUID, Integer>();
    
    public PPlayerMovementListener() {
        new BukkitRunnable() {
            public void run() {
                if (!PSetting.TOGGLE_ON_MOVE.getBoolean()) return;
                
                List<UUID> toRemove = new ArrayList<UUID>();
                
                for (UUID uuid : timeSinceLastMovement.keySet()) {
                    PPlayer pplayer = DataManager.getPPlayer(uuid);
                    if (pplayer == null) {
                        toRemove.add(uuid);
                    } else {
                        int standingTime = timeSinceLastMovement.get(uuid);
                        pplayer.setMoving(standingTime < PSetting.TOGGLE_ON_MOVE_DELAY.getInt());
                        if (standingTime < PSetting.TOGGLE_ON_MOVE_DELAY.getInt())
                            timeSinceLastMovement.replace(uuid, standingTime + CHECK_INTERVAL);
                    }
                }
                
                for (UUID uuid : toRemove)
                    timeSinceLastMovement.remove(uuid);
            }
        }.runTaskTimer(PlayerParticles.getPlugin(), 0, CHECK_INTERVAL);
    }

    /**
     * Used to detect if the player is moving
     * 
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!PSetting.TOGGLE_ON_MOVE.getBoolean()) return;
        if (event.getTo().getBlockX() == event.getFrom().getBlockX() && event.getTo().getBlockY() == event.getFrom().getBlockY() && event.getTo().getBlockZ() == event.getFrom().getBlockZ()) return;
        
        UUID playerUUID = event.getPlayer().getUniqueId();
        if (!timeSinceLastMovement.containsKey(playerUUID)) {
            timeSinceLastMovement.put(playerUUID, 0);
        } else {
            timeSinceLastMovement.replace(playerUUID, 0);
        }
    }

}
