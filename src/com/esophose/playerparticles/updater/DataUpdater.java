package com.esophose.playerparticles.updater;

import java.sql.Statement;

import com.esophose.playerparticles.PlayerParticles;

/**
 * This class handles updating the SQLite or MySQL data from older versions to the current version
 * Everything is purposely done on the main thread to prevent the plugin from starting before all the data is updated
 */
public class DataUpdater {
    
    /**
     * Checks to make sure all the correct database tables exist
     * If they don't, create them
     */
    public static void tryCreateTables() {
        PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            try (Statement createStatement = connection.createStatement()) {
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_settings (player_uuid VARCHAR(36), particles_hidden TINYINT)");
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_particle (uuid VARCHAR(36), group_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(100), style VARCHAR(100), item_material VARCHAR(100), block_material VARCHAR(100), note SMALLINT, r SMALLINT, g SMALLINT, b SMALLINT, PRIMARY KEY(uuid))");
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_group (uuid VARCHAR(36), owner_uuid VARCHAR(36), name VARCHAR(100), PRIMARY KEY(uuid))");
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS pp_fixed (owner_uuid VARCHAR(36), id SMALLINT, particle_uuid VARCHAR(36), world VARCHAR(100), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE, PRIMARY KEY(owner_uuid, id), FOREIGN KEY(particle_uuid) REFERENCES pp_particle(uuid) ON DELETE CASCADE)");
                createStatement.executeBatch();
            }
        });
    }

    /**
     * Updates the plugin data from one version of the plugin to the current version
     * 
     * @param configVersion The old version of the plugin 
     * @param currentVersion The current version of the plugin
     */
    public static void updateData(double configVersion, double currentVersion) {
        if (configVersion == currentVersion) return;
        
        PlayerParticles.getPlugin().getLogger().warning("Starting to update SQLite/MySQL data from " + (configVersion < 5.3 ? "a legacy version" : "v" + configVersion) + " to v" + PlayerParticles.getPlugin().getDescription().getVersion() + ", this may take a while...");
        
        if (configVersion < 5.3) {
            updateFrom_legacy_to_current();
        } else if (configVersion == 5.3) {
            updateFrom_5_3_to_current();
        } else {
            PlayerParticles.getPlugin().getLogger().warning("Found nothing to update.");
        }
        
        PlayerParticles.getPlugin().getLogger().warning("Finished updating SQLite/MySQL data from " + (configVersion < 5.3 ? "a legacy version" : "v" + configVersion) + " to v" + PlayerParticles.getPlugin().getDescription().getVersion());
    }
    
    /**
     * Updates the data from versions older than v5.2
     */
    private static void updateFrom_legacy_to_current() {
        PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            try (Statement statement = connection.createStatement()) {
                statement.addBatch("DROP TABLE IF EXISTS pp_users");
                statement.addBatch("DROP TABLE IF EXISTS pp_fixed");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_item");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_block");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_color");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_note");
                statement.executeBatch();
            }
        });
    }
    
    /**
     * Updates the data from v5.3 to current
     * Note: v5.3 was never officially released
     */
    private static void updateFrom_5_3_to_current() {
        PlayerParticles.getPlugin().getDBConnector().connect((connection) -> {
            // Create player settings table
            try (Statement statement = connection.createStatement()) {
                String updateQuery = "CREATE TABLE IF NOT EXISTS pp_settings (player_uuid VARCHAR(36), particles_hidden TINYINT)";
                statement.executeUpdate(updateQuery);
            }
        });
    }

}
