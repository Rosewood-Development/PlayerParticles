package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class ParticleStyleBlockPlace implements ParticleStyle, Listener {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        
        location.add(0.5, 0.5, 0.5); // Center around the block

        for (int i = 0; i < 10; i++)
            particles.add(new PParticle(location, 0.75F, 0.75F, 0.75F, 0.05F));

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
    
    public boolean canToggleWithMovement() {
        return false;
    }
    
    public double getFixedEffectOffset() {
        return 0;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        Player player = event.getPlayer();
        PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
        if (pplayer != null) {
            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.BLOCKPLACE)) {
                Location loc = event.getBlock().getLocation().clone();
                particleManager.displayParticles(particle, DefaultStyles.BLOCKPLACE.getParticles(particle, loc));
            }
        }
    }

}
