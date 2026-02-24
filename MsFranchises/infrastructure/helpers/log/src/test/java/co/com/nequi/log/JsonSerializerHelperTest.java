package co.com.nequi.log;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;

class JsonSerializerHelperTest {

    @Test
    @DisplayName("shouldParseValidJsonStringToObject")
    void shouldParseValidJsonStringToObject() {
        Object out = JsonSerializerHelper.getBodyAsObject("{\"a\":1}");
        assertInstanceOf(Map.class, out);
        assertEquals(1, ((Map<?, ?>) out).get("a"));
    }

    @Test
    @DisplayName("shouldReturnOriginalWhenInvalidJson")
    void shouldReturnOriginalWhenInvalidJson() {
        String invalid = "{a:1,}";
        Object out = JsonSerializerHelper.getBodyAsObject(invalid);
        assertSame(invalid, out);
    }

    @Test
    @DisplayName("shouldSerializeMapToObject")
    void shouldSerializeMapToObject() {
        var input = Map.of("k", "v");
        Object out = JsonSerializerHelper.getBodyAsObject(input);
        assertInstanceOf(Map.class, out);
        assertEquals("v", ((Map<?, ?>) out).get("k"));
    }
}
