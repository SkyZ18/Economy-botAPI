package org.economy.models.JSON;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter
@Setter
public class JSONData {
    private String name;
    private String description;
    private Env env;
}

