package co.com.nequi.log;

import co.com.nequi.log.datatest.TestDataFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TechMessageHelperTest {

    @Test
    @DisplayName("shouldBuildErrorMapWithTraceCauseAndException")
    void shouldBuildErrorMapWithTraceCauseAndException() {
        var map = TechMessageHelper.getErrorObjectMessage(new IllegalStateException("oops"));
        assertTrue(map.containsKey(LogConstants.TRACE.getName()));
        assertEquals("oops", map.get(LogConstants.CAUSE.getName()));
        assertTrue(map.get(LogConstants.EXCEPTION.getName()).toString().contains("IllegalStateException"));
    }

    @Test
    @DisplayName("shouldJoinHeaderValuesAndReturnEmptyWhenMissing")
    void shouldJoinHeaderValuesAndReturnEmptyWhenMissing() {
        var headers = Map.of("k", List.of("a", "b"));
        assertEquals("a,b", TechMessageHelper.getHeader(headers, "k"));
        assertEquals("", TechMessageHelper.getHeader(headers, "missing"));
    }

    @Test
    @DisplayName("shouldFlattenHeadersMapAndHandleNull")
    void shouldFlattenHeadersMapAndHandleNull() {
        var flat = TechMessageHelper.getHeaders(Map.of("k", List.of("a", "b")));
        assertEquals("a,b", flat.get("k"));

        var none = TechMessageHelper.getHeaders(null);
        assertTrue(none.isEmpty());
    }

    @Test
    @DisplayName("shouldGetFirstHeaderOrEmptyString")
    void shouldGetFirstHeaderOrEmptyString() {
        HttpHeaders h = new HttpHeaders();
        h.add(LogConstants.MESSAGE_ID.getName(), TestDataFactory.MESSAGE_ID);
        assertEquals(TestDataFactory.MESSAGE_ID, TechMessageHelper.getFirstHeader(h, LogConstants.MESSAGE_ID));

        assertEquals("", TechMessageHelper.getFirstHeader(new HttpHeaders(), LogConstants.MESSAGE_ID));
    }

    @Test
    @DisplayName("shouldBuildInfoMessageRequestResponse")
    void shouldBuildInfoMessageRequestResponse() {
        var params = Map.of(
                LogConstants.HEADERS.getName(), Map.of(
                        LogConstants.REQUEST.getName(), Map.of("a", "b"),
                        LogConstants.RESPONSE.getName(), Map.of("x", "y")
                ),
                LogConstants.REQUEST.getName(), Map.of("foo", "bar"),
                LogConstants.RESPONSE.getName(), Map.of("baz", "qux"),
                "Start", 123L,
                "End", 456L
        );

        var msg = TechMessageHelper.getInfoMessageRequestResponse(params);
        var req = (Map<?, ?>) msg.get(LogConstants.REQUEST.getName());
        var res = (Map<?, ?>) msg.get(LogConstants.RESPONSE.getName());

        assertEquals("123", req.get(LogConstants.TIMESTAMP.getName()));
        assertEquals("456", res.get(LogConstants.TIMESTAMP.getName()));
        assertInstanceOf(Map.class, req.get(LogConstants.BODY.getName()));
        assertInstanceOf(Map.class, res.get(LogConstants.BODY.getName()));
    }
}
