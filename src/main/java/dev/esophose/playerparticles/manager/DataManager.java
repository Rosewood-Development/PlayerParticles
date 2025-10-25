package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.database.migrations._1_InitialMigration;
import dev.esophose.playerparticles.database.migrations._2_Add_Data_Columns;
import dev.esophose.playerparticles.database.migrations._3_Add_Setting_Toggle_Self_Column;
import dev.esophose.playerparticles.database.migrations._4_Add_Fixed_Effect_Yaw_Pitch_Columns;
import dev.esophose.playerparticles.database.migrations._5_Add_Data_Transparency_Column;
import dev.esophose.playerparticles.particles.ConsolePPlayer;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleDataType;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.particles.data.TransparentColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.SQLiteConnector;
import dev.rosewood.rosegarden.manager.AbstractDataManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * All data changes to PPlayers such as group or fixed effect changes must be done through here,
 * rather than directly on the PPlayer object
 */
public class DataManager extends AbstractDataManager {

    public DataManager(RosePlugin playerParticles) {
        super(playerParticles);
    }

    /**
     * Loads all fixed effects and player particles from the database
     */
    public void loadEffects() {
        this.rosePlugin.getScheduler().runTaskAsync(() -> {
            for (Player player : Bukkit.getOnlinePlayers())
                this.getPPlayer(player.getUniqueId(), (pplayer) -> { }, false); // Loads the PPlayer from the database

            this.getPPlayer(ConsolePPlayer.getUUID(), (pplayer) -> { }, false); // Load the console PPlayer
            this.loadFixedEffects();
        });
    }

    /**
     * Gets a PPlayer from cache
     * This method should be used over the other one unless you absolutely need the PPlayer and you don't care about waiting
     * You should always check for a null result when using this method
     *
     * @param playerUUID The PPlayer to get
     * @return The PPlayer from cache
     */
    public PPlayer getPPlayer(UUID playerUUID) {
        return this.rosePlugin.getManager(ParticleManager.class).getPPlayers().get(playerUUID);
    }

    /**
     * Gets a player from the save data, creates one if it doesn't exist and caches it
     *
     * @param playerUUID The pplayer to get
     * @param callback The callback to execute with the found pplayer, or a newly generated one
     */
    public void getPPlayer(UUID playerUUID, Consumer<PPlayer> callback) {
        this.getPPlayer(playerUUID, callback, true);
    }

