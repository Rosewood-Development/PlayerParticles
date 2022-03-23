package dev.esophose.playerparticles.database.migrations;

import dev.esophose.playerparticles.database.DataMigration;
import dev.esophose.playerparticles.database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _4_Add_Fixed_Effect_Yaw_Pitch_Columns extends DataMigration {

    public _4_Add_Fixed_Effect_Yaw_Pitch_Columns() {
        super(4);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE " + tablePrefix + "fixed ADD COLUMN yaw DOUBLE DEFAULT 0");
            statement.executeUpdate("ALTER TABLE " + tablePrefix + "fixed ADD COLUMN pitch DOUBLE DEFAULT 0");
        }
    }

}

