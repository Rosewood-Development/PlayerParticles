package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
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

public class ParticleStyleSwords extends DefaultParticleStyle implements Listener {

    private static final List<String> SWORD_NAMES;

    private int multiplier;

    static {
        SWORD_NAMES = new ArrayList<>();
        SWORD_NAMES.addAll(Arrays.asList("WOOD_SWORD", "STONE_SWORD", "IRON_SWORD", "GOLD_SWORD", "GOLDEN_SWORD", "DIAMOND_SWORD", "TRIDENT"));
    }

    public ParticleStyleSwords() {
        super("swords", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < this.multiplier; i++)
            particles.addAll(DefaultStyles.NORMAL.getParticles(particle, location));

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("multiplier", 15, "The multiplier for the number of particles to spawn", "This style uses the same spawning as the 'normal' style");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.multiplier = config.getInt("multiplier");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();
            PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
            if (pplayer != null && SWORD_NAMES.contains(player.getInventory().getItemInMainHand().getType().name())) {
                for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.SWORDS)) {
                    Location loc = entity.getLocation().clone().add(0, 1, 0);
                    particleManager.displayParticles(player, player.getWorld(), particle, DefaultStyles.SWORDS.getParticles(particle, loc));
                }
            }
        }
    }

}
