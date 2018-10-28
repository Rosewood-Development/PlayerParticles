package com.esophose.playerparticles.manager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.particles.FixedParticleEffect;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.ParticleUtils;

/**
 * All data changes to PPlayers such as group or fixed effect changes must be done through here,
 * rather than directly on the PPlayer object
 */
public class DataManager {

    /**
     * The disabled worlds cached for quick access
     */
    private static List<String> disabledWorlds = null;

    /**
     * The max number of fixed effects a player can have, defined in the config
     */
    private static int maxFixedEffects = -1;

    /**
     * The max distance a fixed effect can be created relative to the player
     */
    private static int maxFixedEffectCreationDistance = -1;

    /**
     * This is not instantiable
     */
    private DataManager() {
        
    }

    /**
     * Gets a PPlayer from cache
     * This method should be used over the other one unless you absolutely need the PPlayer and you don't care about waiting
     * You should always check for a null result when using this method
     * 
     * @param playerUUID The PPlayer to get
     * @return The PPlayer from cache
     */
    public static PPlayer getPPlayer(UUID playerUUID) {
        for (PPlayer pp : ParticleManager.particlePlayers)
            if (pp.getUniqueId().equals(playerUUID)) return pp;
        return null;
    }

    /**
     * Gets a player from the save data, creates one if it doesn't exist and caches it
     * 
     * @param playerUUID The pplayer to get
     * @param callback The callback to execute with the found pplayer, or a newly generated one
     */
    public static void getPPlayer(UUID playerUUID, ConfigurationCallback<PPlayer> callback) {
        // Try to get them from cache first
        PPlayer fromCache = getPPlayer(playerUUID);
        if (fromCache != null) {
            callback.execute(fromCache);
            return;
        }

        async(() -> {
            List<ParticleGroup> groups = new ArrayList<ParticleGroup>();
            List<FixedParticleEffect> fixedParticles = new ArrayList<FixedParticleEffect>();

            PlayerParticles.getDBConnector().connect((connection) -> {
                // Load particle groups
                String groupQuery = "SELECT * FROM pp_group g " + // @formatter:off
        					   	    "JOIN pp_particle p ON g.uuid = p.group_uuid " + 
        					   	    "WHERE g.owner_uuid = ?"; // @formatter:on
                try (PreparedStatement statement = connection.prepareStatement(groupQuery)) {
                    statement.setString(1, playerUUID.toString());

                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        // Group properties
                        String groupName = result.getString("name");

                        // Particle properties
                        int id = result.getInt("id");
                        ParticleEffect effect = ParticleEffect.fromName(result.getString("effect"));
                        ParticleStyle style = ParticleStyle.fromName(result.getString("style"));
                        Material itemMaterial = ParticleUtils.closestMatchWithFallback(result.getString("item_material"));
                        Material blockMaterial = ParticleUtils.closestMatchWithFallback(result.getString("block_material"));
                        NoteColor noteColor = new NoteColor(result.getInt("note"));
                        OrdinaryColor color = new OrdinaryColor(result.getInt("r"), result.getInt("g"), result.getInt("b"));
                        ParticlePair particle = new ParticlePair(playerUUID, id, effect, style, itemMaterial, blockMaterial, color, noteColor);

                        // Try to add particle to an existing group
                        boolean groupAlreadyExists = false;
                        for (ParticleGroup group : groups) {
                            if (group.getName().equalsIgnoreCase(groupName)) {
                                group.getParticles().add(particle);
                                groupAlreadyExists = true;
                                break;
                            }
                        }

                        // Add the particle to a new group if one didn't already exist
                        if (!groupAlreadyExists) {
                            List<ParticlePair> particles = new ArrayList<ParticlePair>();
                            particles.add(particle);
                            ParticleGroup newGroup = new ParticleGroup(groupName, particles);
                            groups.add(newGroup);
                        }
                    }
                }

                // Load fixed effects
                String fixedQuery = "SELECT f.id AS f_id, f.world, f.xPos, f.yPos, f.zPos, p.id AS p_id, p.effect, p.style, p.item_material, p.block_material, p.note, p.r, p.g, p.b FROM pp_fixed f " + // @formatter:off
        						    "JOIN pp_particle p ON f.particle_uuid = p.uuid " + 
        						    "WHERE f.owner_uuid = ?"; // @formatter:on
                try (PreparedStatement statement = connection.prepareStatement(fixedQuery)) {
                    statement.setString(1, playerUUID.toString());

                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        // Fixed effect properties
                        int fixedEffectId = result.getInt("f_id");
                        String worldName = result.getString("world");
                        double xPos = result.getDouble("xPos");
                        double yPos = result.getDouble("yPos");
                        double zPos = result.getDouble("zPos");

                        // Particle properties
                        int particleId = result.getInt("p_id");
                        ParticleEffect effect = ParticleEffect.fromName(result.getString("effect"));
                        ParticleStyle style = ParticleStyle.fromName(result.getString("style"));
                        Material itemMaterial = ParticleUtils.closestMatchWithFallback(result.getString("item_material"));
                        Material blockMaterial = ParticleUtils.closestMatchWithFallback(result.getString("block_material"));
                        NoteColor noteColor = new NoteColor(result.getInt("note"));
                        OrdinaryColor color = new OrdinaryColor(result.getInt("r"), result.getInt("g"), result.getInt("b"));
                        ParticlePair particle = new ParticlePair(playerUUID, particleId, effect, style, itemMaterial, blockMaterial, color, noteColor);

                        fixedParticles.add(new FixedParticleEffect(playerUUID, fixedEffectId, worldName, xPos, yPos, zPos, particle));
                    }
                }
                
                // If there aren't any groups then this is a brand new PPlayer and we need to save a new active group for them
                boolean activeGroupExists = false;
                for (ParticleGroup group : groups) {
                    if (group.getName().equals(ParticleGroup.DEFAULT_NAME)) {
                        activeGroupExists = true;
                        break;
                    }
                }

                if (!activeGroupExists) { 
                    ParticleGroup activeGroup = new ParticleGroup(ParticleGroup.DEFAULT_NAME, new ArrayList<ParticlePair>());
                    saveParticleGroup(playerUUID, activeGroup);
                    groups.add(activeGroup);
                }

                final PPlayer loadedPPlayer = new PPlayer(playerUUID, groups, fixedParticles);
                
                sync(() -> {
                    if (getPPlayer(playerUUID) == null) { // Make sure the PPlayer still isn't added, since this is async it's possible it got ran twice
                        ParticleManager.particlePlayers.add(loadedPPlayer);
                        callback.execute(loadedPPlayer);
                    }
                });
            });
        });
    }
    
    /**
     * Loads all PPlayers from the database that own FixedParticleEffects
     */
    public static void loadFixedEffects() {
        async(() -> {
            PlayerParticles.getDBConnector().connect((connection) -> {
                String query = "SELECT DISTINCT owner_uuid FROM pp_fixed";
                try (PreparedStatement statement = connection.prepareStatement(query)) {
                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        UUID playerUUID = UUID.fromString(result.getString("owner_uuid"));
                        getPPlayer(playerUUID, (pplayer) -> { });
                    }
                }
            });
        });
    }

    /**
     * Saves a ParticleGroup. If it already exists, update it.
     * 
     * @param playerUUID The owner of the group
     * @param group The group to create/update
     */
    public static void saveParticleGroup(UUID playerUUID, ParticleGroup group) {
        async(() -> {
            PlayerParticles.getDBConnector().connect((connection) -> {
                String groupUUIDQuery = "SELECT uuid FROM pp_group WHERE owner_uuid = ? AND name = ?";
                try (PreparedStatement statement = connection.prepareStatement(groupUUIDQuery)) {
                    statement.setString(1, playerUUID.toString());
                    statement.setString(2, group.getName());

                    String groupUUID = null;

                    ResultSet result = statement.executeQuery();
                    if (result.next()) { // Clear out particles from existing group
                        groupUUID = result.getString("uuid");

                        String particlesDeleteQuery = "DELETE FROM pp_particle WHERE group_uuid = ?";
                        PreparedStatement particlesDeleteStatement = connection.prepareStatement(particlesDeleteQuery);
                        particlesDeleteStatement.setString(1, result.getString("uuid"));

                        particlesDeleteStatement.executeUpdate();
                    } else { // Create new group
                        groupUUID = UUID.randomUUID().toString();

                        String groupCreateQuery = "INSERT INTO pp_group (uuid, owner_uuid, name) VALUES (?, ?, ?)";
                        PreparedStatement groupCreateStatement = connection.prepareStatement(groupCreateQuery);
                        groupCreateStatement.setString(1, groupUUID);
                        groupCreateStatement.setString(2, playerUUID.toString());
                        groupCreateStatement.setString(3, group.getName());

                        groupCreateStatement.executeUpdate();
                    }

                    // Fill group with new particles
                    String createParticlesQuery = "INSERT INTO pp_particle (uuid, group_uuid, id, effect, style, item_material, block_material, note, r, g, b) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    PreparedStatement particlesStatement = connection.prepareStatement(createParticlesQuery);
                    for (ParticlePair particle : group.getParticles()) {
                        particlesStatement.setString(1, UUID.randomUUID().toString());
                        particlesStatement.setString(2, groupUUID);
                        particlesStatement.setInt(3, particle.getId());
                        particlesStatement.setString(4, particle.getEffect().getName());
                        particlesStatement.setString(5, particle.getStyle().getName());
                        particlesStatement.setString(6, particle.getItemMaterial().name());
                        particlesStatement.setString(7, particle.getBlockMaterial().name());
                        particlesStatement.setInt(8, particle.getNoteColor().getNote());
                        particlesStatement.setInt(9, particle.getColor().getRed());
                        particlesStatement.setInt(10, particle.getColor().getGreen());
                        particlesStatement.setInt(11, particle.getColor().getBlue());
                        particlesStatement.addBatch();
                    }
                    
                    particlesStatement.executeBatch();
                }
            });
        });

        getPPlayer(playerUUID, (pplayer) -> {
            for (ParticleGroup existing : pplayer.getParticleGroups()) {
                if (group.getName().equalsIgnoreCase(existing.getName())) {
                    pplayer.getParticleGroups().remove(existing);
                    break;
                }
            }
            pplayer.getParticleGroups().add(group);
        });
    }

    /**
     * Removes a ParticleGroup
     * 
     * @param playerUUID The owner of the group
     * @param group The group to remove
     */
    public static void removeParticleGroup(UUID playerUUID, ParticleGroup group) {
        async(() -> {
            PlayerParticles.getDBConnector().connect((connection) -> {
                String groupQuery = "SELECT * FROM pp_group WHERE owner_uuid = ? AND name = ?";
                String particleDeleteQuery = "DELETE FROM pp_particle WHERE group_uuid = ?";
                String groupDeleteQuery = "DELETE FROM pp_group WHERE uuid = ?";

                // Execute group uuid query
                String groupUUID = null;
                try (PreparedStatement statement = connection.prepareStatement(groupQuery)) {
                    statement.setString(1, playerUUID.toString());
                    statement.setString(2, group.getName());

                    ResultSet result = statement.executeQuery();
                    groupUUID = result.getString("uuid");
                }

                // Execute particle delete update
                try (PreparedStatement statement = connection.prepareStatement(particleDeleteQuery)) {
                    statement.setString(1, groupUUID);

                    statement.executeUpdate();
                }

                // Execute group delete update
                try (PreparedStatement statement = connection.prepareStatement(groupDeleteQuery)) {
                    statement.setString(1, groupUUID);

                    statement.executeUpdate();
                }
            });
        });

        getPPlayer(playerUUID, (pplayer) -> {
            pplayer.getParticleGroups().remove(group);
        });
    }

    /**
     * Saves a fixed effect to save data
     * Does not perform a check to see if a fixed effect with this id already exists
     * 
     * @param fixedEffect The fixed effect to save
     */
    public static void saveFixedEffect(FixedParticleEffect fixedEffect) {
        async(() -> {
            PlayerParticles.getDBConnector().connect((connection) -> {
                String particleUUID = UUID.randomUUID().toString();

                String particleQuery = "INSERT INTO pp_particle (uuid, group_uuid, id, effect, style, item_material, block_material, note, r, g, b) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(particleQuery)) {
                    ParticlePair particle = fixedEffect.getParticlePair();
                    statement.setString(1, particleUUID);
                    statement.setNull(2, java.sql.Types.VARCHAR);
                    statement.setInt(3, fixedEffect.getId());
                    statement.setString(4, particle.getEffect().getName());
                    statement.setString(5, particle.getStyle().getName());
                    statement.setString(6, particle.getItemMaterial().name());
                    statement.setString(7, particle.getBlockMaterial().name());
                    statement.setInt(8, particle.getNoteColor().getNote());
                    statement.setInt(9, particle.getColor().getRed());
                    statement.setInt(10, particle.getColor().getGreen());
                    statement.setInt(11, particle.getColor().getBlue());
                    statement.executeUpdate();
                }

                String fixedEffectQuery = "INSERT INTO pp_fixed (owner_uuid, id, particle_uuid, world, xPos, yPos, zPos) VALUES (?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement statement = connection.prepareStatement(fixedEffectQuery)) {
                    statement.setString(1, fixedEffect.getOwnerUniqueId().toString());
                    statement.setInt(2, fixedEffect.getId());
                    statement.setString(3, particleUUID);
                    statement.setString(4, fixedEffect.getLocation().getWorld().getName());
                    statement.setDouble(5, fixedEffect.getLocation().getX());
                    statement.setDouble(6, fixedEffect.getLocation().getY());
                    statement.setDouble(7, fixedEffect.getLocation().getZ());
                    statement.executeUpdate();
                }
            });
        });

        getPPlayer(fixedEffect.getOwnerUniqueId(), (pplayer) -> {
            pplayer.addFixedEffect(fixedEffect);
        });
    }

    /**
     * Deletes a fixed effect from save data
     * Does not perform a check to see if a fixed effect with this id already exists
     * 
     * @param playerUUID The player who owns the effect
     * @param id The id of the effect to remove
     */
    public static void removeFixedEffect(UUID playerUUID, int id) {
        async(() -> {
            PlayerParticles.getDBConnector().connect((connection) -> {
                String particleUUID = null;

                String particleUUIDQuery = "SELECT particle_uuid FROM pp_fixed WHERE owner_uuid = ? AND id = ?";
                try (PreparedStatement statement = connection.prepareStatement(particleUUIDQuery)) {
                    statement.setString(1, playerUUID.toString());
                    statement.setInt(2, id);

                    ResultSet result = statement.executeQuery();
                    particleUUID = result.getString("particle_uuid");
                }

                String particleDeleteQuery = "DELETE FROM pp_particle WHERE uuid = ?";
                try (PreparedStatement statement = connection.prepareStatement(particleDeleteQuery)) {
                    statement.setString(1, particleUUID);

                    statement.executeUpdate();
                }

                String fixedEffectDeleteQuery = "DELETE FROM pp_fixed WHERE owner_uuid = ? AND id = ?";
                try (PreparedStatement statement = connection.prepareStatement(fixedEffectDeleteQuery)) {
                    statement.setString(1, playerUUID.toString());
                    statement.setInt(2, id);

                    statement.executeUpdate();
                }
            });
        });

        getPPlayer(playerUUID, (pplayer) -> {
            pplayer.removeFixedEffect(id);
        });
    }

    /**
     * Checks if the given player has reached the max number of fixed effects
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of fixed effects
     */
    public static boolean hasPlayerReachedMaxFixedEffects(PPlayer pplayer) {
        if (maxFixedEffects == -1) { // Initialize on the fly
            maxFixedEffects = PlayerParticles.getPlugin().getConfig().getInt("max-fixed-effects");
        }

        if (pplayer.getPlayer().hasPermission("playerparticles.fixed.unlimited")) return false;
        return pplayer.getFixedEffectIds().size() >= maxFixedEffects;
    }

    /**
     * Gets the max distance a fixed effect can be created from the player
     * 
     * @return The max distance a fixed effect can be created from the player
     */
    public static int getMaxFixedEffectCreationDistance() {
        if (maxFixedEffectCreationDistance == -1) { // Initialize on the fly
            maxFixedEffectCreationDistance = PlayerParticles.getPlugin().getConfig().getInt("max-fixed-effect-creation-distance");
        }
        return maxFixedEffectCreationDistance;
    }

    /**
     * Checks if a world is disabled for particles to spawn in
     * 
     * @param world The world name to check
     * @return True if the world is disabled
     */
    public static boolean isWorldDisabled(String world) {
        return getDisabledWorlds().contains(world);
    }

    /**
     * Gets all the worlds that are disabled
     * 
     * @return All world names that are disabled
     */
    public static List<String> getDisabledWorlds() {
        if (disabledWorlds == null) { // Initialize on the fly
            disabledWorlds = PlayerParticles.getPlugin().getConfig().getStringList("disabled-worlds");
        }
        return disabledWorlds;
    }
    
    /**
     * Resets all config-related settings
     */
    public static void reload() {
        maxFixedEffects = -1;
        maxFixedEffectCreationDistance = -1;
        disabledWorlds = null;
    }

    /**
     * Asynchronizes the callback with it's own thread
     * 
     * @param asyncCallback The callback to run on a separate thread
     */
    private static void async(SyncInterface asyncCallback) {
        new BukkitRunnable() {
            public void run() {
                asyncCallback.execute();
            }
        }.runTaskAsynchronously(PlayerParticles.getPlugin());
    }

    /**
     * Synchronizes the callback with the main thread
     * 
     * @param syncCallback The callback to run on the main thread
     */
    private static void sync(SyncInterface syncCallback) {
        new BukkitRunnable() {
            public void run() {
                syncCallback.execute();
            }
        }.runTask(PlayerParticles.getPlugin());
    }

    /**
     * Provides an easy way to run a section of code either synchronously or asynchronously using a callback
     */
    private static interface SyncInterface {
        public void execute();
    }

    /**
     * Allows callbacks to be passed between configuration methods and executed for returning objects after database queries
     */
    public static interface ConfigurationCallback<T> {
        public void execute(T obj);
    }

}
