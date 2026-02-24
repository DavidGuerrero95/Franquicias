package co.com.nequi.api.franchise;

import co.com.nequi.api.commons.util.RequestUtil;
import co.com.nequi.api.franchise.dto.FranchiseCreateRQ;
import co.com.nequi.api.franchise.dto.FranchiseRenameRQ;
import co.com.nequi.api.franchise.mapper.FranchiseMapper;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.franchise.command.FranchiseRename;
import co.com.nequi.model.product.query.TopStockQuery;
import co.com.nequi.usecase.franchise.CreateFranchiseUseCase;
import co.com.nequi.usecase.franchise.RenameFranchiseUseCase;
import co.com.nequi.usecase.product.RetrieveTopStockByBranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class FranchiseHandler {

    private final RequestUtil requestUtil;
    private final FranchiseMapper mapper;

    private final CreateFranchiseUseCase createFranchiseUseCase;
    private final RenameFranchiseUseCase renameFranchiseUseCase;
    private final RetrieveTopStockByBranchUseCase retrieveTopStockByBranchUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        return request.bodyToMono(FranchiseCreateRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(mapper.toModel(rq))
                        .build())
                .flatMap(createFranchiseUseCase::execute)
                .flatMap(tx -> ServerResponse.status(tx.getStatusCode()).bodyValue(mapper.toRs(tx)));
    }

    public Mono<ServerResponse> rename(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var franchiseId = Long.valueOf(request.pathVariable("franchiseId"));

        return request.bodyToMono(FranchiseRenameRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(new FranchiseRename(franchiseId, rq.data().name()))
                        .build())
                .flatMap(renameFranchiseUseCase::execute)
                .flatMap(tx -> ServerResponse.status(tx.getStatusCode()).bodyValue(mapper.toRs(tx)));
    }

    public Mono<ServerResponse> topStock(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var franchiseId = Long.valueOf(request.pathVariable("franchiseId"));

        return Mono.just(Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(new TopStockQuery(franchiseId))
                        .build())
                .flatMap(retrieveTopStockByBranchUseCase::execute)
                .flatMap(tx -> ServerResponse.ok().bodyValue(mapper.toTopStockRs(tx)));
    }
}