package co.com.nequi.api.branch;

import co.com.nequi.api.branch.dto.BranchCreateRQ;
import co.com.nequi.api.branch.dto.BranchRenameRQ;
import co.com.nequi.api.branch.mapper.BranchMapper;
import co.com.nequi.api.commons.util.RequestUtil;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.branch.command.BranchRename;
import co.com.nequi.usecase.branch.CreateBranchUseCase;
import co.com.nequi.usecase.branch.RenameBranchUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class BranchHandler {

    private final RequestUtil requestUtil;
    private final BranchMapper mapper;
    private final CreateBranchUseCase createBranchUseCase;
    private final RenameBranchUseCase renameBranchUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var franchiseId = Long.valueOf(request.pathVariable("franchiseId"));

        return request.bodyToMono(BranchCreateRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(mapper.toCreate(franchiseId, rq.data().name()))
                        .build())
                .flatMap(createBranchUseCase::execute)
                .flatMap(tx -> ServerResponse.status(tx.getStatusCode()).bodyValue(mapper.toRs(tx)));
    }

    public Mono<ServerResponse> rename(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var branchId = Long.valueOf(request.pathVariable("branchId"));

        return request.bodyToMono(BranchRenameRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(new BranchRename(branchId, rq.data().name()))
                        .build())
                .flatMap(renameBranchUseCase::execute)
                .flatMap(tx -> ServerResponse.status(tx.getStatusCode()).bodyValue(mapper.toRs(tx)));
    }
}