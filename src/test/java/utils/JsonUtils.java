package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonUtils {
    private static final ObjectMapper mapper = new ObjectMapper();

    public static JsonNode parseJson(String json) {
        try {
            return mapper.readTree(json);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getStringValue(JsonNode object, String... fields) { // Перебор вложенных полей.
        JsonNode node = object;       // field1, field2, ...
        for (String field : fields) { // С каждой иттерацией все глубже и глубже по дереву.
            if (node == null || !node.isObject()) return null;
            node  = node.get(field);
        }
        return node.asText();
    }
}
