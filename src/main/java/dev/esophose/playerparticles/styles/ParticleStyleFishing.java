package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.rosewood.rosegarden.config.CommentedFileConfiguration;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class ParticleStyleFishing extends ConfiguredParticleStyle implements Listener {

    // I hate legacy versions. The Spigot API changed the PlayerFishEvent#getHook method from returning a Fish to a FishHook in 1.13
    private static Method PlayerFishEvent_getHook;
    static {
        try {
            PlayerFishEvent_getHook = PlayerFishEvent.class.getDeclaredMethod("getHook");
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    private final Deque<LaunchedFishingHook> projectiles;

    protected ParticleStyleFishing() {
        super("fishing", false, false, 0);

        this.projectiles = new ConcurrentLinkedDeque<>();

        // Removes all fish hooks that are considered dead
        PlayerParticles.getInstance().getScheduler().runTaskTimer(() -> {
            this.projectiles.removeIf(x -> !x.getProjectile().isValid());
        }, 0L, 5L);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        List<LaunchedFishingHook> listCopy = new ArrayList<>(this.projectiles); // Copy in case of modification while looping due to async
        for (LaunchedFishingHook projectile : listCopy) {
            UUID shooter = projectile.getShooter();
            if (shooter.equals(particle.getOwnerUniqueId()))
                particles.add(PParticle.builder(projectile.getProjectile().getLocation()).offsets(0.05F, 0.05F, 0.05F).build());
        }

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("FISHING_ROD");
    }

    @Override
    public boolean hasLongRangeVisibility() {
        return true;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        Projectile projectile = null;
        try {
            projectile = (Projectile) PlayerFishEvent_getHook.invoke(event);
        } catch (ReflectiveOperationException ignored) { }

        if (projectile == null || !(projectile.getShooter() instanceof Player))
            return;

        UUID shooter = ((Player) projectile.getShooter()).getUniqueId();

        // Done through a string switch for 1.9.4 compatibility
        switch (event.getState().toString()) {
            case "FISHING":
                this.projectiles.add(new LaunchedFishingHook(projectile, shooter));
                break;
            case "CAUGHT_FISH":
            case "CAUGHT_ENTITY":
            case "REEL_IN":
                this.projectiles.removeIf(x -> x.getShooter().equals(shooter));
                break;
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

    private static class LaunchedFishingHook {

        private final Projectile projectile;
        private final UUID shooter;

        public LaunchedFishingHook(Projectile projectile, UUID shooter) {
            this.projectile = projectile;
            this.shooter = shooter;
        }

        public Projectile getProjectile() {
            return this.projectile;
        }

        public UUID getShooter() {
            return this.shooter;
        }

    }

}
