package com.esophose.playerparticles.manager;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.manager.SettingManager.PSetting;
import com.esophose.playerparticles.particles.FixedParticleEffect;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.PParticle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class ParticleManager extends BukkitRunnable implements Listener {

    /**
     * The list containing all the loaded PPlayer info
     */
    private static List<PPlayer> particlePlayers = new ArrayList<>();

    /**
     * Rainbow particle effect hue and note color used for rainbow colorable effects
     * These should be moved to a more appropriate place later
     */
    private static int hue = 0;
    private static int note = 0;
    private static final Random RANDOM = new Random();

    /**
     * Adds the player to the array when they join
     * 
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent e) {
        DataManager.getPPlayer(e.getPlayer().getUniqueId(), (pplayer) -> { }); // Loads the PPlayer from the database
    }

    /**
     * Removes the player from the array when they log off
     * 
     * @param e The event
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent e) {
        PPlayer pplayer = DataManager.getPPlayer(e.getPlayer().getUniqueId());
        if (pplayer != null && pplayer.getFixedEffectIds().isEmpty()) particlePlayers.remove(pplayer); // Unload the PPlayer if they don't have any fixed effects
    }

    /**
     * Gets the PPlayers that are loaded
     * 
     * @return The loaded PPlayers
     */
    public static List<PPlayer> getPPlayers() {
        return particlePlayers;
    }

    /**
     * Loads all FixedParticleEffects from the database
     * Loads all online PPlayers from the database
     */
    public static void refreshData() {
        particlePlayers.clear();
        DataManager.loadFixedEffects();
        for (Player player : Bukkit.getOnlinePlayers()) {
            DataManager.getPPlayer(player.getUniqueId(), (pplayer) -> { }); // Loads the PPlayer from the database
        }
    }

    /**
     * The main loop to display all the particles
     * Does not display particles if the world is disabled or if the player is in spectator mode
     */
    public void run() {
        ParticleStyleManager.updateTimers();

        hue += PSetting.RAINBOW_CYCLE_SPEED.getInt();
        hue %= 360;

        if (hue % 4 == 0) { // Only increment note by 5 notes per second
            note++;
            note %= 25;
        }

        // Loop over backwards so we can remove pplayers if need be
        for (int i = particlePlayers.size() - 1; i >= 0; i--) {
            PPlayer pplayer = particlePlayers.get(i);
            Player player = pplayer.getPlayer();

            // Don't show their particles if they are in spectator mode
            // Don't spawn particles if the world doesn't allow it
            if (player != null && player.getGameMode() != GameMode.SPECTATOR && PermissionManager.isWorldEnabled(player.getWorld().getName()))
                for (ParticlePair particles : pplayer.getActiveParticles())
                    this.displayParticles(pplayer, particles, player.getLocation().clone().add(0, 1, 0));
            
            // Loop for FixedParticleEffects
            // Don't spawn particles if the world doesn't allow it
            for (FixedParticleEffect effect : pplayer.getFixedParticles())
                if (PermissionManager.isWorldEnabled(effect.getLocation().getWorld().getName()))
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
        if (!ParticleStyleManager.isCustomHandled(particle.getStyle())) {
            if (PSetting.TOGGLE_ON_MOVE.getBoolean() && particle.getStyle().canToggleWithMovement() && pplayer.isMoving()) {
                for (PParticle pparticle : DefaultStyles.FEET.getParticles(particle, location))
                    ParticleEffect.display(particle, pparticle, false, pplayer.getPlayer());
            } else {
                for (PParticle pparticle : particle.getStyle().getParticles(particle, location))
                    ParticleEffect.display(particle, pparticle, false, pplayer.getPlayer());
            }
        }  
    }

    /**
     * An alternative method used for custom handled styles
     * 
     * @param particle The ParticlePair to use for getting particle settings
     * @param particles The particles to display
     */
    public static void displayParticles(ParticlePair particle, List<PParticle> particles) {
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
    public static OrdinaryColor getRainbowParticleColor() {
        Color rgb = Color.getHSBColor(hue / 360F, 1.0F, 1.0F);
        return new OrdinaryColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
    }

    /**
     * Gets the rainbow NoteColor for particle spawning with data 'rainbow'
     * 
     * @return The rainbow NoteColor for particle spawning with data 'rainbow'
     */
    public static NoteColor getRainbowNoteParticleColor() {
        return new NoteColor(note);
    }
    
    /**
     * Gets a randomized OrdinaryColor for particle spawning with data 'random'
     * 
     * @return A randomized OrdinaryColor for particle spawning with data 'random'
     */
    public static OrdinaryColor getRandomParticleColor() {
        Color rgb = new Color(RANDOM.nextInt(256), RANDOM.nextInt(256), RANDOM.nextInt(256));
        return new OrdinaryColor(rgb.getRed(), rgb.getGreen(), rgb.getBlue());
    }
    
    /**
     * Gets a randomized NoteColor for particle spawning with data 'random'
     * 
     * @return A randomized NoteColor for particle spawning with data 'random'
     */
    public static NoteColor getRandomNoteParticleColor() {
        return new NoteColor(RANDOM.nextInt(25));
    }

}
