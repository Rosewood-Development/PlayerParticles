package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.ConfigurationManager;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ParticleStyleDeath extends DefaultParticleStyle implements Listener {

    private String style;
    private List<EntityDamageEvent.DamageCause> causes;
    private int targetDuration;
    private final long ticksPerParticle = ConfigurationManager.Setting.TICKS_PER_PARTICLE.getLong();

    protected ParticleStyleDeath() {
        super("death", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        ParticleStyle style = ParticleStyle.fromName(this.style);
        if (style == null || style == this)
            style = DefaultStyles.WHIRL;

        return style.getParticles(particle, location);
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Arrays.asList("TOTEM_OF_UNDYING", "TOTEM", "BED");
    }

    @Override
    public boolean hasLongRangeVisibility() {
        return true;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("style", "whirl", "The name of the style to be displayed");
        this.setIfNotExists("target-duration", 60, "How long to display the particles for");
        this.setIfNotExists("disabled-causes", Collections.singletonList("DROWNING"), "What damage types shouldn't spawn particles?");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.style = config.getString("style");
        this.targetDuration = config.getInt("target-duration");
        this.causes = config.getStringList("disabled-causes").stream()
                .map(s -> EntityDamageEvent.DamageCause.valueOf(s.toUpperCase()))
                .collect(Collectors.toList());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDeath(PlayerDeathEvent event) {
        EntityDamageEvent damageEvent = event.getEntity().getLastDamageCause();
        if (damageEvent != null && this.causes.contains(damageEvent.getCause()))
            return;

        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);
        PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(event.getEntity().getUniqueId());
        if (pplayer == null)
            return;

        Location location = event.getEntity().getLocation().add(0, 1, 0);
        new BukkitRunnable() {
            private int totalDuration = 0;

            @Override
            public void run() {
                for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.DEATH))
                    particleManager.displayParticles(pplayer, event.getEntity().getWorld(), particle, DefaultStyles.DEATH.getParticles(particle, location), false);

                this.totalDuration += ParticleStyleDeath.this.ticksPerParticle;
                if (this.totalDuration > ParticleStyleDeath.this.targetDuration)
                    this.cancel();
            }
        }.runTaskTimer(PlayerParticles.getInstance(), 0, this.ticksPerParticle);
    }

}
