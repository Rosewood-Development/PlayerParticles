package dev.esophose.playerparticles.database.migrations;

import dev.rosewood.rosegarden.database.DataMigration;
import dev.rosewood.rosegarden.database.DatabaseConnector;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class _5_Add_Data_Transparency_Column extends DataMigration {

    public _5_Add_Data_Transparency_Column() {
        super(5);
    }

    @Override
    public void migrate(DatabaseConnector connector, Connection connection, String tablePrefix) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER TABLE " + tablePrefix + "fixed ADD COLUMN a SMALLINT DEFAULT 255");
            statement.executeUpdate("ALTER TABLE " + tablePrefix + "particle ADD COLUMN a SMALLINT DEFAULT 255");
        }
    }

}

