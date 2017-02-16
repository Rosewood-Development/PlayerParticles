package com.esophose.playerparticles.library;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Abstract Database class, serves as a base for any connection method (MySQL,
 * SQLite, etc.)
 * 
 * @author -_Husky_-
 * @author tips48
 */
public abstract class Database {

	protected Connection connection;

	/**
	 * Creates a new Database
	 *
	 */
	protected Database() {
		this.connection = null;
	}

	/**
	 * Opens a connection with the database
	 * 
	 * @return Opened connection
	 * @throws SQLException if the connection can not be opened
	 * @throws ClassNotFoundException if the driver cannot be found
	 */
	public abstract Connection openConnection() throws SQLException, ClassNotFoundException;

	/**
	 * Checks if a connection is open with the database
	 * 
	 * @return true if the connection is open
	 * @throws SQLException if the connection cannot be checked
	 */
	public boolean checkConnection() throws SQLException {
		return connection != null && !connection.isClosed();
	}

	/**
	 * Gets the connection with the database
	 * 
	 * @return Connection with the database, null if none
	 */
	public Connection getConnection() {
		return connection;
	}

	/**
	 * Closes the connection with the database
	 * 
	 * @return true if successful
	 * @throws SQLException if the connection cannot be closed
	 */
	public boolean closeConnection() throws SQLException {
		if (connection == null) {
			return false;
		}
		connection.close();
		return true;
	}

	/**
	 * Executes a SQL Query<br>
	 * 
	 * If the connection is closed, it will be opened
	 * 
	 * @param query Query to be run
	 * @return the results of the query
	 * @throws SQLException If the query cannot be executed
	 * @throws ClassNotFoundException If the driver cannot be found; see
	 *             {@link #openConnection()}
	 */
	public ResultSet querySQL(String query) throws SQLException, ClassNotFoundException {
		if (!checkConnection()) {
			openConnection();
		}

		Statement statement = connection.createStatement();

		ResultSet result = statement.executeQuery(query);

		return result;
	}

	/**
	 * Executes an Update SQL Query<br>
	 * See {@link java.sql.Statement#executeUpdate(String)}<br>
	 * If the connection is closed, it will be opened
	 * 
	 * Executes multiple updates broken up by semi-colons
	 * 
	 * @param query Query to be run
	 * @return Result Code, see {@link java.sql.Statement#executeUpdate(String)}
	 * @throws SQLException If the query cannot be executed
	 * @throws ClassNotFoundException If the driver cannot be found; see
	 *             {@link #openConnection()}
	 */
	public int updateSQL(String query) throws SQLException, ClassNotFoundException {
		if (!checkConnection()) {
			openConnection();
		}

		Statement statement = connection.createStatement();
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
	}
}
