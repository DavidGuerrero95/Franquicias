package co.com.nequi.log;

import co.com.nequi.log.datatest.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TechMessageTest {


    @Test
    @DisplayName("shouldBuildTechMessageWithParamsRequestResponse")
    void shouldBuildTechMessageWithParamsRequestResponse() {
        Map<String, Serializable> params = (Map<String, Serializable>) (Map) TestDataFactory.paramsForTechMessage();
        var msg = TechMessage.buildTechMessage(Map.of("rq", 1), Map.of("rs", 2), params, "Clazz");
        assertEquals("/resource/action", msg.getAction());
        var data = (Map<?, ?>) msg.getData();
        assertTrue(data.containsKey(LogConstants.REQUEST.getName()));
        assertTrue(data.containsKey(LogConstants.RESPONSE.getName()));
    }

    @Test
    @DisplayName("shouldBuildTechMessageErrorWithName")
    void shouldBuildTechMessageErrorWithName() {
        var msg = TechMessage.buildTechMessageError(new RuntimeException("bad"), "component", "Clazz");
        assertNull(msg.getMessageId());
        assertEquals("component", msg.getAction());
    }
}
