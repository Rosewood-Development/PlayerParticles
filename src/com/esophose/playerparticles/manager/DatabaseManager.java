package com.esophose.playerparticles.manager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.bukkit.configuration.file.FileConfiguration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseManager {
    
    private HikariDataSource hikari;

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
        config.setMaximumPoolSize(10);
        
        hikari = new HikariDataSource(config);
    }
    
    public void closeConnection() {
        hikari.close();
    }
    
    public ResultSet querySQL(String query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet result = null;
        
        try {
            connection = hikari.getConnection();
            statement = connection.createStatement();
            result = statement.executeQuery(query);
        } catch (SQLException ex) {
            throw ex;
        } finally {
            try { if (connection != null) connection.close(); } catch (Exception ex) { };
            try { if (statement != null) statement.close(); } catch (Exception ex) { };
        }
        
        return result;
    }
    
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

}
