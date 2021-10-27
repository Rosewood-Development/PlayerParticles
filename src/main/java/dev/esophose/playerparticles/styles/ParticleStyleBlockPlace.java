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
import org.bukkit.event.block.BlockPlaceEvent;

public class ParticleStyleBlockPlace extends DefaultParticleStyle implements Listener {

    private int particleAmount;
    private double particleSpread;
    private double particleSpeed;

    protected ParticleStyleBlockPlace() {
        super("blockplace", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();
        
        location.add(0.5, 0.5, 0.5); // Center around the block

        for (int i = 0; i < this.particleAmount; i++)
            particles.add(new PParticle(location, this.particleSpread, this.particleSpread, this.particleSpread, this.particleSpeed));

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("OAK_PLANKS", "WOOD");
    }

    @Override
    public boolean hasLongRangeVisibility() {
        return true;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("particle-amount", 10, "The number of particles to spawn");
        this.setIfNotExists("particle-spread", 0.75, "The distance to spread particles");
        this.setIfNotExists("particle-speed", 0.05, "The speed of the particles");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.particleAmount = config.getInt("particle-amount");
        this.particleSpread = config.getInt("particle-spread");
        this.particleSpeed = config.getDouble("particle-speed");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        Player player = event.getPlayer();
        PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
        if (pplayer == null)
            return;

        for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.BLOCKPLACE)) {
            Location loc = event.getBlock().getLocation();
            particleManager.displayParticles(pplayer, player.getWorld(), particle, DefaultStyles.BLOCKPLACE.getParticles(particle, loc), false);
        }
    }

}
