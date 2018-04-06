package com.esophose.playerparticles.styles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleSwords implements ParticleStyle, Listener {
    
    private static final List<String> SWORD_NAMES;
    
    static {
        SWORD_NAMES = new ArrayList<String>();
        SWORD_NAMES.addAll(Arrays.asList("WOOD_SWORD", "STONE_SWORD", "IRON_SWORD", "GOLD_SWORD", "DIAMOND_SWORD"));
    }

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
        return "swords";
    }

    public boolean canBeFixed() {
        return false;
    }
    
    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();
            PPlayer pplayer = ConfigManager.getInstance().getPPlayer(player.getUniqueId());
            if (pplayer != null && pplayer.getParticleStyle() == DefaultStyles.SWORDS && PermissionManager.hasStylePermission(player, DefaultStyles.SWORDS)) {
                if (player.getInventory().getItemInHand() != null && SWORD_NAMES.contains(player.getInventory().getItemInHand().getType().name())) {
                    Location loc = entity.getLocation().clone().add(0, 1, 0);
                    ParticleManager.displayParticles(pplayer, DefaultStyles.SWORDS.getParticles(pplayer, loc));
                }
            }
        }
    }

}