package dev.esophose.playerparticles.database.migrations;

import dev.esophose.playerparticles.database.DataMigration;
import dev.esophose.playerparticles.database.DatabaseConnector;
import dev.esophose.playerparticles.database.SQLiteConnector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class _1_InitialMigration extends DataMigration {

    public _1_InitialMigration() {
        super(1);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        // Check if we are still running legacy data
        boolean hasLegacy;
        String legacyQuery;
        if (connector instanceof SQLiteConnector) {
            legacyQuery = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";
        } else {
            legacyQuery = "SHOW TABLES LIKE ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(legacyQuery)) {
            statement.setString(1, "pp_data_note");
            hasLegacy = statement.executeQuery().next();
        }

        if (hasLegacy) {
            // Drop legacy tables
            try (Statement statement = connection.createStatement()) {
                statement.addBatch("DROP TABLE IF EXISTS pp_users");
                statement.addBatch("DROP TABLE IF EXISTS pp_fixed");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_item");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_block");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_color");
                statement.addBatch("DROP TABLE IF EXISTS pp_data_note");
                statement.executeBatch();
            }
        }

        // Check if we are still running legacy data
        boolean hasTables;
        String tablesQuery;
        if (connector instanceof SQLiteConnector) {
            tablesQuery = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";
        } else {
            tablesQuery = "SHOW TABLES LIKE ?";
        }

        try (PreparedStatement statement = connection.prepareStatement(tablesQuery)) {
            statement.setString(1, "pp_settings");
            hasTables = statement.executeQuery().next();
        }

        // Rename the tables if they aleady exist
        if (hasTables) {
            try (Statement statement = connection.createStatement()) {
                if (connector instanceof SQLiteConnector) {
                    statement.addBatch("ALTER TABLE pp_settings RENAME TO " + tablePrefix + "settings");
                    statement.addBatch("ALTER TABLE pp_particle RENAME TO " + tablePrefix + "particle");
                    statement.addBatch("ALTER TABLE pp_group RENAME TO " + tablePrefix + "group");
                    statement.addBatch("ALTER TABLE pp_fixed RENAME TO " + tablePrefix + "fixed");
                } else {
                    statement.addBatch("RENAME TABLE pp_settings TO " + tablePrefix + "settings");
                    statement.addBatch("RENAME TABLE pp_particle TO " + tablePrefix + "particle");
                    statement.addBatch("RENAME TABLE pp_group TO " + tablePrefix + "group");
                    statement.addBatch("RENAME TABLE pp_fixed TO " + tablePrefix + "fixed");
                }

                statement.executeBatch();
            }
        } else { // Otherwise create them
            try (Statement createStatement = connection.createStatement()) {
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS " + tablePrefix + "settings (player_uuid VARCHAR(36), particles_hidden TINYINT)");
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS " + tablePrefix + "particle (uuid VARCHAR(36), group_uuid VARCHAR(36), id SMALLINT, effect VARCHAR(100), style VARCHAR(100), item_material VARCHAR(100), block_material VARCHAR(100), note SMALLINT, r SMALLINT, g SMALLINT, b SMALLINT, PRIMARY KEY(uuid))");
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS " + tablePrefix + "group (uuid VARCHAR(36), owner_uuid VARCHAR(36), name VARCHAR(100), PRIMARY KEY(uuid))");
                createStatement.addBatch("CREATE TABLE IF NOT EXISTS " + tablePrefix + "fixed (owner_uuid VARCHAR(36), id SMALLINT, particle_uuid VARCHAR(36), world VARCHAR(100), xPos DOUBLE, yPos DOUBLE, zPos DOUBLE, PRIMARY KEY(owner_uuid, id), FOREIGN KEY(particle_uuid) REFERENCES " + tablePrefix + "particle(uuid) ON DELETE CASCADE)");
                createStatement.executeBatch();
            }
        }

    }

}

