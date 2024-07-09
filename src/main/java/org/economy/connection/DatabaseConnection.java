package org.economy.connection;

import lombok.AllArgsConstructor;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.Connection;

@AllArgsConstructor
public class DatabaseConnection {
    String url;
    String user;
    String password;

    public Connection openConn() throws Exception {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        dataSource.setUrl(url);
        dataSource.setUser(user);
        dataSource.setPassword(password);

        return dataSource.getConnection();
    }
}
