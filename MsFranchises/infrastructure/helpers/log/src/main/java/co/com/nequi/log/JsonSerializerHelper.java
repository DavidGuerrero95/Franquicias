package co.com.nequi.log;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public class JsonSerializerHelper {

    private static final String EMPTY_BODY = "{}";
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public Object getBodyAsObject(Object jsonBody) {
        Object body;
        try {
            if (jsonBody instanceof String s) {
                body = OBJECT_MAPPER.readValue(Objects.toString(s, EMPTY_BODY), Object.class);
            } else if (jsonBody instanceof Map<?, ?> map) {
                body = OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(map), Object.class);
            } else {
                body = getBodyAsObject(OBJECT_MAPPER.writeValueAsString(jsonBody));
            }
            return body;
        } catch (IOException e) {
            return jsonBody;
        }
    }

}