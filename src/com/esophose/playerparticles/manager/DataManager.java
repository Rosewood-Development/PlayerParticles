package com.esophose.playerparticles.manager;

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
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * All data changes to PPlayers such as group or fixed effect changes must be done through here,
 * rather than directly on the PPlayer object
 */
public class DataManager {

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
        List<PPlayer> pplayers;
        synchronized (pplayers = ParticleManager.getPPlayers()) { // Under rare circumstances, the PPlayers list can be changed while it's looping
            for (PPlayer pp : pplayers)
                if (pp.getUniqueId().equals(playerUUID))
                    return pp;
            return null;
        }
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
            List<ParticleGroup> groups = new ArrayList<>();
            List<FixedParticleEffect> fixedParticles = new ArrayList<>();

            PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
                // Load settings
                boolean particlesHidden = false;
                String settingsQuery = "SELECT particles_hidden FROM pp_settings WHERE player_uuid = ?";
                try (PreparedStatement statement = connection.prepareStatement(settingsQuery)) {
                    statement.setString(1, playerUUID.toString());

                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        particlesHidden = result.getBoolean("particles_hidden");
                    } else {
                        statement.close();

                        String updateQuery = "INSERT INTO pp_settings (player_uuid, particles_hidden) VALUES (?, ?)";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setString(1, playerUUID.toString());
                            updateStatement.setBoolean(2, false);

                            updateStatement.executeUpdate();
                        }
                    }
                }

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
                        Material itemMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("item_material"));
                        Material blockMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("block_material"));
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
                            List<ParticlePair> particles = new ArrayList<>();
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
                        Material itemMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("item_material"));
                        Material blockMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("block_material"));
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
                    ParticleGroup activeGroup = new ParticleGroup(ParticleGroup.DEFAULT_NAME, new ArrayList<>());
                    saveParticleGroup(playerUUID, activeGroup);
                    groups.add(activeGroup);
                }

                PPlayer loadedPPlayer = new PPlayer(playerUUID, groups, fixedParticles, particlesHidden);

                sync(() -> {
                    synchronized (loadedPPlayer) {
                        if (getPPlayer(playerUUID) == null) { // Make sure the PPlayer still isn't added, since this is async it's possible it got ran twice
                            ParticleManager.getPPlayers().add(loadedPPlayer); // This will be fine now since loadedPPlayer is synchronized
                            callback.execute(loadedPPlayer);
                        }
                    }
                });
            });
        });
    }

    /**
     * Loads all PPlayers from the database that own FixedParticleEffects
     */
    public static void loadFixedEffects() {
        async(() -> PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            String query = "SELECT DISTINCT owner_uuid FROM pp_fixed";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    UUID playerUUID = UUID.fromString(result.getString("owner_uuid"));
                    sync(() -> getPPlayer(playerUUID, (pplayer) -> { }));
                }
            }
        }));
    }

    /**
     * Updates the particles_hidden setting in the database and for the PPlayer
     *
     * @param playerUUID The player to hide PlayerParticles from
     * @param particlesHidden True if the particles should be hidden, otherwise False
     */
    public static void updateSettingParticlesHidden(UUID playerUUID, boolean particlesHidden) {
        async(() -> PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            String updateQuery = "UPDATE pp_settings SET particles_hidden = ? WHERE player_uuid = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setBoolean(1, particlesHidden);
                updateStatement.setString(2, playerUUID.toString());

                updateStatement.executeUpdate();
            }
        }));

        getPPlayer(playerUUID, (pplayer) -> pplayer.setParticlesHidden(particlesHidden));
    }

    /**
     * Saves a ParticleGroup. If it already exists, update it.
     *
     * @param playerUUID The owner of the group
     * @param group The group to create/update
     */
    public static void saveParticleGroup(UUID playerUUID, ParticleGroup group) {
        async(() -> PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            String groupUUID;

            String groupUUIDQuery = "SELECT uuid FROM pp_group WHERE owner_uuid = ? AND name = ?";
            try (PreparedStatement statement = connection.prepareStatement(groupUUIDQuery)) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, group.getName());

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
            }

            // Fill group with new particles
            String createParticlesQuery = "INSERT INTO pp_particle (uuid, group_uuid, id, effect, style, item_material, block_material, note, r, g, b) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement particlesStatement = connection.prepareStatement(createParticlesQuery)) {
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
        }));

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
        async(() -> PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            String groupQuery = "SELECT * FROM pp_group WHERE owner_uuid = ? AND name = ?";
            String particleDeleteQuery = "DELETE FROM pp_particle WHERE group_uuid = ?";
            String groupDeleteQuery = "DELETE FROM pp_group WHERE uuid = ?";

            // Execute group uuid query
            String groupUUID = null;
            try (PreparedStatement statement = connection.prepareStatement(groupQuery)) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, group.getName());

                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    groupUUID = result.getString("uuid");
                }
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
        }));

        getPPlayer(playerUUID, (pplayer) -> pplayer.getParticleGroups().remove(group));
    }

    /**
     * Saves a fixed effect to save data
     * Does not perform a check to see if a fixed effect with this id already exists
     *
     * @param fixedEffect The fixed effect to save
     */
    public static void saveFixedEffect(FixedParticleEffect fixedEffect) {
        async(() -> PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
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
        }));

        getPPlayer(fixedEffect.getOwnerUniqueId(), (pplayer) -> pplayer.addFixedEffect(fixedEffect));
    }

    /**
     * Updates a fixed effect's particle values
     *
     * @param fixedEffect The fixed effect to update
     */
    public static void updateFixedEffect(FixedParticleEffect fixedEffect) {
        async(() -> PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            // Update fixed effect
            String fixedEffectQuery = "UPDATE pp_fixed SET xPos = ?, yPos = ?, zPos = ? WHERE owner_uuid = ? AND id = ?";
            try (PreparedStatement statement = connection.prepareStatement(fixedEffectQuery)) {
                statement.setDouble(1, fixedEffect.getLocation().getX());
                statement.setDouble(2, fixedEffect.getLocation().getY());
                statement.setDouble(3, fixedEffect.getLocation().getZ());
                statement.setString(4, fixedEffect.getOwnerUniqueId().toString());
                statement.setInt(5, fixedEffect.getId());
                statement.executeUpdate();
            }

            // Update particle
            String particleUpdateQuery = "UPDATE pp_particle " +
                                         "SET effect = ?, style = ?, item_material = ?, block_material = ?, note = ?, r = ?, g = ?, b = ? " +
                                         "WHERE uuid = (SELECT particle_uuid FROM pp_fixed WHERE owner_uuid = ? AND id = ?)";
            try (PreparedStatement statement = connection.prepareStatement(particleUpdateQuery)) {
                ParticlePair particle = fixedEffect.getParticlePair();
                statement.setString(1, particle.getEffect().getName());
                statement.setString(2, particle.getStyle().getName());
                statement.setString(3, particle.getItemMaterial().name());
                statement.setString(4, particle.getBlockMaterial().name());
                statement.setInt(5, particle.getNoteColor().getNote());
                statement.setInt(6, particle.getColor().getRed());
                statement.setInt(7, particle.getColor().getGreen());
                statement.setInt(8, particle.getColor().getBlue());
                statement.setString(9, fixedEffect.getOwnerUniqueId().toString());
                statement.setInt(10, fixedEffect.getId());
                statement.executeUpdate();
            }
        }));

        getPPlayer(fixedEffect.getOwnerUniqueId(), (pplayer) -> {
            pplayer.removeFixedEffect(fixedEffect.getId());
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
        async(() -> PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            String particleUUID = null;

            String particleUUIDQuery = "SELECT particle_uuid FROM pp_fixed WHERE owner_uuid = ? AND id = ?";
            try (PreparedStatement statement = connection.prepareStatement(particleUUIDQuery)) {
                statement.setString(1, playerUUID.toString());
                statement.setInt(2, id);

                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    particleUUID = result.getString("particle_uuid");
                }
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
        }));

        getPPlayer(playerUUID, (pplayer) -> pplayer.removeFixedEffect(id));
    }

    /**
     * Asynchronizes the callback with it's own thread
     *
     * @param asyncCallback The callback to run on a separate thread
     */
    private static void async(Runnable asyncCallback) {
        Bukkit.getScheduler().runTaskAsynchronously(PlayerParticles.getPlugin(), asyncCallback);
    }

    /**
     * Synchronizes the callback with the main thread
     *
     * @param syncCallback The callback to run on the main thread
     */
    private static void sync(Runnable syncCallback) {
        Bukkit.getScheduler().runTask(PlayerParticles.getPlugin(), syncCallback);
    }

    /**
     * Allows callbacks to be passed between configuration methods and executed for returning objects after database queries
     */
    @FunctionalInterface
    public interface ConfigurationCallback<T> {
        void execute(T obj);
    }

}
