package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import dev.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.DefaultStyles;
import java.awt.Color;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitTask;

public class ParticleManager extends Manager implements Listener, Runnable {

    /**
     * The map containing all the loaded PPlayer info
     */
    private Map<UUID, PPlayer> particlePlayers = new HashMap<>();

    /**
     * The task that spawns the particles
     */
    private BukkitTask particleTask = null;

    /**
     * Rainbow particle effect hue and note color used for rainbow colorable effects
     */
    private int hue = 0;
    private int note = 0;
    private final Random random = new Random();

    public ParticleManager(PlayerParticles playerParticles) {
        super(playerParticles);

        Bukkit.getPluginManager().registerEvents(this, this.playerParticles);
    }

    @Override
    public void reload() {
        if (this.particleTask != null)
            this.particleTask.cancel();

        Bukkit.getScheduler().runTaskLater(this.playerParticles, () -> {
            long ticks = Setting.TICKS_PER_PARTICLE.getLong();
            this.particleTask = Bukkit.getScheduler().runTaskTimer(this.playerParticles, this, 5, ticks);
        }, 1);

        this.particlePlayers.clear();
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        dataManager.loadFixedEffects();
        for (Player player : Bukkit.getOnlinePlayers())
            dataManager.getPPlayer(player.getUniqueId(), (pplayer) -> { }); // Loads the PPlayer from the database
    }

    @Override
    public void disable() {
        if (this.particleTask != null)
            this.particleTask.cancel();
    }

    /**
     * Adds the player to the array when they join
     * 
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        this.playerParticles.getManager(DataManager.class).getPPlayer(e.getPlayer().getUniqueId(), (pplayer) -> { }); // Loads the PPlayer from the database
    }

    /**
     * Removes the player from the array when they log off
     * 
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        PPlayer pplayer = this.playerParticles.getManager(DataManager.class).getPPlayer(e.getPlayer().getUniqueId());
        if (pplayer != null && pplayer.getFixedEffectIds().isEmpty())
            this.particlePlayers.remove(pplayer.getUniqueId()); // Unload the PPlayer if they don't have any fixed effects
    }

    /**
     * Gets the PPlayers that are loaded
     * 
     * @return The loaded PPlayers
     */
    public Collection<PPlayer> getPPlayers() {
        return this.particlePlayers.values();
    }

    /**
     * The main loop to display all the particles
     * Does not display particles if the world is disabled or if the player is in spectator mode
     */
    public void run() {
        this.playerParticles.getManager(ParticleStyleManager.class).updateTimers();

        this.hue += Setting.RAINBOW_CYCLE_SPEED.getInt();
        this.hue %= 360;

        if (this.hue % 4 == 0) { // Only increment note by 5 notes per second
            this.note++;
            this.note %= 25;
        }

        PermissionManager permissionManager = this.playerParticles.getManager(PermissionManager.class);

        // Spawn particles for each player
        for (PPlayer pplayer : this.particlePlayers.values()) {
            Player player = pplayer.getPlayer();

            // Don't show their particles if they are in spectator mode
            // Don't spawn particles if the world doesn't allow it
            if (player != null && player.getGameMode() != GameMode.SPECTATOR && permissionManager.isWorldEnabled(player.getWorld().getName()))
                for (ParticlePair particles : pplayer.getActiveParticles())
                    this.displayParticles(pplayer, particles, player.getLocation().clone().add(0, 1, 0));
            
            // Loop for FixedParticleEffects
            // Don't spawn particles if the world doesn't allow it
            for (FixedParticleEffect effect : pplayer.getFixedParticles())
                if (effect.getLocation().getWorld() != null && permissionManager.isWorldEnabled(effect.getLocation().getWorld().getName()))
                    this.displayFixedParticleEffect(effect);
        }
    }

    /**
     * Displays particles at the given player location with their settings
     * 
     * @param pplayer The PPlayer to spawn the particles for
     * @param particle The ParticlePair to use for getting particle settings
     * @param location The location to display at
     */
    private void displayParticles(PPlayer pplayer, ParticlePair particle, Location location) {
        if (!this.playerParticles.getManager(ParticleStyleManager.class).isEventHandled(particle.getStyle())) {
            if (Setting.TOGGLE_ON_MOVE.getBoolean() && particle.getStyle().canToggleWithMovement() && pplayer.isMoving()) {
                for (PParticle pparticle : DefaultStyles.FEET.getParticles(particle, location))
                    ParticleEffect.display(particle, pparticle, false, pplayer.getPlayer());
            } else {
                for (PParticle pparticle : particle.getStyle().getParticles(particle, location))
                    ParticleEffect.display(particle, pparticle, false, pplayer.getPlayer());
            }
        }  
    }

    /**
     * An alternative method used for event styles
     * 
     * @param particle The ParticlePair to use for getting particle settings
     * @param particles The particles to display
     */
    public void displayParticles(ParticlePair particle, List<PParticle> particles) {
        for (PParticle pparticle : particles)
            ParticleEffect.display(particle, pparticle, false, Bukkit.getPlayer(particle.getOwnerUniqueId()));
    }

    /**
     * Displays particles at the given fixed effect location
     * 
     * @param fixedEffect The fixed effect to display
     */
    private void displayFixedParticleEffect(FixedParticleEffect fixedEffect) {
        ParticlePair particle = fixedEffect.getParticlePair();
        for (PParticle pparticle : particle.getStyle().getParticles(particle, fixedEffect.getLocation().clone().add(0, particle.getStyle().getFixedEffectOffset(), 0)))
            ParticleEffect.display(particle, pparticle, true, null);
    }

    /**
     * Gets the rainbow OrdinaryColor for particle spawning with data 'rainbow'
     * 
     * @return The rainbow OrdinaryColor for particle spawning with data 'rainbow'
     */
    public OrdinaryColor getRainbowParticleColor() {
        Color rgb = Color.getHSBColor(this.hue / 360F, 1.0F, 1.0F);
        return new OrdinaryColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
    }

    /**
     * Gets the rainbow NoteColor for particle spawning with data 'rainbow'
     * 
     * @return The rainbow NoteColor for particle spawning with data 'rainbow'
     */
    public NoteColor getRainbowNoteParticleColor() {
        return new NoteColor(this.note);
    }
    
    /**
     * Gets a randomized OrdinaryColor for particle spawning with data 'random'
     * 
     * @return A randomized OrdinaryColor for particle spawning with data 'random'
     */
    public OrdinaryColor getRandomParticleColor() {
        Color rgb = new Color(this.random.nextInt(256), this.random.nextInt(256), this.random.nextInt(256));
        return new OrdinaryColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
    }
    
    /**
     * Gets a randomized NoteColor for particle spawning with data 'random'
     * 
     * @return A randomized NoteColor for particle spawning with data 'random'
     */
    public NoteColor getRandomNoteParticleColor() {
        return new NoteColor(this.random.nextInt(25));
    }
}
