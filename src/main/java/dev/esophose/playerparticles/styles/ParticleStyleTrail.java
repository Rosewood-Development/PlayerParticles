package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParticleStyleTrail extends DefaultParticleStyle implements Listener {

    private double offset;
    private double spread;
    private double speed;

    protected ParticleStyleTrail() {
        super("trail", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        return Collections.singletonList(new PParticle(location.clone().add(0.0, this.offset, 0.0), this.spread, this.spread, this.spread, this.speed));
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("GHAST_TEAR");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("player-offset", 0.0, "How far to offset the player location vertically");
        this.setIfNotExists("spread", 0.1, "How much to spread the particles");
        this.setIfNotExists("speed", 0.01, "If the particle supports speed, how much speed to apply");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.offset = config.getDouble("player-offset");
        this.spread = config.getDouble("spread");
        this.speed = config.getDouble("speed");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        Player player = event.getPlayer();
        PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
        if (pplayer == null)
            return;

        for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.TRAIL)) {
            Location loc = player.getLocation().clone();
            loc.setY(loc.getY() + 1);
            particleManager.displayParticles(pplayer, player.getWorld(), particle, DefaultStyles.TRAIL.getParticles(particle, loc), false);
        }
    }

}
