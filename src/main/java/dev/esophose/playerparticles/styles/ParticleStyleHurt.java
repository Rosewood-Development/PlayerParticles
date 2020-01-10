package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
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
import org.bukkit.event.entity.EntityDamageEvent;

public class ParticleStyleHurt extends DefaultParticleStyle implements Listener {

    public ParticleStyleHurt() {
        super("hurt", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> baseParticles = DefaultStyles.THICK.getParticles(particle, location);

        int multiplyingFactor = 3; // Uses the same logic as ParticleStyleThick except multiplies the resulting particles by 3x
        List<PParticle> particles = new ArrayList<>();
        for (int i = 0; i < baseParticles.size() * multiplyingFactor; i++) {
            particles.add(baseParticles.get(i % baseParticles.size()));
        }

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onEntityDamage(EntityDamageEvent event) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
            if (pplayer != null) {
                for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.HURT)) {
                    Location loc = player.getLocation().clone().add(0, 1, 0);
                    particleManager.displayParticles(particle, DefaultStyles.HURT.getParticles(particle, loc));
                }
            }
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
