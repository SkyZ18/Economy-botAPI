package org.economy.config;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Statement;

public class SQLFileReader {

    public void runScript(Connection connection, String path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            String line;
            StringBuilder sb = new StringBuilder();

            Statement stmt = connection.createStatement();

            while ((line = reader.readLine()) != null) {
                if (line.trim().startsWith("--") || line.trim().isEmpty()) {
                    continue;
                }
                sb.append(line);

                if (line.trim().endsWith(";")) {
                    stmt.execute(sb.toString());
                    sb.setLength(0);
                }
            }
            reader.close();
            stmt.close();

            System.out.println("\nSQL Script ran successfully");
        } catch (Exception e) {
            System.out.println("Error accoured: " + e);
        }
    }

}
