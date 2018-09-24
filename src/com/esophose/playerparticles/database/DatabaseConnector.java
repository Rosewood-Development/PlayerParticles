package com.esophose.playerparticles.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

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
     * Executes an update statement and cleans up all resources
     * Allows batched statements separated by semicolons
     * 
     * @param query The update statement to run
     * @return An int with the status of the first statement in the query
     * @throws SQLException If an SQL problem occurs executing the statement
     */
	public int updateSQL(String query) throws SQLException {
		Connection connection = null;
        Statement statement = null;
        
        try {
            connection = this.getConnection();
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
