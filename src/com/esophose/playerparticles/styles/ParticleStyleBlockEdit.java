package com.esophose.playerparticles.styles;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.manager.PPlayerDataManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBlockEdit implements ParticleStyle, Listener {

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        return new PParticle[0]; // Particles are taken from DefaultStyles.BLOCKPLACE or DefaultStyles.BLOCKBREAK
    }

    public void updateTimers() {
        
    }

    public String getName() {
        return "blockedit";
    }

    public boolean canBeFixed() {
        return false;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PPlayer pplayer = PPlayerDataManager.getInstance().getPPlayer(player.getUniqueId());
        if (pplayer != null && pplayer.getParticleStyle() == DefaultStyles.BLOCKEDIT && PermissionManager.hasStylePermission(player, DefaultStyles.BLOCKEDIT)) {
            Location loc = event.getBlock().getLocation();
            ParticleManager.displayParticles(pplayer, DefaultStyles.BLOCKBREAK.getParticles(pplayer, loc));
        }
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PPlayer pplayer = PPlayerDataManager.getInstance().getPPlayer(player.getUniqueId());
        if (pplayer != null && pplayer.getParticleStyle() == DefaultStyles.BLOCKEDIT && PermissionManager.hasStylePermission(player, DefaultStyles.BLOCKEDIT)) {
            Location loc = event.getBlockPlaced().getLocation();
            ParticleManager.displayParticles(pplayer, DefaultStyles.BLOCKPLACE.getParticles(pplayer, loc));
        }
    }

}
