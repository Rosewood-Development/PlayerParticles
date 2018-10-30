package com.esophose.playerparticles.database;

import java.sql.Connection;
import java.sql.SQLException;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.SettingManager.PSetting;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class MySqlDatabaseConnector implements DatabaseConnector {

    private HikariDataSource hikari;
    private boolean initializedSuccessfully = false;

    public MySqlDatabaseConnector() {
        String hostname = PSetting.DATABASE_HOSTNAME.getString();
        String port = PSetting.DATABASE_PORT.getString();
        String database = PSetting.DATABASE_NAME.getString();
        String user = PSetting.DATABASE_USER_NAME.getString();
        String pass = PSetting.DATABASE_USER_PASSWORD.getString();

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
        try (Connection connection = hikari.getConnection()) {
            callback.execute(connection);
        } catch (SQLException ex) {
            PlayerParticles.getPlugin().getLogger().severe("An error occurred retrieving a mysql database connection: " + ex.getMessage());
        }
    }

}
