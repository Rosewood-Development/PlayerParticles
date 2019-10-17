package dev.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.api.PParticle;
import dev.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleHurt implements ParticleStyle, Listener {

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> baseParticles = DefaultStyles.THICK.getParticles(particle, location);

        int multiplyingFactor = 3; // Uses the same logic as ParticleStyleThick except multiplies the resulting particles by 3x
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < baseParticles.size() * multiplyingFactor; i++) {
            particles.add(baseParticles.get(i % baseParticles.size()));
        }

        return particles;
    }

    public void updateTimers() {

    }

    public String getName() {
        return "hurt";
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
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PPlayer pplayer = DataManager.getPPlayer(player.getUniqueId());
            if (pplayer != null) {
                for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.HURT)) {
                    Location loc = player.getLocation().clone().add(0, 1, 0);
                    ParticleManager.displayParticles(particle, DefaultStyles.HURT.getParticles(particle, loc));
                }
            }
        }
    }

}
