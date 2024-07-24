package org.economy;

import com.github.lalyos.jfiglet.FigletFont;
import org.economy.config.JSONParserService;
import org.economy.config.PasswordDecoder;
import org.economy.config.SQLFileReader;
import org.economy.connection.DatabaseConnection;
import org.economy.models.JSON.JSONData;
import org.economy.models.Metadata;
import org.economy.models.ThreadRunner;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class EconomyAPI {

    private static final JSONParserService parser = new JSONParserService();
    private static final SQLFileReader sqlReader = new SQLFileReader();
    private static final PasswordDecoder decoder = new PasswordDecoder();
    private static final JSONData obj = parser.readJSON();

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println(FigletFont.convertOneLine("ECONOMY-API"));

        String url =
                "jdbc:mariadb://"
                        + obj.getEnv().getDb().getMariaDbHost()
                        + obj.getEnv().getDb().getMariaDbPort()
                        + obj.getEnv().getDb().getMariaDbDatabase();

        DatabaseConnection dbConn = new DatabaseConnection(
                url,
                obj.getEnv().getDb().getMariaDbUser(),
                decoder.decodePassword(obj.getEnv().getDb().getMariaDbPassword())
        );

        try {
            Connection connection = dbConn.openConn();
            DatabaseMetaData data = connection.getMetaData();

            Metadata metadata = new Metadata(
                    connection.getCatalog(),
                    obj.getName(),
                    obj.getDescription(),
                    data.getDriverName(),
                    data.getDriverVersion(),
                    data.getURL(),
                    data.getDatabaseMajorVersion(),
                    data.getUserName(),
                    data.getConnection().isClosed()
            );

            System.out.println(metadata.getMetadata());
            sqlReader.runScript(connection, obj.getEnv().getPathToSql());
            System.out.println("\nAPI running");

            ThreadRunner.run(connection, dbConn);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}