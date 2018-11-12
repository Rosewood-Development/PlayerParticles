package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBlockEdit implements ParticleStyle, Listener {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return new ArrayList<PParticle>(); // Particles are taken from DefaultStyles.BLOCKPLACE or DefaultStyles.BLOCKBREAK
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
        PPlayer pplayer = DataManager.getPPlayer(player.getUniqueId());
        if (pplayer != null) {
            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.BLOCKEDIT)) {
                Location loc = event.getBlock().getLocation();
                ParticleManager.displayParticles(particle, DefaultStyles.BLOCKBREAK.getParticles(particle, loc));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PPlayer pplayer = DataManager.getPPlayer(player.getUniqueId());
        if (pplayer != null) {
            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.BLOCKEDIT)) {
                Location loc = event.getBlock().getLocation().clone();
                ParticleManager.displayParticles(particle, DefaultStyles.BLOCKPLACE.getParticles(particle, loc));
            }
        }
    }

}
