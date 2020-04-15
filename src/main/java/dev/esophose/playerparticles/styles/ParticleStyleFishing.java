package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.FishHook;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerFishEvent.State;

public class ParticleStyleFishing extends DefaultParticleStyle implements Listener {

    private List<FishHook> projectiles;

    public ParticleStyleFishing() {
        super("fishing", false, false, 0);

        this.projectiles = Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        List<FishHook> listCopy = new ArrayList<>(this.projectiles); // Copy in case of modification while looping due to async
        for (FishHook fishHook : listCopy)
            particles.add(new PParticle(fishHook.getLocation(), 0.05F, 0.05F, 0.05F, 0.0F));

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
        switch (event.getState()) {
            case FISHING:
                this.projectiles.add(event.getHook());
                break;
            case CAUGHT_FISH:
            case REEL_IN:
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
