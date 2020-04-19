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
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PPlayerCombatListener implements Listener {

    private static final int CHECK_INTERVAL = 20;
    private Map<UUID, Integer> timeSinceCombat = new HashMap<>();

    public PPlayerCombatListener() {
        DataManager dataManager = PlayerParticles.getInstance().getManager(DataManager.class);

        Bukkit.getScheduler().runTaskTimer(PlayerParticles.getInstance(), () -> {
            if (!Setting.TOGGLE_ON_COMBAT.getBoolean())
                return;

            List<UUID> toRemove = new ArrayList<>();

            for (UUID uuid : this.timeSinceCombat.keySet()) {
                PPlayer pplayer = dataManager.getPPlayer(uuid);
                if (pplayer == null) {
                    toRemove.add(uuid);
                } else {
                    int idleTime = this.timeSinceCombat.get(uuid);
                    pplayer.setInCombat(idleTime < Setting.TOGGLE_ON_COMBAT_DELAY.getInt());
                    if (pplayer.isInCombat())
                        this.timeSinceCombat.replace(uuid, idleTime + 1);
                }
            }

            for (UUID uuid : toRemove)
                this.timeSinceCombat.remove(uuid);
        }, 0, CHECK_INTERVAL);
    }

    /**
     * Used to detect if the player is moving
     *
     * @param event The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerAttack(EntityDamageByEntityEvent event) {
        if (event.getEntity().getType() != EntityType.PLAYER)
            return;

        Player attacker;
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (!(projectile.getShooter() instanceof Player))
                return;

            attacker = (Player) projectile.getShooter();
        } else if (event.getDamager() instanceof Player) {
            attacker = (Player) event.getDamager();
        } else return;

        Player damaged = (Player) event.getEntity();

        this.markInCombat(attacker);
        this.markInCombat(damaged);
    }

    /**
     * Marks the player as in combat
     *
     * @param player The player to mark
     */
    private void markInCombat(Player player) {
        UUID playerUUID = player.getUniqueId();
        if (!this.timeSinceCombat.containsKey(playerUUID)) {
            this.timeSinceCombat.put(playerUUID, 0);
        } else {
            this.timeSinceCombat.replace(playerUUID, 0);
        }
    }

}
