package dev.esophose.playerparticles.database.migrations;

import dev.esophose.playerparticles.database.DataMigration;
import dev.esophose.playerparticles.database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _3_Add_Setting_Toggle_Self_Column extends DataMigration {

    public _3_Add_Setting_Toggle_Self_Column() {
        super(3);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE " + tablePrefix + "settings ADD COLUMN particles_hidden_self TINYINT DEFAULT 0");
        }
    }

}

