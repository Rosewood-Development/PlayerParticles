package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.projectiles.ProjectileSource;

public class ParticleStyleFishing extends DefaultParticleStyle implements Listener {

    // I hate legacy versions. The Spigot API changed the PlayerFishEvent#getHook method from returning a Fish to a FishHook in 1.13
    private static Method PlayerFishEvent_getHook;
    static {
        try {
            PlayerFishEvent_getHook = PlayerFishEvent.class.getDeclaredMethod("getHook");
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }

    private Set<Projectile> projectiles;

    protected ParticleStyleFishing() {
        super("fishing", false, false, 0);

        this.projectiles = new HashSet<>();

        // Removes all fish hooks that are considered dead
        Bukkit.getScheduler().runTaskTimer(PlayerParticles.getInstance(), () -> this.projectiles.removeIf(x -> !x.isValid()), 0L, 5L);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        List<Projectile> listCopy = new ArrayList<>(this.projectiles); // Copy in case of modification while looping due to async
        for (Projectile projectile : listCopy) {
            ProjectileSource shooter = projectile.getShooter();
            if (shooter instanceof Player && ((Player) shooter).getUniqueId().equals(particle.getOwnerUniqueId()))
                particles.add(new PParticle(projectile.getLocation(), 0.05F, 0.05F, 0.05F, 0.0F));
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
        return false;
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent event) {
        // Done through a string switch for 1.9.4 compatibility
        switch (event.getState().toString()) {
            case "FISHING":
                try {
                    this.projectiles.add((Projectile) PlayerFishEvent_getHook.invoke(event));
                } catch (ReflectiveOperationException ignored) { }
                break;
            case "CAUGHT_FISH":
            case "CAUGHT_ENTITY":
            case "REEL_IN":
                try {
                    this.projectiles.remove((Projectile) PlayerFishEvent_getHook.invoke(event));
                } catch (ReflectiveOperationException ignored) { }
                break;
        }
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {

    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {

    }

}
