package dev.esophose.playerparticles.database;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class DataMigration {

    private final int revision;

    public DataMigration(int revision) {
        this.revision = revision;
    }

    /**
     * Migrates the database to this migration stage
     *
     * @param connector The connector for the database
     * @param connection The connection to the database
     * @param tablePrefix The prefix of the database
     * @throws SQLException Any error that occurs during the SQL execution
     */
    public abstract void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException;

    /**
     * @return the revision number of this migration
     */
    public int getRevision() {
        return this.revision;
    }

}
