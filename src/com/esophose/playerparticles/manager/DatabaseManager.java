package com.esophose.playerparticles.manager;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;

import com.esophose.playerparticles.PlayerParticles;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {
    
    private HikariDataSource hikari;
    private boolean initializedSuccessfully = false;

    public DatabaseManager(FileConfiguration pluginConfig) {
        String hostname = pluginConfig.getString("database-hostname");
        String port = pluginConfig.getString("database-port");
        String database = pluginConfig.getString("database-name");
        String user = pluginConfig.getString("database-user-name");
        String pass = pluginConfig.getString("database-user-password");
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database);
        config.setUsername(user);
        config.setPassword(pass);
        config.setMaximumPoolSize(5);
        
        try {
            hikari = new HikariDataSource(config);
            initializedSuccessfully = true;
        } catch (Exception ex) {
            initializedSuccessfully = false;
        }
    }
    
    /**
     * Checks if the connection to the database has been created
     * 
     * @return If the connection is created or not
     */
    public boolean isInitialized() {
        return initializedSuccessfully;
    }
    
    /**
     * Closes all connections to the database
     */
    public void closeConnection() {
        hikari.close();
    }
    
    /**
     * Executes a callback with a Connection passed and automatically closes it
     * 
     * @param callback The callback to execute once the connection is retrieved
     */
    public void connect(ConnectionCallback callback) {
        try (Connection connection = hikari.getConnection()) {
            callback.execute(connection);
        } catch (SQLException ex) {
            PlayerParticles.getPlugin().getLogger().warning("An error occurred retrieving a database connection: " + ex.getMessage());
        }
    }
    
    /**
     * Executes an update statement and cleans up all resources
     * 
     * @param query The update statement to run
     * @return An int with the status of the first statement in the query
     * @throws SQLException If an SQL problem occurs executing the statement
     */
    public int updateSQL(String query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        
        try {
            connection = hikari.getConnection();
            statement = connection.createStatement();
            
            int[] results;

            if (query.indexOf(';') != -1) {
                String[] queries = query.split(";");
                for (String q : queries) {
                    statement.addBatch(q);
                }

                results = statement.executeBatch();
            } else {
                results = new int[] { statement.executeUpdate(query) };
            }

            statement.close();

            return results[0];
        } catch (SQLException ex) {
            throw ex;
        } finally {
            try { if (connection != null) connection.close(); } catch (Exception ex) { };
            try { if (statement != null) statement.close(); } catch (Exception ex) { };
        }
    }
    
    /**
     * Allows Lambda expressions to be used to reduce duplicated code for getting connections
     */
    public static interface ConnectionCallback {
        public void execute(Connection connection) throws SQLException;
    }

}
