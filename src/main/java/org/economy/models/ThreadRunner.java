package org.economy.models;

import org.economy.connection.DatabaseConnection;

import java.sql.Connection;

public class ThreadRunner {

    public static Connection conn;

    public static void run(Connection connection, DatabaseConnection dbConn) {
        new Thread(() -> {
            conn = connection;
            while(true) {
                try {
                    if(dbConn.healthCheck(connection)) {
                        System.out.println("Reconnect to db");
                        conn = dbConn.openConn();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

}
