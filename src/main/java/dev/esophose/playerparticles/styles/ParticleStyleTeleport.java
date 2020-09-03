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
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class ParticleStyleTeleport extends DefaultParticleStyle implements Listener {

    private boolean before;
    private boolean after;

    private double amount;
    private double spread;
    private double speed;

    protected ParticleStyleTeleport() {
        super("teleport", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < this.amount; i++)
            particles.add(new PParticle(location, this.spread, this.spread, this.spread, this.speed));

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("ENDER_PEARL");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("before", true, "Spawn the particles at the teleporting position");
        this.setIfNotExists("after", true, "Spawn the particles after the teleport in the new position");
        this.setIfNotExists("amount", 25, "The number of particles to spawn");
        this.setIfNotExists("spread", 0.5, "How much to spread the particles");
        this.setIfNotExists("speed", 0.05, "If the particle supports speed, how much speed to apply");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.before = config.getBoolean("before");
        this.after = config.getBoolean("after");
        this.amount = config.getDouble("amount");
        this.spread = config.getDouble("spread");
        this.speed = config.getDouble("speed");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        TeleportCause cause = event.getCause();
        if (cause == TeleportCause.UNKNOWN)
            return;
        
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        Player player = event.getPlayer();
        PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
        if (pplayer == null)
            return;

        for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.TELEPORT)) {
            if (this.before) {
                Location loc1 = player.getLocation().clone();
                loc1.setY(loc1.getY() + 1);
                particleManager.displayParticles(pplayer, player.getWorld(), particle, DefaultStyles.TELEPORT.getParticles(particle, loc1), false);
            }

            if (this.after) {
                Bukkit.getScheduler().runTaskLater(PlayerParticles.getInstance(), () -> {
                    Location loc2 = player.getLocation().clone();
                    loc2.setY(loc2.getY() + 1);
                    particleManager.displayParticles(pplayer, player.getWorld(), particle, DefaultStyles.TELEPORT.getParticles(particle, loc2), false);
                }, 1);
            }
        }
    }

}
