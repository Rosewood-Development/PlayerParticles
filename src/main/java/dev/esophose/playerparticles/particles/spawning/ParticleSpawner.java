package dev.esophose.playerparticles.particles.spawning;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.metadata.MetadataValue;

public abstract class ParticleSpawner {

    /**
     * Displays a particle effect
     *
     * @param particleEffect The particle type to display
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @throws ParticleDataException If the particle effect requires additional data
     */
    public abstract void display(ParticleEffect particleEffect, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner);

    /**
     * Displays a single particle which is colored
     *
     * @param particleEffect The particle type to display
     * @param color Color of the particle
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @throws ParticleColorException If the particle effect is not colorable or the color type is incorrect
     */
    public abstract void display(ParticleEffect particleEffect, ParticleColor color, Location center, boolean isLongRange, Player owner);

    /**
     * Displays a particle effect which requires additional data and is only
     * visible for all players within a certain range in the world of @param
     * center
     *
     * @param particleEffect The particle type to display
     * @param spawnMaterial Material of the effect
     * @param offsetX Maximum distance particles can fly away from the center on the x-axis
     * @param offsetY Maximum distance particles can fly away from the center on the y-axis
     * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
     * @param speed Display speed of the particles
     * @param amount Amount of particles
     * @param center Center location of the effect
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @throws ParticleDataException If the particle effect does not require additional data or if the data type is incorrect
     */
    public abstract void display(ParticleEffect particleEffect, Material spawnMaterial, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner);

    /**
     * Gets a List of Players within the particle display range
     *
     * @param center The center of the radius to check around
     * @param isLongRange If the particle can be viewed from long range
     * @param owner The player that owns the particles
     * @return A List of Players within the particle display range
     */
    protected List<Player> getPlayersInRange(Location center, boolean isLongRange, Player owner) {
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
     * Represents a runtime exception that is thrown either if the displayed
     * particle effect requires data and has none or vice-versa or if the data
     * type is incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     *
     * @author DarkBlade12
     * @since 1.6
     */
    public static final class ParticleDataException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737484L;

        /**
         * Construct a new particle data exception
         *
         * @param message Message that will be logged
         */
        public ParticleDataException(String message) {
            super(message);
        }
    }

    /**
     * Represents a runtime exception that is thrown either if the displayed
     * particle effect is not colorable or if the particle color type is
     * incorrect
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     *
     * @author DarkBlade12
     * @since 1.7
     */
    public static final class ParticleColorException extends RuntimeException {
        private static final long serialVersionUID = 3203085387160737485L;

        /**
         * Construct a new particle color exception
         *
         * @param message Message that will be logged
         */
        public ParticleColorException(String message) {
            super(message);
        }
    }

}
