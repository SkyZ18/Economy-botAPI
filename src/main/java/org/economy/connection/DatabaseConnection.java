package org.economy.connection;

import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;


public class DatabaseConnection {

    public static Connection openConn(String url, String user, String password) throws Exception {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        return dataSource.getConnection();
    }
}
