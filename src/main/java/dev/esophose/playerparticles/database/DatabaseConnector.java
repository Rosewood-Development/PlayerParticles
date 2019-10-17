package dev.esophose.playerparticles.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface DatabaseConnector {

    /**
     * Checks if the connection to the database has been created
     * 
     * @return If the connection is created or not
     */
    boolean isInitialized();

    /**
     * Closes all open connections to the database
     */
    void closeConnection();

    /**
     * Executes a callback with a Connection passed and automatically closes it when finished
     * 
     * @param callback The callback to execute once the connection is retrieved
     */
    void connect(ConnectionCallback callback);

    /**
     * Allows Lambda expressions to be used to reduce duplicated code for getting connections
     */
    interface ConnectionCallback {
        void execute(Connection connection) throws SQLException;
    }

}
