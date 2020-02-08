package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class ParticleStyleArrows extends DefaultParticleStyle implements Listener {

    private List<Projectile> arrows = new ArrayList<>();

    private int maxArrowsPerPlayer;
    private boolean onlySpawnIfFlying;
    private List<String> arrowEntityNames;

    public ParticleStyleArrows() {
        super("arrows", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        int count = 0;
        for (int i = this.arrows.size() - 1; i >= 0; i--) { // Loop backwards so the last-fired arrows are the ones that have particles if they go over the max
            Projectile arrow = this.arrows.get(i);
            if (this.onlySpawnIfFlying && arrow.isOnGround())
                continue;

            if (arrow.getShooter() != null && ((Player) arrow.getShooter()).getUniqueId().equals(particle.getOwnerUniqueId())) {
                particles.add(new PParticle(arrow.getLocation(), 0.05F, 0.05F, 0.05F, 0.0F));
                count++;
            }
            
            if (count >= this.maxArrowsPerPlayer)
                break;
        }

        return particles;
    }

    /**
     * Removes all arrows that are considered dead
     */
    @Override
    public void updateTimers() {
        for (int i = this.arrows.size() - 1; i >= 0; i--) {
            Projectile arrow = this.arrows.get(i);
            if (arrow.getTicksLived() >= 1200 || arrow.isDead() || !arrow.isValid() || arrow.getShooter() == null)
                this.arrows.remove(i);
        }
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
        if (this.arrowEntityNames.contains(entityName))
            this.arrows.add(event.getEntity());
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("max-arrows-per-player", 10, "The max number of arrows that will spawn particles per player");
        this.setIfNotExists("only-spawn-if-flying", false, "Only spawn particles while the arrow is still in the air");
        this.setIfNotExists("arrow-entities", Arrays.asList("ARROW", "SPECTRAL_ARROW", "TIPPED_ARROW"), "The name of the projectile entities that are counted as arrows");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.maxArrowsPerPlayer = config.getInt("max-arrows-per-player");
        this.onlySpawnIfFlying = config.getBoolean("only-spawn-if-flying");
        this.arrowEntityNames = config.getStringList("arrow-entities");
    }

}
