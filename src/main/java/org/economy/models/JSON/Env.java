package org.economy.models.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class Env {
    private String pathToSql;
    private Db db;
}

