package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

public class ParticleStyleHurt extends DefaultParticleStyle implements Listener {

    private int thickMultiplier;

    protected ParticleStyleHurt() {
        super("hurt", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < this.thickMultiplier; i++)
            particles.addAll(DefaultStyles.THICK.getParticles(particle, location));

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("CACTUS");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
            if (pplayer != null) {
                for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.HURT)) {
                    Location loc = player.getLocation().add(0, 1, 0);
                    particleManager.displayParticles(pplayer, player.getWorld(), particle, DefaultStyles.HURT.getParticles(particle, loc), false);
                }
            }
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("thick-multiplier", 3, "How much to multiply the particles by", "This style uses the same spawning as the 'thick' style");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.thickMultiplier = config.getInt("thick-multiplier");
    }

}
