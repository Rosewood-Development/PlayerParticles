package dev.esophose.playerparticles.styles;

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

import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.api.PParticle;
import dev.esophose.playerparticles.styles.api.ParticleStyle;

public class ParticleStyleSwords implements ParticleStyle, Listener {

    private static final List<String> SWORD_NAMES;

    static {
        SWORD_NAMES = new ArrayList<String>();
        SWORD_NAMES.addAll(Arrays.asList("WOOD_SWORD", "STONE_SWORD", "IRON_SWORD", "GOLD_SWORD", "GOLDEN_SWORD", "DIAMOND_SWORD", "TRIDENT"));
    }

    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> baseParticles = DefaultStyles.NORMAL.getParticles(particle, location);

        int multiplyingFactor = 15; // Uses the same logic as ParticleStyleNormal except multiplies the resulting particles by 3x
        List<PParticle> particles = new ArrayList<PParticle>();
        for (int i = 0; i < baseParticles.size() * multiplyingFactor; i++) {
            particles.add(baseParticles.get(i % baseParticles.size()));
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
    
    public boolean canToggleWithMovement() {
        return false;
    }
    
    public double getFixedEffectOffset() {
        return 0;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();
            PPlayer pplayer = DataManager.getPPlayer(player.getUniqueId());
            if (pplayer != null) {
                for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.SWORDS)) {
                    Location loc = entity.getLocation().clone().add(0, 1, 0);
                    ParticleManager.displayParticles(particle, DefaultStyles.SWORDS.getParticles(particle, loc));
                }
            }
        }
    }

}
