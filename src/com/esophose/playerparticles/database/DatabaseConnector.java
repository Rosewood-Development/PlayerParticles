package com.esophose.playerparticles.database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DatabaseConnector {

    /**
     * Checks if the connection to the database has been created
     * 
     * @return If the connection is created or not
     */
    public abstract boolean isInitialized();

    /**
     * Closes all open connections to the database
     */
    public abstract void closeConnection();

    /**
     * Executes a callback with a Connection passed and automatically closes it when finished
     * 
     * @param callback The callback to execute once the connection is retrieved
     */
    public abstract void connect(ConnectionCallback callback);

    /**
     * Gets a connection to the database
     * 
     * @return A Connection to the database
     * @throws SQLException If an SQL problem occurs getting the connection
     */
    protected abstract Connection getConnection() throws SQLException;

    /**
     * Allows Lambda expressions to be used to reduce duplicated code for getting connections
     */
    public static interface ConnectionCallback {
        public void execute(Connection connection) throws SQLException;
    }

}
