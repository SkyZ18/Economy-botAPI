package org.economy.models;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Metadata {

    private String database;
    private String appName;
    private String appDescription;
    private String driver;
    private String driverVersion;
    private String databaseURL;
    private int databaseVersion;
    private String loggedUser;
    private boolean status;

    public String getMetadata() {
        String status = (!this.status) ? "ONLINE" : "OFFLINE";

        return "Connection opened successfully to database: " + database
                + "\n---------------------[INFO]-----------------------"
                + "\nMETADATA:"
                + "\n Application name: " + appName
                + "\n Application description: " + appDescription
                + "\n Driver: " + driver
                + "\n Driver-Version: " + driverVersion
                + "\n Database-URL: " + databaseURL
                + "\n Database-Version: " + databaseVersion
                + "\n Logged Username: " + loggedUser
                + "\n Database status: " + status
                + "\n--------------------------------------------------";
    }
}
