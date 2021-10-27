package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.database.DataMigration;
import dev.esophose.playerparticles.database.DatabaseConnector;
import dev.esophose.playerparticles.database.SQLiteConnector;
import dev.esophose.playerparticles.database.migrations._1_InitialMigration;
import dev.esophose.playerparticles.database.migrations._2_Add_Data_Columns;
import dev.esophose.playerparticles.database.migrations._3_Add_Setting_Toggle_Self_Column;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class DataMigrationManager extends Manager {

    private final List<DataMigration> migrations;

    public DataMigrationManager(PlayerParticles playerParticles) {
        super(playerParticles);

        this.migrations = Arrays.asList(
                new _1_InitialMigration(),
                new _2_Add_Data_Columns(),
                new _3_Add_Setting_Toggle_Self_Column()
        );
    }

    @Override
    public void reload() {
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        DatabaseConnector databaseConnector = dataManager.getDatabaseConnector();

        databaseConnector.connect((connection -> {
            int currentMigration = -1;
            boolean migrationsExist;

            String query;
            if (databaseConnector instanceof SQLiteConnector) {
                query = "SELECT 1 FROM sqlite_master WHERE type = 'table' AND name = ?";
            } else {
                query = "SHOW TABLES LIKE ?";
            }

            try (PreparedStatement statement = connection.prepareStatement(query)) {
                statement.setString(1, this.getMigrationsTableName());
                migrationsExist = statement.executeQuery().next();
            }

            if (!migrationsExist) {
                // No migration table exists, create one
                String createTable = "CREATE TABLE " + this.getMigrationsTableName() + " (migration_version INT NOT NULL)";
                try (PreparedStatement statement = connection.prepareStatement(createTable)) {
                    statement.execute();
                }

                // Insert primary row into migration table
                String insertRow = "INSERT INTO " + this.getMigrationsTableName() + " VALUES (?)";
                try (PreparedStatement statement = connection.prepareStatement(insertRow)) {
                    statement.setInt(1, -1);
                    statement.execute();
                }
            } else {
                // Grab the current migration version
                String selectVersion = "SELECT migration_version FROM " + this.getMigrationsTableName();
                try (PreparedStatement statement = connection.prepareStatement(selectVersion)) {
                    ResultSet result = statement.executeQuery();
                    result.next();
                    currentMigration = result.getInt("migration_version");
                }
            }

            // Grab required migrations
            int finalCurrentMigration = currentMigration;
            List<DataMigration> requiredMigrations = this.migrations
                    .stream()
                    .filter(x -> x.getRevision() > finalCurrentMigration)
                    .sorted(Comparator.comparingInt(DataMigration::getRevision))
                    .collect(Collectors.toList());

            // Nothing to migrate, abort
            if (requiredMigrations.isEmpty())
                return;

            // Migrate the data
            for (DataMigration dataMigration : requiredMigrations)
                dataMigration.migrate(databaseConnector, connection, dataManager.getTablePrefix());

            // Set the new current migration to be the highest migrated to
            currentMigration = requiredMigrations
                    .stream()
                    .map(DataMigration::getRevision)
                    .max(Integer::compareTo)
                    .orElse(-1);

            String updateVersion = "UPDATE " + this.getMigrationsTableName() + " SET migration_version = ?";
            try (PreparedStatement statement = connection.prepareStatement(updateVersion)) {
                statement.setInt(1, currentMigration);
                statement.execute();
            }
        }));
    }

    @Override
    public void disable() {

    }

    /**
     * @return the name of the migrations table
     */
    private String getMigrationsTableName() {
        return this.playerParticles.getManager(DataManager.class).getTablePrefix() + "migrations";
    }

}
