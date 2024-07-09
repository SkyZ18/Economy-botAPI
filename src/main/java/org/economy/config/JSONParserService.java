package org.economy.config;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;

public class JSONParserService {

    private static final String path = "src/main/resources/config.json";
    JSONParser parser = new JSONParser();

    public JSONObject readJSON() {
        try {
            return (JSONObject) parser.parse(new FileReader(path));
        } catch (IOException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public String iterator(JSONObject obj, String searchedValue) {
        for (Object o : obj.keySet()) {
            if (o.equals(searchedValue)) {
                return obj.get(o).toString();

            } else if (obj.get(o) instanceof org.json.simple.JSONObject) {
                String value = iterator((JSONObject) obj.get(o), searchedValue);
                if (value != null) {
                    return value;
                }
            }
        }

        return null;
    }
}
