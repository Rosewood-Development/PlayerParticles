package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.hook.WorldGuardHook;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.ConsolePPlayer;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.util.NMSUtil;
import java.awt.Color;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
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
    private final Map<UUID, PPlayer> particlePlayers;

    /**
     * The task that spawns the particles
     */
    private BukkitTask particleTask;

    /**
     * The task that checks player worldguard region statuses
     */
    private BukkitTask worldGuardTask;

    /**
     * Rainbow particle effect hue and note color used for rainbow colorable effects
     */
    private int hue;
    private final Random random;

    public ParticleManager(PlayerParticles playerParticles) {
        super(playerParticles);

        this.particlePlayers = new ConcurrentHashMap<>();
        this.particleTask = null;
        this.hue = 0;
        this.random = new Random();

        Bukkit.getPluginManager().registerEvents(this, this.playerParticles);
    }

    @Override
    public void reload() {
        if (this.particleTask != null)
            this.particleTask.cancel();

        if (this.worldGuardTask != null) {
            this.worldGuardTask.cancel();
            this.worldGuardTask = null;
        }

        Bukkit.getScheduler().runTaskLater(this.playerParticles, () -> {
            long ticks = Setting.TICKS_PER_PARTICLE.getLong();
            this.particleTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.playerParticles, this, 0, ticks);

            if (WorldGuardHook.enabled()) {
                long worldGuardTicks = Setting.WORLDGUARD_CHECK_INTERVAL.getLong();
                this.worldGuardTask = Bukkit.getScheduler().runTaskTimer(this.playerParticles, this::updateWorldGuardStatuses, 0, worldGuardTicks);
            }
        }, 5);

        this.particlePlayers.clear();
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        dataManager.loadFixedEffects();
        for (Player player : Bukkit.getOnlinePlayers())
            dataManager.getPPlayer(player.getUniqueId(), (pplayer) -> { }); // Loads the PPlayer from the database
        dataManager.getPPlayer(ConsolePPlayer.getUUID(), (pplayer) -> { }); // Load the console PPlayer
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
        if (pplayer != null) {
            pplayer.clearCachedPlayer();
            if (pplayer.getFixedEffectIds().isEmpty())
                this.particlePlayers.remove(pplayer.getUniqueId()); // Unload the PPlayer if they don't have any fixed effects
        }
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
     * Adds a PPlayer to the loaded map
     *
     * @param pplayer The PPlayer to add
     */
    public void addPPlayer(PPlayer pplayer) {
        this.particlePlayers.put(pplayer.getUniqueId(), pplayer);
    }

    /**
     * The main loop to display all the particles
     * Does not display particles if the world is disabled or if the player is in spectator mode
     */
    public void run() {
        this.playerParticles.getManager(ParticleStyleManager.class).updateTimers();

        this.hue += Setting.RAINBOW_CYCLE_SPEED.getInt();
        this.hue %= 360;

        PermissionManager permissionManager = this.playerParticles.getManager(PermissionManager.class);

        // Spawn particles for each player
        for (PPlayer pplayer : this.particlePlayers.values()) {
            Player player = pplayer.getPlayer();

            // Don't show their particles if they are in spectator mode
            // Don't spawn particles if the world doesn't allow it
            if (player != null && (NMSUtil.getVersionNumber() < 8 || player.getGameMode() != GameMode.SPECTATOR) && permissionManager.isWorldEnabled(player.getWorld().getName()))
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
     * Updates the WorldGuard region statuses for players
     */
    private void updateWorldGuardStatuses() {
        PermissionManager permissionManager = this.playerParticles.getManager(PermissionManager.class);

        for (PPlayer pplayer : this.particlePlayers.values()) {
            Player player = pplayer.getPlayer();
            if (player == null)
                continue;

            boolean inAllowedRegion = WorldGuardHook.isInAllowedRegion(player.getLocation());
            if (!inAllowedRegion && Setting.WORLDGUARD_ENABLE_BYPASS_PERMISSION.getBoolean())
                inAllowedRegion = permissionManager.hasWorldGuardBypass(player);

            pplayer.setInAllowedRegion(inAllowedRegion);
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
            if (Setting.TOGGLE_ON_COMBAT.getBoolean() && particle.getStyle().canToggleWithCombat() && pplayer.isInCombat())
                return;

            if (!pplayer.isInAllowedRegion())
                return;

            if (particle.getStyle().canToggleWithMovement() && pplayer.isMoving()) {
                switch (Setting.TOGGLE_ON_MOVE.getString().toUpperCase()) {
                    case "DISPLAY_FEET":
                    case "TRUE": // Old default value, keep here for legacy config compatibility
                        for (PParticle pparticle : DefaultStyles.FEET.getParticles(particle, location))
                            ParticleEffect.display(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
                        return;
                    case "DISPLAY_NORMAL":
                        for (PParticle pparticle : DefaultStyles.NORMAL.getParticles(particle, location))
                            ParticleEffect.display(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
                        return;
                    case "DISPLAY_OVERHEAD":
                        for (PParticle pparticle : DefaultStyles.OVERHEAD.getParticles(particle, location))
                            ParticleEffect.display(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
                        return;
                    case "NONE":
                    case "FALSE": // Old default value, keep here for legacy config compatibility
                        break;
                    default:
                        return;
                }
            }

            for (PParticle pparticle : particle.getStyle().getParticles(particle, location))
                ParticleEffect.display(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
        }  
    }

    /**
     * An alternative method used for event styles
     *
     * @param pplayer The PPlayer the particles are spawning from, nullable for special cases
     * @param world The world the particles are spawning in
     * @param particle The ParticlePair to use for getting particle settings
     * @param particles The particles to display
     * @param isLongRange If the particle can be viewed from long range
     */
    public void displayParticles(PPlayer pplayer, World world, ParticlePair particle, List<PParticle> particles, boolean isLongRange) {
        PermissionManager permissionManager = this.playerParticles.getManager(PermissionManager.class);

        Player player = null;
        if (pplayer != null) {
            if (!pplayer.isInAllowedRegion())
                return;
            player = pplayer.getPlayer();
        }

        if ((player != null && (NMSUtil.getVersionNumber() < 8 || player.getGameMode() == GameMode.SPECTATOR)) || !permissionManager.isWorldEnabled(world.getName()))
            return;

        for (PParticle pparticle : particles)
            ParticleEffect.display(particle, pparticle, isLongRange, player);
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
        int note = ((int) Math.round(24 - 24 / 360.0 * this.hue) + 7) % 24;
        return new NoteColor(note);
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
