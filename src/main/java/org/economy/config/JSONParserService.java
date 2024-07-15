package org.economy.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.economy.models.JSON.JSONData;

import java.io.InputStream;

public class JSONParserService {

    private static final String path = "config.json";

    public JSONData readJSON() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream iS = getClass().getClassLoader().getResourceAsStream(path);
            return mapper.readValue(iS, JSONData.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
