package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParticleStyleMove extends DefaultParticleStyle implements Listener {

    public ParticleStyleMove() {
        super("move", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return DefaultStyles.NORMAL.getParticles(particle, location);
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent e) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(e.getPlayer().getUniqueId());
        if (pplayer != null) {
            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.MOVE)) {
                Location loc = e.getPlayer().getLocation().clone();
                loc.setY(loc.getY() + 0.05);
                particleManager.displayParticles(particle, DefaultStyles.MOVE.getParticles(particle, loc));
            }
        }
    }

}
