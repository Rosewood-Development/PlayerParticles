package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.hook.WorldGuardHook;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.awt.Color;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.bukkit.Bukkit;
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
        Bukkit.getScheduler().runTaskLater(this.playerParticles, () -> {
            long ticks = Setting.TICKS_PER_PARTICLE.getLong();
            this.particleTask = Bukkit.getScheduler().runTaskTimerAsynchronously(this.playerParticles, this, 0, ticks);

            if (WorldGuardHook.enabled()) {
                long worldGuardTicks = Setting.WORLDGUARD_CHECK_INTERVAL.getLong();
                this.worldGuardTask = Bukkit.getScheduler().runTaskTimer(this.playerParticles, this::updateWorldGuardStatuses, 0, worldGuardTicks);
            }
        }, 5);
    }

    @Override
    public void disable() {
        if (this.particleTask != null) {
            this.particleTask.cancel();
            this.particleTask = null;
        }

        if (this.worldGuardTask != null) {
            this.worldGuardTask.cancel();
            this.worldGuardTask = null;
        }

        this.particlePlayers.clear();
    }

    /**
     * Adds the player to the array when they join
     * 
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        // Loads the PPlayer from the database
        this.playerParticles.getManager(DataManager.class).getPPlayer(e.getPlayer().getUniqueId(), pplayer -> {
            // If enabled, check permissions for that player
            if (!Setting.CHECK_PERMISSIONS_ON_LOGIN.getBoolean())
                return;

            DataManager dataManager = this.playerParticles.getManager(DataManager.class);
            PermissionManager permissionManager = this.playerParticles.getManager(PermissionManager.class);

            // Check groups
            Iterator<ParticleGroup> groupIterator = pplayer.getParticleGroups().values().iterator();
            while (groupIterator.hasNext()) {
                ParticleGroup group = groupIterator.next();
                int originalSize = group.getParticles().size();
                group.getParticles().values().removeIf(x -> !permissionManager.hasEffectPermission(pplayer, x.getEffect()) || !permissionManager.hasStylePermission(pplayer, x.getStyle()));
                if (group.getParticles().size() == originalSize)
                    continue;

                if (group.getParticles().isEmpty() && !group.getName().equals(ParticleGroup.DEFAULT_NAME)) {
                    dataManager.removeParticleGroup(pplayer.getUniqueId(), group.getName());
                    groupIterator.remove();
                } else {
                    dataManager.saveParticleGroup(pplayer.getUniqueId(), group);
                }
            }

            // Check fixed effects
            Iterator<FixedParticleEffect> fixedParticleEffectIterator = pplayer.getFixedParticles().iterator();
            while (fixedParticleEffectIterator.hasNext()) {
                FixedParticleEffect fixedParticleEffect = fixedParticleEffectIterator.next();
                ParticlePair particlePair = fixedParticleEffect.getParticlePair();
                if (!permissionManager.hasEffectPermission(pplayer, particlePair.getEffect()) || !permissionManager.hasStylePermission(pplayer, particlePair.getStyle())) {
                    dataManager.removeFixedEffect(pplayer.getUniqueId(), fixedParticleEffect.getId());
                    fixedParticleEffectIterator.remove();
                }
            }
        });
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
    public Map<UUID, PPlayer> getPPlayers() {
        return Collections.unmodifiableMap(this.particlePlayers);
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

            // Don't show their particles if they are invisible
            // Don't spawn particles if the world doesn't allow it
            if (player != null && ParticleUtils.isPlayerVisible(player) && permissionManager.isWorldEnabled(player.getWorld().getName()))
                for (ParticlePair particles : pplayer.getActiveParticles())
                    this.displayParticles(pplayer, particles, player.getEyeLocation().add(0, -0.6, 0));
            
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

            for (PParticle pparticle : this.getParticlesWithPlayer(particle, location, pplayer.getPlayer()))
                ParticleEffect.display(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
        }
    }

    /**
     * Gets all particles with the player object, if no particles are returned it defaults to non-player method
     *
     * @param particle The ParticlePair that contains the particle's data
     * @param location The central location of the particles
     * @param player The player that the particles are spawning on - null for fixed effects
     * @return A List of PParticles to spawn
     */
    private List<PParticle> getParticlesWithPlayer(ParticlePair particle, Location location, Player player){
      List<PParticle> particles = particle.getStyle().getParticles(particle, location, player);
      return particles != null ? particles :  particle.getStyle().getParticles(particle, location);
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
        this.displayParticles(pplayer, world, particle, particles, isLongRange, false);
    }

    /**
     * An alternative method used for event styles
     *
     * @param pplayer The PPlayer the particles are spawning from, nullable for special cases
     * @param world The world the particles are spawning in
     * @param particle The ParticlePair to use for getting particle settings
     * @param particles The particles to display
     * @param isLongRange If the particle can be viewed from long range
     * @param ignorePlayerState If the player's visibility state should be ignored
     */
    public void displayParticles(PPlayer pplayer, World world, ParticlePair particle, List<PParticle> particles, boolean isLongRange, boolean ignorePlayerState) {
        PermissionManager permissionManager = this.playerParticles.getManager(PermissionManager.class);

        Player player = null;
        if (!ignorePlayerState) {
            if (pplayer != null) {
                if (!pplayer.isInAllowedRegion())
                    return;
                player = pplayer.getPlayer();
            }

            if ((player != null && !ParticleUtils.isPlayerVisible(player)) || !permissionManager.isWorldEnabled(world.getName()))
                return;
        }

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
     * Gets the rainbow OrdinaryColor for particle spawning with data 'rainbow', shifted half way through the HSB spectrum
     *
     * @return The rainbow OrdinaryColor for particle spawning with data 'rainbow', shifted half way through the HSB spectrum
     */
    public OrdinaryColor getShiftedRainbowParticleColor() {
        Color rgb = Color.getHSBColor((this.hue / 360F) + 0.5F, 1.0F, 1.0F);
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
