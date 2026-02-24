package co.com.nequi.api.products.doc;

import co.com.nequi.api.products.datatest.TestDataProductDetail;
import org.junit.jupiter.api.Test;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductDetailOpenApiTest {

    @Test
    void shouldExposeOpenApiConsumer() {
        assertNotNull(ProductDetailOpenApi.getOpenAPI());
    }

    @Test
    void shouldApplyOpenApiConsumerWhenBuildingRoute() {
        RouterFunction<ServerResponse> router = SpringdocRouteBuilder.route()
                .POST(TestDataProductDetail.BASE_PATH + TestDataProductDetail.PRODUCT_DETAIL_PATH,
                        request -> ServerResponse.ok().bodyValue("ok"),
                        ProductDetailOpenApi.getOpenAPI())
                .build();

        assertNotNull(router);
    }
}
