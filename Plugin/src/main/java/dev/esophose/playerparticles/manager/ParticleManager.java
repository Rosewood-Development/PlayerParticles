package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.hook.WorldGuardHook;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.nms.wrapper.ParticleHandler;
import dev.esophose.playerparticles.particles.ConsolePPlayer;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffectSettings;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.ParticleProperty;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.particles.version.VersionMapping;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.util.NMSUtil;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.MetadataValue;
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

    private ParticleHandler particleHandler;
    private Map<ParticleEffect, ParticleEffectSettings> supportedParticleEffects;

    /**
     * Rainbow particle effect hue and note color used for rainbow colorable effects
     */
    private int hue;
    private int note;
    private final Random random;

    public ParticleManager(PlayerParticles playerParticles) {
        super(playerParticles);

        this.particlePlayers = new ConcurrentHashMap<>();
        this.particleTask = null;
        this.hue = 0;
        this.note = 0;
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

        int overrideVersion = Setting.OVERRIDE_PARTICLE_VERSION.getInt();
        VersionMapping versionMapping = VersionMapping.getVersionMapping(overrideVersion != -1 ? overrideVersion : NMSUtil.getVersionNumber());
        this.particleHandler = NMSUtil.getHandler(versionMapping);

        this.supportedParticleEffects = new HashMap<>();
        for (ParticleEffect particleEffect : versionMapping.getParticleEffectNameMapping().keySet())
            this.supportedParticleEffects.put(particleEffect, new ParticleEffectSettings(particleEffect));

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
    @Override
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
                            this.displayParticles(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
                        return;
                    case "DISPLAY_NORMAL":
                        for (PParticle pparticle : DefaultStyles.NORMAL.getParticles(particle, location))
                            this.displayParticles(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
                        return;
                    case "DISPLAY_OVERHEAD":
                        for (PParticle pparticle : DefaultStyles.OVERHEAD.getParticles(particle, location))
                            this.displayParticles(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
                        return;
                    case "NONE":
                    case "FALSE": // Old default value, keep here for legacy config compatibility
                        break;
                    default:
                        return;
                }
            }

            for (PParticle pparticle : particle.getStyle().getParticles(particle, location))
                this.displayParticles(particle, pparticle, particle.getStyle().hasLongRangeVisibility(), pplayer.getPlayer());
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
            this.displayParticles(particle, pparticle, isLongRange, player);
    }

    /**
     * Displays particles at the given fixed effect location
     * 
     * @param fixedEffect The fixed effect to display
     */
    private void displayFixedParticleEffect(FixedParticleEffect fixedEffect) {
        ParticlePair particle = fixedEffect.getParticlePair();
        for (PParticle pparticle : particle.getStyle().getParticles(particle, fixedEffect.getLocation().clone().add(0, particle.getStyle().getFixedEffectOffset(), 0)))
            this.displayParticles(particle, pparticle, true, null);
    }

    /**
     * The main internal method for spawning particles
     *
     * @param particle The ParticlePair to spawn
     * @param pparticle The particle data to spawn with
     * @param isLongRange true if the particle is viewable at long distances, otherwise false
     * @param owner The owner of the particle, nullable for no online owner
     */
    @SuppressWarnings("deprecation")
    private void displayParticles(ParticlePair particle, PParticle pparticle, boolean isLongRange, Player owner) {
        ParticleEffect effect = particle.getEffect();
        int count = pparticle.isDirectional() ? 0 : 1;

        Object data = null;
        Location center;
        float offsetX, offsetY, offsetZ;
        if (effect.hasProperty(ParticleProperty.REQUIRES_BLOCK_DATA)) {
            if (NMSUtil.getVersionNumber() >= 13) {
                data = particle.getSpawnMaterial().createBlockData();
            } else {
                data = new MaterialData(particle.getSpawnMaterial());
            }

            center = pparticle.getLocation(false);
            offsetX = pparticle.getOffsetX();
            offsetY = pparticle.getOffsetY();
            offsetZ = pparticle.getOffsetZ();
        } else if (effect.hasProperty(ParticleProperty.REQUIRES_ITEM_DATA)) {
            data = new ItemStack(particle.getSpawnMaterial());
            center = pparticle.getLocation(false);
            offsetX = pparticle.getOffsetX();
            offsetY = pparticle.getOffsetY();
            offsetZ = pparticle.getOffsetZ();
        } else if (effect.hasProperty(ParticleProperty.REQUIRES_COLOR_DATA)) {
            center = pparticle.getLocation(true);

            ParticleColor color = particle.getSpawnColor();
            if ((effect == ParticleEffect.DUST || effect == ParticleEffect.DUST_COLOR_TRANSITION) && NMSUtil.getVersionNumber() >= 13) {
                OrdinaryColor dustColor = (OrdinaryColor) color;
                data = new DustOptions(org.bukkit.Color.fromRGB(dustColor.getRed(), dustColor.getGreen(), dustColor.getBlue()), Setting.DUST_SIZE.getFloat());
                offsetX = 0;
                offsetY = 0;
                offsetZ = 0;
            } else {
                offsetX = (effect == ParticleEffect.DUST || effect == ParticleEffect.DUST_COLOR_TRANSITION) && color.getValueX() == 0 ? Float.MIN_VALUE : color.getValueX();
                offsetY = color.getValueY();
                offsetZ = color.getValueZ();
            }
        } else {
            center = pparticle.getLocation(false);
            offsetX = pparticle.getOffsetX();
            offsetY = pparticle.getOffsetY();
            offsetZ = pparticle.getOffsetZ();
        }

        this.particleHandler.spawnParticle(effect, this.getPlayersInRange(center, isLongRange, owner), center, count, offsetX, offsetY, offsetZ, pparticle.getSpeed(), data);
    }

    /**
     * Gets a List of Players within the particle display range
     *
     * @param center The center of the radius to check around
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @return A List of Players within the particle display range
     */
    private List<Player> getPlayersInRange(Location center, boolean isLongRange, Player owner) {
        List<Player> players = new ArrayList<>();
        int range = !isLongRange ? Setting.PARTICLE_RENDER_RANGE_PLAYER.getInt() : Setting.PARTICLE_RENDER_RANGE_FIXED_EFFECT.getInt();
        range *= range;

        for (PPlayer pplayer : PlayerParticles.getInstance().getManager(ParticleManager.class).getPPlayers()) {
            Player p = pplayer.getPlayer();
            if (!this.canSee(p, owner))
                continue;

            if (p != null && pplayer.canSeeParticles() && p.getWorld().equals(center.getWorld()) && center.distanceSquared(p.getLocation()) <= range)
                players.add(p);
        }

        return players;
    }

    /**
     * Checks if a player can see another player
     *
     * @param player The player
     * @param target The target
     * @return True if player can see target, otherwise false
     */
    private boolean canSee(Player player, Player target) {
        if (player == null || target == null)
            return true;

        for (MetadataValue meta : target.getMetadata("vanished"))
            if (meta.asBoolean())
                return false;

        return player.canSee(target);
    }

    /**
     * @return A sorted List of all enabled ParticleEffects
     */
    public List<ParticleEffect> getEnabledEffects() {
        return this.getEnabledEffectsStream()
                .sorted(Comparator.comparing(x -> x.getValue().getName()))
                .map(Entry::getKey)
                .collect(Collectors.toList());
    }

    /**
     * Gets the settings for a ParticleEffect
     *
     * @param particleEffect The ParticleEffect to get the settings for
     * @return The settings for a ParticleEffect
     */
    public ParticleEffectSettings getEffectSettings(ParticleEffect particleEffect) {
        return this.supportedParticleEffects.get(particleEffect);
    }

    /**
     * Returns the ParticleEffect with the given name
     *
     * @param name Name of the ParticleEffect
     * @return The ParticleEffect, or null if not found
     */
    public ParticleEffect getEffectFromName(String name) {
        return this.getEnabledEffectsStream()
                .filter(x -> x.getValue().getName().equalsIgnoreCase(name))
                .findFirst()
                .map(Entry::getKey)
                .orElse(null);
    }

    /**
     * Returns the ParticleEffect with the given internal name
     *
     * @param internalName Internal name of the particle effect
     * @return The ParticleEffect, or null if not found
     */
    public ParticleEffect getEffectFromInternalName(String internalName) {
        return this.getEnabledEffectsStream()
                .filter(x -> x.getValue().getInternalName().equalsIgnoreCase(internalName))
                .findFirst()
                .map(Entry::getKey)
                .orElse(null);
    }

    /**
     * @return A stream of enabled ParticleEffects
     */
    private Stream<Entry<ParticleEffect, ParticleEffectSettings>> getEnabledEffectsStream() {
        return this.supportedParticleEffects.entrySet().stream().filter(x -> x.getValue().isEnabled());
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