    /**
     * Gets a player from the save data, creates one if it doesn't exist and caches it
     *
     * @param playerUUID The pplayer to get
     * @param callback The callback to execute with the found pplayer, or a newly generated one
     * @param readCache If true, tries to read from the cache first, otherwise always forcefully reloads the player data
     */
    public void getPPlayer(UUID playerUUID, Consumer<PPlayer> callback, boolean readCache) {
        // Try to get them from cache first if allowed
        if (readCache) {
            PPlayer fromCache = this.getPPlayer(playerUUID);
            if (fromCache != null) {
                callback.accept(fromCache);
                return;
            }
        }

        Runnable task = () -> {
            Map<String, ParticleGroup> groups = new ConcurrentHashMap<>();
            Map<Integer, FixedParticleEffect> fixedParticles = new ConcurrentHashMap<>();

            this.databaseConnector.connect((connection) -> {
                // Load settings
                boolean particlesHidden = false;
                boolean particlesHiddenSelf = false;
                String settingsQuery = "SELECT particles_hidden, particles_hidden_self FROM " + this.getTablePrefix() + "settings WHERE player_uuid = ?";
                try (PreparedStatement statement = connection.prepareStatement(settingsQuery)) {
                    statement.setString(1, playerUUID.toString());

                    ResultSet result = statement.executeQuery();
                    if (result.next()) {
                        particlesHidden = result.getBoolean("particles_hidden");
                        particlesHiddenSelf = result.getBoolean("particles_hidden_self");
                    } else {
                        statement.close();

                        String updateQuery = "INSERT INTO " + this.getTablePrefix() + "settings (player_uuid, particles_hidden, particles_hidden_self) VALUES (?, ?, ?)";
                        try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                            updateStatement.setString(1, playerUUID.toString());
                            updateStatement.setBoolean(2, false);
                            updateStatement.setBoolean(3, false);

                            updateStatement.executeUpdate();
                        }
                    }
                }

                // Load particle groups
                String groupQuery = "SELECT * FROM " + this.getTablePrefix() + "group g " +
                        "JOIN " + this.getTablePrefix() + "particle p ON g.uuid = p.group_uuid " +
                        "WHERE g.owner_uuid = ?";
                try (PreparedStatement statement = connection.prepareStatement(groupQuery)) {
                    statement.setString(1, playerUUID.toString());

                    Set<String> modifiedGroups = new HashSet<>();
                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        // Group properties
                        String groupName = result.getString("name");

                        // Particle properties
                        int id = result.getInt("id");
                        ParticleEffect effect = ParticleEffect.fromInternalName(result.getString("effect"));
                        ParticleStyle style = ParticleStyle.fromInternalName(result.getString("style"));
                        Material itemMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("item_material"));
                        Material blockMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("block_material"));
                        NoteColor noteColor = new NoteColor(result.getInt("note"));
                        OrdinaryColor color;
                        if (effect != null && effect.getDataType() == ParticleDataType.COLORABLE_TRANSPARENCY) {
                            color = new TransparentColor(result.getInt("r"), result.getInt("g"), result.getInt("b"), result.getInt("a"));
                        } else {
                            color = new OrdinaryColor(result.getInt("r"), result.getInt("g"), result.getInt("b"));
                        }
                        ColorTransition colorTransition = new ColorTransition(color, new OrdinaryColor(result.getInt("r_end"), result.getInt("g_end"), result.getInt("b_end")));
                        Vibration vibration = new Vibration(result.getInt("duration"));
                        ParticlePair particle = new ParticlePair(playerUUID, id, effect, style, itemMaterial, blockMaterial, color, noteColor, colorTransition, vibration);

                        boolean invalid = effect == null || style == null;
                        if (invalid) // Effect or style is now missing or disabled, remove the particle
                            modifiedGroups.add(groupName);

                        // Try to add particle to an existing group
                        boolean groupAlreadyExists = false;
                        for (ParticleGroup group : groups.values()) {
                            if (group.getName().equalsIgnoreCase(groupName)) {
                                if (!invalid)
                                    group.getParticles().put(particle.getId(), particle);
                                groupAlreadyExists = true;
                                break;
                            }
                        }

                        // Add the particle to a new group if one didn't already exist
                        if (!groupAlreadyExists) {
                            Map<Integer, ParticlePair> particles = new ConcurrentHashMap<>();
                            if (!invalid)
                                particles.put(particle.getId(), particle);
                            ParticleGroup newGroup = new ParticleGroup(groupName, particles);
                            groups.put(newGroup.getName().toLowerCase(), newGroup);
                        }
                    }

                    // Update modified groups
                    for (String modifiedGroup : modifiedGroups) {
                        ParticleGroup group = groups.get(modifiedGroup.toLowerCase());
                        this.saveParticleGroup(playerUUID, group);
                        if (group.getParticles().isEmpty() && !group.getName().equals(ParticleGroup.DEFAULT_NAME))
                            groups.remove(modifiedGroup);
                    }
                }

                boolean deleteInvalidFixedEffects = dev.esophose.playerparticles.config.Settings.DELETE_INVALID_FIXED_EFFECTS.get();

