package dev.esophose.playerparticles.database.migrations;

import dev.esophose.playerparticles.database.DataMigration;
import dev.esophose.playerparticles.database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _2_Add_Data_Columns extends DataMigration {

    public _2_Add_Data_Columns() {
        super(2);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.addBatch("ALTER TABLE " + tablePrefix + "particle ADD COLUMN r_end SMALLINT DEFAULT 0");
            statement.addBatch("ALTER TABLE " + tablePrefix + "particle ADD COLUMN g_end SMALLINT DEFAULT 0");
            statement.addBatch("ALTER TABLE " + tablePrefix + "particle ADD COLUMN b_end SMALLINT DEFAULT 0");
            statement.addBatch("ALTER TABLE " + tablePrefix + "particle ADD COLUMN duration INT DEFAULT 20");
            statement.executeBatch();
        }
    }

}

