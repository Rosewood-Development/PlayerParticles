package dev.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.api.PParticle;
import dev.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleBlockBreak implements ParticleStyle, Listener {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<PParticle>();
        
        location.add(0.5, 0.5, 0.5); // Center around the block

        for (int i = 0; i < 10; i++)
            particles.add(new PParticle(location, 0.5F, 0.5F, 0.5F, 0.05F));

        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "blockbreak";
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
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        PPlayer pplayer = DataManager.getPPlayer(player.getUniqueId());
        if (pplayer != null) {
            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.BLOCKBREAK)) {
                Location loc = event.getBlock().getLocation().clone();
                ParticleManager.displayParticles(particle, DefaultStyles.BLOCKBREAK.getParticles(particle, loc));
            }
        }
    }

}
