package com.esophose.playerparticles.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import com.esophose.playerparticles.PlayerParticles;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MySqlDatabaseConnector extends DatabaseConnector {

	private HikariDataSource hikari;
    private boolean initializedSuccessfully = false;

    public MySqlDatabaseConnector(FileConfiguration pluginConfig) {
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
    
    public boolean isInitialized() {
        return initializedSuccessfully;
    }
    
    public void closeConnection() {
        hikari.close();
    }
    
    public void connect(ConnectionCallback callback) {
        try (Connection connection = this.getConnection()) {
            callback.execute(connection);
        } catch (SQLException ex) {
            PlayerParticles.getPlugin().getLogger().severe("An error occurred retrieving a mysql database connection: " + ex.getMessage());
        }
    }
    
    protected Connection getConnection() throws SQLException {
    	return hikari.getConnection();
    }
    
}
