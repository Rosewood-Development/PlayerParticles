package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ParticleStyleArrows extends DefaultParticleStyle implements Listener {

    private List<Projectile> projectiles;

    private int maxArrowsPerPlayer;
    private boolean onlySpawnIfFlying;
    private List<String> projectileEntityNames;
    private int arrowTrackingTime;

    protected ParticleStyleArrows() {
        super("arrows", false, false, 0);

        this.projectiles = new ArrayList<>();

        // Removes all arrows that are considered dead
        Bukkit.getScheduler().runTaskTimer(PlayerParticles.getInstance(), () -> {
            for (int i = this.projectiles.size() - 1; i >= 0; i--) {
                Projectile projectile = this.projectiles.get(i);
                if ((this.arrowTrackingTime != -1 && projectile.getTicksLived() >= this.arrowTrackingTime) || !projectile.isValid() || projectile.getShooter() == null)
                    this.projectiles.remove(i);
            }
        }, 0L, 5L);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        int count = 0;
        List<Projectile> listCopy = new ArrayList<>(this.projectiles); // Copy in case of modification while looping due to async
        for (int i = listCopy.size() - 1; i >= 0; i--) { // Loop backwards so the last-fired projectiles are the ones that have particles if they go over the max
            Projectile projectile = listCopy.get(i);
            if (this.onlySpawnIfFlying && projectile.isOnGround())
                continue;

            if (projectile.getShooter() != null && ((Player) projectile.getShooter()).getUniqueId().equals(particle.getOwnerUniqueId())) {
                particles.add(new PParticle(projectile.getLocation(), 0.05F, 0.05F, 0.05F, 0.0F));
                count++;
            }
            
            if (count >= this.maxArrowsPerPlayer)
                break;
        }

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("BOW");
    }

    @Override
    public boolean hasLongRangeVisibility() {
        return true;
    }

    /**
     * The event used to get all projectiles fired by players
     * Adds all projectiles fired from players to the array
     *
     * @param event The ProjectileLaunchEvent
     */
    @EventHandler
    public void onProjectileLaunch(ProjectileLaunchEvent event) {
        if (!(event.getEntity().getShooter() instanceof Player))
            return;

        String entityName = event.getEntity().getType().name();
        if (this.projectileEntityNames.contains(entityName))
            this.projectiles.add(event.getEntity());
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("max-arrows-per-player", 10, "The max number of arrows that will spawn particles per player");
        this.setIfNotExists("only-spawn-if-flying", false, "Only spawn particles while the arrow is still in the air");
        this.setIfNotExists("arrow-entities", Arrays.asList("ARROW", "SPECTRAL_ARROW", "TIPPED_ARROW"), "The name of the projectile entities that are counted as arrows");
        this.setIfNotExists("arrow-tracking-time", 1200, "The maximum number of ticks to track an arrow for", "Set to -1 to disable (not recommended)");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.maxArrowsPerPlayer = config.getInt("max-arrows-per-player");
        this.onlySpawnIfFlying = config.getBoolean("only-spawn-if-flying");
        this.projectileEntityNames = config.getStringList("arrow-entities");
        this.arrowTrackingTime = config.getInt("arrow-tracking-time");
    }

}
