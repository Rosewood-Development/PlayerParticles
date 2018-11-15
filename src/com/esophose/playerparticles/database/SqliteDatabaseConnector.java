package com.esophose.playerparticles.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.esophose.playerparticles.PlayerParticles;

public class SqliteDatabaseConnector implements DatabaseConnector {

    private final String connectionString;

    public SqliteDatabaseConnector(String directory) {
        this.connectionString = "jdbc:sqlite:" + directory + File.separator + "playerparticles.db";
    }

    public boolean isInitialized() {
        return true; // Always available
    }

    public void closeConnection() {
        // Nothing to do
    }

    public void connect(ConnectionCallback callback) {
        try (Connection connection = DriverManager.getConnection(this.connectionString)) {
            callback.execute(connection);
        } catch (SQLException ex) {
            PlayerParticles.getPlugin().getLogger().severe("An error occurred retrieving an SQLite database connection: " + ex.getMessage());
        }
    }

}
