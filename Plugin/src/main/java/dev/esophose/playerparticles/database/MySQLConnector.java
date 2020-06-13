package dev.esophose.playerparticles.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import java.sql.Connection;
import java.sql.SQLException;
import org.bukkit.plugin.Plugin;

public class MySQLConnector implements DatabaseConnector {

    private final Plugin plugin;
    private HikariDataSource hikari;
    private boolean initializedSuccessfully;

    public MySQLConnector(Plugin plugin, String hostname, int port, String database, String username, String password, boolean useSSL) {
        this.plugin = plugin;

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://" + hostname + ":" + port + "/" + database + "?useSSL=" + useSSL);
        config.setUsername(username);
        config.setPassword(password);
        config.setMaximumPoolSize(Setting.MYSQL_CONNECTION_POOL_SIZE.getInt());

        try {
            this.hikari = new HikariDataSource(config);
            this.initializedSuccessfully = true;
        } catch (Exception ex) {
            this.initializedSuccessfully = false;
        }
    }

    public boolean isInitialized() {
        return this.initializedSuccessfully;
    }

    public void closeConnection() {
        this.hikari.close();
    }

    public void connect(ConnectionCallback callback) {
        try (Connection connection = this.hikari.getConnection()) {
            callback.accept(connection);
        } catch (SQLException ex) {
            this.plugin.getLogger().severe("An error occurred executing a MySQL query: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
