package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

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

    private List<Projectile> projectiles;

    public ParticleStyleFishing() {
        super("fishing", false, false, 0);

        this.projectiles = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        List<Projectile> listCopy = new ArrayList<>(this.projectiles); // Copy in case of modification while looping due to async
        for (Projectile projectile : listCopy)
            particles.add(new PParticle(projectile.getLocation(), 0.05F, 0.05F, 0.05F, 0.0F));

        return particles;
    }

    /**
     * Removes all fish hooks that are considered dead
     */
    @Override
    public void updateTimers() {
        this.projectiles.removeIf(x -> !x.isValid());
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
                this.projectiles.remove(event.getHook());
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
