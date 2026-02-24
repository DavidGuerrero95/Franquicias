package co.com.nequi.api.franchise;

import co.com.nequi.api.franchise.doc.FranchiseCreateOpenApi;
import co.com.nequi.api.franchise.doc.FranchiseRenameOpenApi;
import co.com.nequi.api.franchise.doc.TopStockOpenApi;
import lombok.RequiredArgsConstructor;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class FranchiseRouterRest {

    private final FranchiseRoutesProperties props;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionFranchise(FranchiseHandler handler) {
        return SpringdocRouteBuilder.route()
                .POST(
                        props.consultPathBase().concat(props.franchiseCreate()),
                        handler::create,
                        FranchiseCreateOpenApi.getOpenAPI()
                )
                .PUT(
                        props.consultPathBase().concat(props.franchiseRename()),
                        handler::rename,
                        FranchiseRenameOpenApi.getOpenAPI()
                )
                .GET(
                        props.consultPathBase().concat(props.topStockByFranchise()),
                        handler::topStock,
                        TopStockOpenApi.getOpenAPI()
                )
                .build();
    }
}