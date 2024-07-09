package org.economy;

import org.economy.config.JSONParserService;
import org.economy.config.PasswordDecoder;
import org.economy.config.SQLFileReader;
import org.economy.connection.DatabaseConnection;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class Main {

    private static final JSONParserService parser = new JSONParserService();
    private static final SQLFileReader sqlReader = new SQLFileReader();
    private static final PasswordDecoder decoder = new PasswordDecoder();
    private static final JSONObject obj = parser.readJSON();

    public static void main(String[] args) {
        String url =
                "jdbc:mariadb://"
                        + parser.iterator(obj, "maria-db-host")
                        + ":"
                        + parser.iterator(obj, "maria-db-port")
                        + "/"
                        + parser.iterator(obj, "maria-db-database");

        DatabaseConnection dbConn = new DatabaseConnection(
                url,
                parser.iterator(obj, "maria-db-user"),
                decoder.decodePassword(parser.iterator(obj, "maria-db-password"))
        );

        try (Connection connection = dbConn.openConn()) {
            DatabaseMetaData metaData = connection.getMetaData();

            System.out.println("Connection opened successfully to database: " + connection.getCatalog()
                    + "\n--------------------------------------------------"
                    + "\nMETADATA:"
                    + "\n Application name: " + parser.iterator(obj, "name")
                    + "\n Application description: " + parser.iterator(obj, "description")
                    + "\n Driver: " + metaData.getDriverName()
                    + "\n Driver-Version: " + metaData.getDriverVersion()
                    + "\n Database-URL: " + metaData.getURL()
                    + "\n Database-Version: " + metaData.getDatabaseMajorVersion()
                    + "\n Logged Username: " + metaData.getUserName()
                    + "\n--------------------------------------------------"
            );

            sqlReader.runScript(connection, parser.iterator(obj, "path-to-sql"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}