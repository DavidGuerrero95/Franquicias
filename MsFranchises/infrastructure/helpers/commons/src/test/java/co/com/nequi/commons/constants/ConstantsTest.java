package co.com.nequi.commons.constants;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ConstantsTest {

    @Test
    void shouldExposeExpectedConstants() {
        assertAll(
                () -> assertEquals("message-id", Constants.MESSAGE_ID),
                () -> assertEquals("", Constants.EMPTY),
                () -> assertEquals("channel", Constants.CHANNEL),
                () -> assertEquals("Content-Type", Constants.CONTENT_TYPE),
                () -> assertEquals("accept", Constants.ACCEPT),
                () -> assertEquals("200", Constants.SUCCESSFUL),
                () -> assertEquals("409", Constants.CONFLICT),
                () -> assertEquals("500", Constants.INTERNAL_SERVER_ERROR)
        );
    }
}