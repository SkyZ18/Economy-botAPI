package org.economy.config;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.Statement;

public class SQLFileReader {

    public void runScript(Connection connection, String path) {
        try (Statement stmt = connection.createStatement()) {

            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("schema.sql");

            if (inputStream == null) {
                throw new IllegalArgumentException("File not found! schema.sql");
            }

            String sqlContent = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            String[] sqlStatements = sqlContent.split(";");

            for (String sql : sqlStatements) {
                if (!sql.trim().isEmpty()) {
                    try {
                        stmt.execute(sql);
                    } catch (Exception e) {
                        Logger.log("Error: " + e, Logger.LogType.ERROR);
                    }
                }
            }

            Logger.log("SQL-Script executed", Logger.LogType.INFO);

        } catch (Exception e) {
            Logger.log("Error: " + e, Logger.LogType.ERROR);
        }
    }
}
