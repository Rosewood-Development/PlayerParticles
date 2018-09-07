package com.esophose.playerparticles.styles;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.manager.PPlayerDataManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleMove implements ParticleStyle, Listener {
    
    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        return DefaultStyles.NONE.getParticles(pplayer, location);
    }

    public void updateTimers() {
        
    }

    public String getName() {
        return "move";
    }

    public boolean canBeFixed() {
        return false;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        PPlayer pplayer = PPlayerDataManager.getInstance().getPPlayer(e.getPlayer().getUniqueId());
        if (pplayer != null && pplayer.getParticleStyle() == DefaultStyles.MOVE) {
            if (PermissionManager.hasStylePermission(e.getPlayer(), DefaultStyles.MOVE)) {
                Location loc = e.getPlayer().getLocation();
                loc.setY(loc.getY() + 0.05);
                ParticleManager.displayParticles(pplayer, DefaultStyles.MOVE.getParticles(pplayer, loc));
            }
        }
    }

}
