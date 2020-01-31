package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;

public class ParticleStyleArrows extends DefaultParticleStyle implements Listener {

    private static final String[] arrowEntityNames = new String[] { "ARROW", "SPECTRAL_ARROW", "TIPPED_ARROW" };
    private List<Projectile> arrows = new ArrayList<>();

    private int maxArrowsPerPlayer;
    private boolean onlySpawnIfFlying;

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
     * The event used to get all arrows fired by players
     * Adds all arrows fired from players to the array
     * 
     * @param e The EntityShootBowEvent
     */
    @EventHandler
    public void onArrowFired(EntityShootBowEvent e) {
        if (e.getEntityType() != EntityType.PLAYER)
            return;

        String entityName = e.getProjectile().getType().toString();
        if (Arrays.stream(arrowEntityNames).anyMatch(entityName::equalsIgnoreCase))
            this.arrows.add((Projectile) e.getProjectile());
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("max-arrows-per-player", 10, "The max number of arrows that will spawn particles per player");
        this.setIfNotExists("only-spawn-if-flying", false, "Only spawn particles while the arrow is still in the air");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.maxArrowsPerPlayer = config.getInt("max-arrows-per-player");
        this.onlySpawnIfFlying = config.getBoolean("only-spawn-if-flying");
    }

}
