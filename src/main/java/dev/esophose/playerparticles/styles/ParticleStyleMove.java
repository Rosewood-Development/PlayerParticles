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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class ParticleStyleMove extends DefaultParticleStyle implements Listener {

    private final ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);
    private final DataManager dataManager = PlayerParticles.getInstance().getManager(DataManager.class);

    private int multiplier;

    protected ParticleStyleMove() {
        super("move", false, false, 0);
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
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("PISTON", "PISTON_BASE");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("multiplier", 1, "The multiplier for the number of particles to spawn", "This style uses the same spawning as the 'normal' style");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.multiplier = config.getInt("multiplier");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null || (from.getX() == to.getX() && from.getY() == to.getY() && from.getZ() == to.getZ()))
            return;
        
        Player player = event.getPlayer();
        PPlayer pplayer = this.dataManager.getPPlayer(player.getUniqueId());
        if (pplayer == null)
            return;

        for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.MOVE)) {
            Location loc = player.getLocation();
            loc.setY(loc.getY() + 0.05);
            this.particleManager.displayParticles(pplayer, player.getWorld(), particle, DefaultStyles.MOVE.getParticles(particle, loc), false);
        }
    }

}
