package co.com.nequi.api.branch;

import co.com.nequi.api.branch.doc.BranchCreateOpenApi;
import co.com.nequi.api.branch.doc.BranchRenameOpenApi;
import lombok.RequiredArgsConstructor;
import org.springdoc.webflux.core.fn.SpringdocRouteBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
@RequiredArgsConstructor
public class BranchRouterRest {

    private final BranchRoutesProperties props;

    @Bean
    public RouterFunction<ServerResponse> routerFunctionBranch(BranchHandler handler) {
        return SpringdocRouteBuilder.route()
                .POST(props.consultPathBase().concat(props.branchCreate()),
                        handler::create,
                        BranchCreateOpenApi.getOpenAPI())
                .PUT(props.consultPathBase().concat(props.branchRename()),
                        handler::rename,
                        BranchRenameOpenApi.getOpenAPI())
                .build();
    }
}