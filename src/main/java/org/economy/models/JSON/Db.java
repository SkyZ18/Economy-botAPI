package org.economy.models.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Db {
    private String mariaDbHost;
    private String mariaDbPort;
    private String mariaDbDatabase;
    private String mariaDbUser;
    private String mariaDbPassword;
}
