package co.com.nequi.log;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import lombok.experimental.UtilityClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Iterator;
import java.util.Locale;
import java.util.Set;

@UtilityClass
public class LogWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(LogWriter.class.getName());
    private static final String MASK = "********";
    private static volatile ObjectMapper mapper = new ObjectMapper();
    private static volatile Set<String> maskedKeys = Set.of();

    public static void configure(ObjectMapper objectMapper, Set<String> keysToMask) {
        if (objectMapper != null) {
            mapper = objectMapper;
        }
        if (keysToMask != null) {
            maskedKeys = keysToMask;
        }
    }

    public void info(Object payload) {
        LOGGER.info(toJsonSafe(mask(payload)));
    }

    public void debug(Object payload) {
        LOGGER.debug(toJsonSafe(mask(payload)));
    }

    public void error(Object payload) {
        LOGGER.error(toJsonSafe(mask(payload)));
    }

    public void error(String message, Object payload) {
        LOGGER.error("{} {}", message, toJsonSafe(mask(payload)));
    }

    private Object mask(Object input) {
        try {
            JsonNode node = mapper.valueToTree(input);
            JsonNode masked = maskNode(node);
            return mapper.treeToValue(masked, Object.class);
        } catch (JsonProcessingException e) {
            return input;
        }
    }

    private JsonNode maskNode(JsonNode node) {
        if (node == null || node.isNull() || maskedKeys.isEmpty()) return node;

        if (node.isObject()) {
            ObjectNode obj = (ObjectNode) node;
            Iterator<String> it = obj.fieldNames();
            while (it.hasNext()) {
                String field = it.next();
                JsonNode child = obj.get(field);
                if (shouldMask(field)) {
                    obj.set(field, maskedLeaf(child));
                } else {
                    obj.set(field, maskNode(child));
                }
            }
            return obj;
        }
        if (node.isArray()) {
            ArrayNode arr = (ArrayNode) node;
            for (int i = 0; i < arr.size(); i++) {
                arr.set(i, maskNode(arr.get(i)));
            }
            return arr;
        }
        return node;
    }

    private boolean shouldMask(String fieldName) {
        return maskedKeys.contains(fieldName.toLowerCase(Locale.ROOT));
    }

    private JsonNode maskedLeaf(JsonNode child) {
        if (child == null || child.isNull()) return TextNode.valueOf(MASK);
        if (child.isContainerNode()) return TextNode.valueOf(MASK);
        return TextNode.valueOf(MASK);
    }

    private String toJsonSafe(Object value) {
        try {
            return mapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            return "{\"error\":\"failed-to-serialize-log\"}";
        }
    }
}