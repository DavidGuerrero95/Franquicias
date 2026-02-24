package co.com.nequi.api.product;

import co.com.nequi.api.product.doc.ProductCreateOpenApi;
import co.com.nequi.api.product.doc.ProductDeleteOpenApi;
import co.com.nequi.api.product.doc.ProductRenameOpenApi;
import co.com.nequi.api.product.doc.ProductStockUpdateOpenApi;
import lombok.RequiredArgsConstructor;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class ProductRouterRest {

    private final ProductRoutesProperties props;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionProduct(ProductHandler handler) {
        return SpringdocRouteBuilder.route()
                .POST(
                        props.consultPathBase().concat(props.productCreate()),
                        handler::create,
                        ProductCreateOpenApi.getOpenAPI()
                )
                .DELETE(
                        props.consultPathBase().concat(props.productDelete()),
                        handler::delete,
                        ProductDeleteOpenApi.getOpenAPI()
                )
                .PUT(
                        props.consultPathBase().concat(props.productStockUpdate()),
                        handler::updateStock,
                        ProductStockUpdateOpenApi.getOpenAPI()
                )
                .PUT(
                        props.consultPathBase().concat(props.productRename()),
                        handler::rename,
                        ProductRenameOpenApi.getOpenAPI()
                )
                .build();
    }
}