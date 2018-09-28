package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBlockPlace implements ParticleStyle, Listener {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();

        for (int i = 0; i < 15; i++)
            particles.add(new PParticle(location.clone().add(0.5, 0.5, 0.5), 0.75F, 0.75F, 0.75F, 0.05F));

        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "blockplace";
    }

    public boolean canBeFixed() {
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        PPlayer pplayer = DataManager.getPPlayer(player.getUniqueId());
        if (pplayer != null) {
            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.BLOCKPLACE)) {
                Location loc = event.getBlock().getLocation();
                ParticleManager.displayParticles(particle, DefaultStyles.BLOCKPLACE.getParticles(particle, loc));
            }
        }
    }

}
