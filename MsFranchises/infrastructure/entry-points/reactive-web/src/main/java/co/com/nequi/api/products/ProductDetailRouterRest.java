package co.com.nequi.api.products;

import co.com.nequi.api.products.doc.ProductDetailOpenApi;
import lombok.RequiredArgsConstructor;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class ProductDetailRouterRest {

    private final ProductDetailProperties props;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionProductDetail(ProductDetailHandler handler) {
        return SpringdocRouteBuilder.route()
                .POST(props.consultPathBase().concat(props.productDetail()),
                        handler::retrieve,
                        ProductDetailOpenApi.getOpenAPI())
                .build();
    }

}