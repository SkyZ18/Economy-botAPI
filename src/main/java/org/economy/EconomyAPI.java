package org.economy;

import com.github.lalyos.jfiglet.FigletFont;
import org.economy.api.EconomyUserService;
import org.economy.config.JSONParserService;
import org.economy.config.Logger;
import org.economy.config.PasswordDecoder;
import org.economy.config.SQLFileReader;
import org.economy.connection.DatabaseConnection;
import org.economy.models.JSON.JSONData;
import org.economy.models.Metadata;

import java.sql.Connection;
import java.sql.DatabaseMetaData;

public class EconomyAPI {

    private static final JSONParserService parser = new JSONParserService();
    private static final SQLFileReader sqlReader = new SQLFileReader();
    private static final PasswordDecoder decoder = new PasswordDecoder();
    private static final JSONData obj = parser.readJSON();

    public static String url =
            "jdbc:mariadb://"
                    + obj.getEnv().getDb().getMariaDbHost()
                    + obj.getEnv().getDb().getMariaDbPort()
                    + obj.getEnv().getDb().getMariaDbDatabase();
    public static String user = obj.getEnv().getDb().getMariaDbUser();
    public static String password = decoder.decodePassword(obj.getEnv().getDb().getMariaDbPassword());
    public static Connection connection;

    private static final EconomyUserService userService = new EconomyUserService();

    public static void main(String[] args) {
        run();
    }

    public static void run() {
        System.out.println(FigletFont.convertOneLine("ECONOMY-API"));

        try {
            connection = DatabaseConnection.openConn(url,user,password);

            new Thread(() -> {
                try {
                    while (true) {
                        if(connection.isClosed()) {
                            connection = DatabaseConnection.openConn(url,user,password);
                        }
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).start();

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

            Logger.log("API running", Logger.LogType.INFO);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}