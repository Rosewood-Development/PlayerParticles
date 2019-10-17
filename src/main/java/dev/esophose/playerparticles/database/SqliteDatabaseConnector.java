package dev.esophose.playerparticles.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import dev.esophose.playerparticles.PlayerParticles;

public class SqliteDatabaseConnector implements DatabaseConnector {

    private final String connectionString;
    private Connection connection;

    public SqliteDatabaseConnector(String directory) {
        this.connectionString = "jdbc:sqlite:" + directory + File.separator + "playerparticles.db";
    }

    public boolean isInitialized() {
        return true; // Always available
    }

    public void closeConnection() {
        try {
            if (this.connection != null) {
                this.connection.close();
            }
        } catch (SQLException ex) {
            PlayerParticles.getPlugin().getLogger().severe("An error occurred closing the SQLite database connection: " + ex.getMessage());
        }
    }

    public void connect(ConnectionCallback callback) {
        if (this.connection == null) {
            try {
                this.connection = DriverManager.getConnection(this.connectionString);
            } catch (SQLException ex) {
                PlayerParticles.getPlugin().getLogger().severe("An error occurred retrieving the SQLite database connection: " + ex.getMessage());
            }
        }
        
        try {
            callback.execute(this.connection);
        } catch (SQLException ex) {
            PlayerParticles.getPlugin().getLogger().severe("An error occurred retrieving the SQLite database connection: " + ex.getMessage());
        }
    }

}
