package com.esophose.playerparticles.styles;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleHurt implements ParticleStyle, Listener {

    public PParticle[] getParticles(PPlayer pplayer, Location location) {
        PParticle[] baseParticles = DefaultStyles.THICK.getParticles(pplayer, location);

        int multiplyingFactor = 3; // Uses the same logic as ParticleStyleThick except multiplies the resulting particles by 3x
        PParticle[] particles = new PParticle[baseParticles.length * multiplyingFactor];
        for (int i = 0; i < baseParticles.length * multiplyingFactor; i++) {
            particles[i] = baseParticles[i % baseParticles.length];
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
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PPlayer pplayer = ConfigManager.getInstance().getPPlayer(player.getUniqueId());
            if (pplayer != null && pplayer.getParticleStyle() == DefaultStyles.HURT && PermissionManager.hasStylePermission(player, DefaultStyles.HURT)) {
                Location loc = player.getLocation().clone().add(0, 1, 0);
                ParticleManager.displayParticles(pplayer, DefaultStyles.HURT.getParticles(pplayer, loc));
            }
        }
    }

}