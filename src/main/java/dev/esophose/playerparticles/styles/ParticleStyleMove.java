package dev.esophose.playerparticles.styles;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.api.PParticle;
import dev.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleMove implements ParticleStyle, Listener {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return DefaultStyles.NORMAL.getParticles(particle, location);
    }

    public void updateTimers() {

    }

    public String getName() {
        return "move";
    }

    public boolean canBeFixed() {
        return false;
    }
    
    public boolean canToggleWithMovement() {
        return false;
    }
    
    public double getFixedEffectOffset() {
        return 0;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        PPlayer pplayer = DataManager.getPPlayer(e.getPlayer().getUniqueId());
        if (pplayer != null) {
            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.MOVE)) {
                Location loc = e.getPlayer().getLocation().clone();
                loc.setY(loc.getY() + 0.05);
                ParticleManager.displayParticles(particle, DefaultStyles.MOVE.getParticles(particle, loc));
            }
        }
    }

}