                // Load fixed effects
                String fixedQuery = "SELECT f.id AS f_id, f.world, f.xPos, f.yPos, f.zPos, f.yaw, f.pitch, p.id AS p_id, p.effect, p.style, p.item_material, p.block_material, p.note, p.r, p.g, p.b, p.a, p.r_end, p.g_end, p.b_end, p.duration FROM " + this.getTablePrefix() + "fixed f " +
                        "JOIN " + this.getTablePrefix() + "particle p ON f.particle_uuid = p.uuid " +
                        "WHERE f.owner_uuid = ?";
                try (PreparedStatement statement = connection.prepareStatement(fixedQuery)) {
                    statement.setString(1, playerUUID.toString());

                    ResultSet result = statement.executeQuery();
                    while (result.next()) {
                        // Fixed effect properties
                        int fixedEffectId = result.getInt("f_id");
                        double xPos = result.getDouble("xPos");
                        double yPos = result.getDouble("yPos");
                        double zPos = result.getDouble("zPos");
                        double yaw = result.getDouble("yaw");
                        double pitch = result.getDouble("pitch");
                        World world = Bukkit.getWorld(result.getString("world"));
                        if (world == null) {
                            // World was deleted, remove the fixed effect as it is no longer valid
                            // Only delete on SQLite, as a MySQL server may have fixed effects from other servers saved
                            if (this.databaseConnector instanceof SQLiteConnector && deleteInvalidFixedEffects)
                                this.removeFixedEffect(playerUUID, fixedEffectId);
                            continue;
                        }

                        // Particle properties
                        int particleId = result.getInt("p_id");
                        ParticleEffect effect = ParticleEffect.fromInternalName(result.getString("effect"));
                        ParticleStyle style = ParticleStyle.fromInternalName(result.getString("style"));
                        Material itemMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("item_material"));
                        Material blockMaterial = ParticleUtils.closestMatchWithFallback(true, result.getString("block_material"));
                        NoteColor noteColor = new NoteColor(result.getInt("note"));
                        OrdinaryColor color;
                        if (effect != null && effect.getDataType() == ParticleDataType.COLORABLE_TRANSPARENCY) {
                            color = new TransparentColor(result.getInt("r"), result.getInt("g"), result.getInt("b"), result.getInt("a"));
                        } else {
                            color = new OrdinaryColor(result.getInt("r"), result.getInt("g"), result.getInt("b"));
                        }
                        ColorTransition colorTransition = new ColorTransition(color, new OrdinaryColor(result.getInt("r_end"), result.getInt("g_end"), result.getInt("b_end")));
                        Vibration vibration = new Vibration(result.getInt("duration"));
                        ParticlePair particle = new ParticlePair(playerUUID, particleId, effect, style, itemMaterial, blockMaterial, color, noteColor, colorTransition, vibration);

                        // Effect or style is now missing or disabled, remove the fixed effect
                        if (effect == null || style == null) {
                            if (deleteInvalidFixedEffects)
                                this.removeFixedEffect(playerUUID, fixedEffectId);
                            continue;
                        }

                        fixedParticles.put(fixedEffectId, new FixedParticleEffect(playerUUID, fixedEffectId, new Location(world, xPos, yPos, zPos, (float) yaw, (float) pitch), particle));
                    }
                }

                // If there aren't any groups then this is a brand new PPlayer and we need to save a new active group for them
                boolean activeGroupExists = false;
                for (ParticleGroup group : groups.values()) {
                    if (group.getName().equals(ParticleGroup.DEFAULT_NAME)) {
                        activeGroupExists = true;
                        break;
                    }
                }

                if (!activeGroupExists) {
                    ParticleGroup activeGroup = new ParticleGroup(ParticleGroup.DEFAULT_NAME, new ConcurrentHashMap<>());
                    this.saveParticleGroup(playerUUID, activeGroup);
                    groups.put(activeGroup.getName(), activeGroup);
                }

                PPlayer loadedPPlayer;
                if (!playerUUID.equals(ConsolePPlayer.getUUID())) {
                    loadedPPlayer = new PPlayer(playerUUID, groups, fixedParticles, particlesHidden, particlesHiddenSelf);
                } else {
                    loadedPPlayer = new ConsolePPlayer(groups, fixedParticles);
                }

                this.sync(() -> {
                    this.rosePlugin.getManager(ParticleManager.class).addPPlayer(loadedPPlayer);
                    callback.accept(loadedPPlayer);
                });
            });
        };

        if (!Bukkit.isPrimaryThread()) {
            this.async(task);
        } else {
            task.run();
        }
    }

    /**
     * Loads all PPlayers from the database that own FixedParticleEffects
     */
    public void loadFixedEffects() {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            String query = "SELECT DISTINCT owner_uuid FROM " + this.getTablePrefix() + "fixed";
            List<UUID> uuids = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                ResultSet result = statement.executeQuery();
                while (result.next()) {
                    UUID playerUUID = UUID.fromString(result.getString("owner_uuid"));
                    uuids.add(playerUUID);
                }
            }
            for (UUID uuid : uuids)
                this.getPPlayer(uuid, (pplayer) -> { });
        }));
    }

    /**
     * Updates the particles_hidden setting in the database and for the PPlayer
     *
     * @param playerUUID The player to hide PlayerParticles from
     * @param particlesHidden True if the particles should be hidden, otherwise False
     */
    public void updateSettingParticlesHidden(UUID playerUUID, boolean particlesHidden) {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            String updateQuery = "UPDATE " + this.getTablePrefix() + "settings SET particles_hidden = ? WHERE player_uuid = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setBoolean(1, particlesHidden);
                updateStatement.setString(2, playerUUID.toString());

                updateStatement.executeUpdate();
            }
        }));
    }

    /**
     * Updates the particles_hidden_self setting in the database and for the PPlayer
     *
     * @param playerUUID The player to hide their own PlayerParticles from
     * @param particlesHidden True if the particles should be hidden, otherwise False
     */
    public void updateSettingParticlesHiddenSelf(UUID playerUUID, boolean particlesHidden) {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            String updateQuery = "UPDATE " + this.getTablePrefix() + "settings SET particles_hidden_self = ? WHERE player_uuid = ?";
            try (PreparedStatement updateStatement = connection.prepareStatement(updateQuery)) {
                updateStatement.setBoolean(1, particlesHidden);
                updateStatement.setString(2, playerUUID.toString());

                updateStatement.executeUpdate();
            }
        }));
    }

    /**
     * Saves a ParticleGroup. If it already exists, update it. If it's empty, delete it.
     *
     * @param playerUUID The owner of the group
     * @param group The group to create/update
     */
    public void saveParticleGroup(UUID playerUUID, ParticleGroup group) {
        if (group.getParticles().isEmpty() && !group.getName().equals(ParticleGroup.DEFAULT_NAME)) {
            this.removeParticleGroup(playerUUID, group.getName());
            return;
        }

        this.async(() -> this.databaseConnector.connect((connection) -> {
            String groupUUID;
            boolean existingGroup;

            String groupUUIDQuery = "SELECT uuid FROM " + this.getTablePrefix() + "group WHERE owner_uuid = ? AND name = ?";
            try (PreparedStatement statement = connection.prepareStatement(groupUUIDQuery)) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, group.getName());

                ResultSet result = statement.executeQuery();
                if (result.next()) { // Clear out particles from existing group
                    groupUUID = result.getString("uuid");
                    existingGroup = true;
                } else { // Create new group
                    groupUUID = UUID.randomUUID().toString();
                    existingGroup = false;
                }
            }

            if (existingGroup) {
                String particlesDeleteQuery = "DELETE FROM " + this.getTablePrefix() + "particle WHERE group_uuid = ?";
                try (PreparedStatement particlesDeleteStatement = connection.prepareStatement(particlesDeleteQuery)) {
                    particlesDeleteStatement.setString(1, groupUUID);
                    particlesDeleteStatement.executeUpdate();
                }
            } else {
                String groupCreateQuery = "INSERT INTO " + this.getTablePrefix() + "group (uuid, owner_uuid, name) VALUES (?, ?, ?)";
                try (PreparedStatement groupCreateStatement = connection.prepareStatement(groupCreateQuery)) {
                    groupCreateStatement.setString(1, groupUUID);
                    groupCreateStatement.setString(2, playerUUID.toString());
                    groupCreateStatement.setString(3, group.getName());
                    groupCreateStatement.executeUpdate();
                }
            }

            // Fill group with new particles
            String createParticlesQuery = "INSERT INTO " + this.getTablePrefix() + "particle (uuid, group_uuid, id, effect, style, item_material, block_material, note, r, g, b, a, r_end, g_end, b_end, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement particlesStatement = connection.prepareStatement(createParticlesQuery)) {
                for (ParticlePair particle : group.getParticles().values()) {
                    particlesStatement.setString(1, UUID.randomUUID().toString());
                    particlesStatement.setString(2, groupUUID);
                    particlesStatement.setInt(3, particle.getId());
                    particlesStatement.setString(4, particle.getEffect().getInternalName());
                    particlesStatement.setString(5, particle.getStyle().getInternalName());
                    particlesStatement.setString(6, particle.getItemMaterial().name());
                    particlesStatement.setString(7, particle.getBlockMaterial().name());
                    particlesStatement.setInt(8, particle.getNoteColor().getNote());
                    particlesStatement.setInt(9, particle.getColor().getRed());
                    particlesStatement.setInt(10, particle.getColor().getGreen());
                    particlesStatement.setInt(11, particle.getColor().getBlue());
                    ParticleColor color = particle.getColor();
                    int alpha = color instanceof TransparentColor ? ((TransparentColor) color).getAlpha() : 255;
                    particlesStatement.setInt(12, alpha);
                    particlesStatement.setInt(13, particle.getColorTransition().getEndColor().getRed());
                    particlesStatement.setInt(14, particle.getColorTransition().getEndColor().getGreen());
                    particlesStatement.setInt(15, particle.getColorTransition().getEndColor().getBlue());
                    particlesStatement.setInt(16, particle.getVibration().getDuration());
                    particlesStatement.addBatch();
                }

                particlesStatement.executeBatch();
            }
        }));
    }

    /**
     * Removes a ParticleGroup
     *
     * @param playerUUID The owner of the group
     * @param groupName The group to remove
     */
    public void removeParticleGroup(UUID playerUUID, String groupName) {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            String groupQuery = "SELECT * FROM " + this.getTablePrefix() + "group WHERE owner_uuid = ? AND name = ?";
            String particleDeleteQuery = "DELETE FROM " + this.getTablePrefix() + "particle WHERE group_uuid = ?";
            String groupDeleteQuery = "DELETE FROM " + this.getTablePrefix() + "group WHERE uuid = ?";

            // Execute group uuid query
            String groupUUID = null;
            try (PreparedStatement statement = connection.prepareStatement(groupQuery)) {
                statement.setString(1, playerUUID.toString());
                statement.setString(2, groupName);

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
    }

    /**
     * Attempts to reset the active particle group for the given player name.
     * This works even if the player is offline.
     *
     * @param playerName The name of the player to reset the active particle group for
     * @param callback The callback to execute when finished
     */
    public void resetActiveParticleGroup(String playerName, Consumer<Boolean> callback) {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);

            String query = "DELETE FROM " + this.getTablePrefix() + "particle WHERE group_uuid IN (SELECT uuid FROM " + this.getTablePrefix() + "group WHERE owner_uuid = ? AND name = ?)";
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, offlinePlayer.getUniqueId().toString());
                statement.setString(2, ParticleGroup.DEFAULT_NAME);
                callback.accept(statement.execute());
            }
        }));
    }

    /**
     * Saves a fixed effect to save data
     * Does not perform a check to see if a fixed effect with this id already exists
     *
     * @param fixedEffect The fixed effect to save
     */
    public void saveFixedEffect(FixedParticleEffect fixedEffect) {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            String particleUUID = UUID.randomUUID().toString();

            String particleQuery = "INSERT INTO " + this.getTablePrefix() + "particle (uuid, id, effect, style, item_material, block_material, note, r, g, b, a, r_end, g_end, b_end, duration) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(particleQuery)) {
                ParticlePair particle = fixedEffect.getParticlePair();
                statement.setString(1, particleUUID);
                statement.setInt(2, fixedEffect.getId());
                statement.setString(3, particle.getEffect().getInternalName());
                statement.setString(4, particle.getStyle().getInternalName());
                statement.setString(5, particle.getItemMaterial().name());
                statement.setString(6, particle.getBlockMaterial().name());
                statement.setInt(7, particle.getNoteColor().getNote());
                statement.setInt(8, particle.getColor().getRed());
                statement.setInt(9, particle.getColor().getGreen());
                statement.setInt(10, particle.getColor().getBlue());
                ParticleColor color = particle.getColor();
                int alpha = color instanceof TransparentColor ? ((TransparentColor) color).getAlpha() : 255;
                statement.setInt(11, alpha);
                statement.setInt(12, particle.getColorTransition().getEndColor().getRed());
                statement.setInt(13, particle.getColorTransition().getEndColor().getGreen());
                statement.setInt(14, particle.getColorTransition().getEndColor().getBlue());
                statement.setInt(15, particle.getVibration().getDuration());
                statement.executeUpdate();
            }

            String fixedEffectQuery = "INSERT INTO " + this.getTablePrefix() + "fixed (owner_uuid, id, particle_uuid, world, xPos, yPos, zPos, yaw, pitch) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(fixedEffectQuery)) {
                statement.setString(1, fixedEffect.getOwnerUniqueId().toString());
                statement.setInt(2, fixedEffect.getId());
                statement.setString(3, particleUUID);
                statement.setString(4, fixedEffect.getLocation().getWorld().getName());
                statement.setDouble(5, fixedEffect.getLocation().getX());
                statement.setDouble(6, fixedEffect.getLocation().getY());
                statement.setDouble(7, fixedEffect.getLocation().getZ());
                statement.setDouble(8, fixedEffect.getLocation().getYaw());
                statement.setDouble(9, fixedEffect.getLocation().getPitch());
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Updates a fixed effect's particle values
     *
     * @param fixedEffect The fixed effect to update
     */
    public void updateFixedEffect(FixedParticleEffect fixedEffect) {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            // Update fixed effect
            String fixedEffectQuery = "UPDATE " + this.getTablePrefix() + "fixed SET world = ?, xPos = ?, yPos = ?, zPos = ?, yaw = ?, pitch = ? WHERE owner_uuid = ? AND id = ?";
            try (PreparedStatement statement = connection.prepareStatement(fixedEffectQuery)) {
                Location location = fixedEffect.getLocation();
                statement.setString(1, location.getWorld().getName());
                statement.setDouble(2, location.getX());
                statement.setDouble(3, location.getY());
                statement.setDouble(4, location.getZ());
                statement.setDouble(5, location.getYaw());
                statement.setDouble(6, location.getPitch());
                statement.setString(7, fixedEffect.getOwnerUniqueId().toString());
                statement.setInt(8, fixedEffect.getId());
                statement.executeUpdate();
            }

            // Update particle
            String particleUpdateQuery = "UPDATE " + this.getTablePrefix() + "particle " +
                                         "SET effect = ?, style = ?, item_material = ?, block_material = ?, note = ?, r = ?, g = ?, b = ?, r_end = ?, g_end = ?, b_end = ?, duration = ? " +
                                         "WHERE uuid = (SELECT particle_uuid FROM " + this.getTablePrefix() + "fixed WHERE owner_uuid = ? AND id = ?)";
            try (PreparedStatement statement = connection.prepareStatement(particleUpdateQuery)) {
                ParticlePair particle = fixedEffect.getParticlePair();
                statement.setString(1, particle.getEffect().getInternalName());
                statement.setString(2, particle.getStyle().getInternalName());
                statement.setString(3, particle.getItemMaterial().name());
                statement.setString(4, particle.getBlockMaterial().name());
                statement.setInt(5, particle.getNoteColor().getNote());
                statement.setInt(6, particle.getColor().getRed());
                statement.setInt(7, particle.getColor().getGreen());
                statement.setInt(8, particle.getColor().getBlue());
                statement.setInt(9, particle.getColorTransition().getEndColor().getRed());
                statement.setInt(10, particle.getColorTransition().getEndColor().getGreen());
                statement.setInt(11, particle.getColorTransition().getEndColor().getBlue());
                statement.setInt(12, particle.getVibration().getDuration());
                statement.setString(13, fixedEffect.getOwnerUniqueId().toString());
                statement.setInt(14, fixedEffect.getId());
                statement.executeUpdate();
            }
        }));
    }

    /**
     * Deletes a fixed effect from save data
     * Does not perform a check to see if a fixed effect with this id already exists
     *
     * @param playerUUID The player who owns the effect
     * @param id The id of the effect to remove
     */
    public void removeFixedEffect(UUID playerUUID, int id) {
        this.async(() -> this.databaseConnector.connect((connection) -> {
            String particleUUID = null;

            String particleUUIDQuery = "SELECT particle_uuid FROM " + this.getTablePrefix() + "fixed WHERE owner_uuid = ? AND id = ?";
            try (PreparedStatement statement = connection.prepareStatement(particleUUIDQuery)) {
                statement.setString(1, playerUUID.toString());
                statement.setInt(2, id);

                ResultSet result = statement.executeQuery();
                if (result.next()) {
                    particleUUID = result.getString("particle_uuid");
                }
            }

            String particleDeleteQuery = "DELETE FROM " + this.getTablePrefix() + "particle WHERE uuid = ?";
            try (PreparedStatement statement = connection.prepareStatement(particleDeleteQuery)) {
                statement.setString(1, particleUUID);

                statement.executeUpdate();
            }

            String fixedEffectDeleteQuery = "DELETE FROM " + this.getTablePrefix() + "fixed WHERE owner_uuid = ? AND id = ?";
            try (PreparedStatement statement = connection.prepareStatement(fixedEffectDeleteQuery)) {
                statement.setString(1, playerUUID.toString());
                statement.setInt(2, id);

                statement.executeUpdate();
            }
        }));
    }

    /**
     * Asynchronizes the callback with its own thread unless it is already not on the main thread
     *
     * @param asyncCallback The callback to run on a separate thread
     */
    private void async(Runnable asyncCallback) {
        this.rosePlugin.getScheduler().runTaskAsync(asyncCallback);
    }

    /**
     * Synchronizes the callback with the main thread
     *
     * @param syncCallback The callback to run on the main thread
     */
    private void sync(Runnable syncCallback) {
        this.rosePlugin.getScheduler().runTask(syncCallback);
    }

    @Override
    public List<Supplier<? extends DataMigration>> getDataMigrations() {
        return Arrays.asList(
                _1_InitialMigration::new,
                _2_Add_Data_Columns::new,
                _3_Add_Setting_Toggle_Self_Column::new,
                _4_Add_Fixed_Effect_Yaw_Pitch_Columns::new,
                _5_Add_Data_Transparency_Column::new
        );
    }

}
