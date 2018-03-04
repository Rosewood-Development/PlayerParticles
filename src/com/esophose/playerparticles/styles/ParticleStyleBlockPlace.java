package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBlockPlace implements ParticleStyle, Listener {

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();

        for (int i = 0; i < 15; i++) 
            particles.add(new PParticle(location.clone().add(0.5, 0.5, 0.5), 0.75F, 0.75F, 0.75F, 0.05F));

        return particles.toArray(new PParticle[particles.size()]);
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
        PPlayer pplayer = ConfigManager.getInstance().getPPlayer(player.getUniqueId());
        if (pplayer != null && pplayer.getParticleStyle() == DefaultStyles.BLOCKPLACE && PermissionManager.hasStylePermission(player, DefaultStyles.BLOCKPLACE)) {
            Location loc = event.getBlockPlaced().getLocation();
            ParticleManager.displayParticles(pplayer, DefaultStyles.BLOCKPLACE.getParticles(pplayer, loc));
        }
    }

}
