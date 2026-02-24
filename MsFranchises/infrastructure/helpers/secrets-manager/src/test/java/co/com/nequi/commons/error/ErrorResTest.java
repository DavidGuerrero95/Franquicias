package co.com.nequi.commons.error;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorResTest {

    @Test
    void dataBuilderShouldBuildProperly() {
        var d = ErrorRes.Data.builder()
                .type("T")
                .reason("R")
                .domain("/d")
                .code("C")
                .message("M")
                .build();

        assertEquals("T", d.getType());
        assertEquals("R", d.getReason());
        assertEquals("/d", d.getDomain());
        assertEquals("C", d.getCode());
        assertEquals("M", d.getMessage());
    }

    @Test
    void errorResBuilderShouldHoldList() {
        var d = ErrorRes.Data.builder().code("X").build();
        var res = ErrorRes.builder().errors(List.of(d)).build();
        assertNotNull(res.getErrors());
        assertEquals(1, res.getErrors().size());
        assertEquals("X", res.getErrors().getFirst().getCode());
    }

}