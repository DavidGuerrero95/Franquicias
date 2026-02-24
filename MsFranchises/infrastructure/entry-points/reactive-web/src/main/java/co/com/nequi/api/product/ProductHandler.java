package co.com.nequi.api.product;

import co.com.nequi.api.commons.util.RequestUtil;
import co.com.nequi.api.product.dto.ProductCreateRQ;
import co.com.nequi.api.product.dto.ProductRenameRQ;
import co.com.nequi.api.product.dto.ProductStockUpdateRQ;
import co.com.nequi.api.product.mapper.ProductMapper;
import co.com.nequi.dto.transaction.Transaction;
import co.com.nequi.model.product.command.ProductDelete;
import co.com.nequi.model.product.command.ProductRename;
import co.com.nequi.usecase.product.CreateProductUseCase;
import co.com.nequi.usecase.product.DeleteProductUseCase;
import co.com.nequi.usecase.product.RenameProductUseCase;
import co.com.nequi.usecase.product.UpdateProductStockUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProductHandler {

    private final RequestUtil requestUtil;
    private final ProductMapper mapper;

    private final CreateProductUseCase createProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final UpdateProductStockUseCase updateProductStockUseCase;
    private final RenameProductUseCase renameProductUseCase;

    public Mono<ServerResponse> create(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var branchId = Long.valueOf(request.pathVariable("branchId"));

        return request.bodyToMono(ProductCreateRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(mapper.toCreate(branchId, rq.data().name(), rq.data().stock()))
                        .build())
                .flatMap(createProductUseCase::execute)
                .flatMap(tx -> ServerResponse.status(tx.getStatusCode()).bodyValue(mapper.toRs(tx)));
    }

    public Mono<ServerResponse> delete(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var branchId = Long.valueOf(request.pathVariable("branchId"));
        var productId = Long.valueOf(request.pathVariable("productId"));

        return Mono.just(Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(new ProductDelete(branchId, productId))
                        .build())
                .flatMap(deleteProductUseCase::execute)
                .flatMap(tx -> ServerResponse.noContent().build());
    }

    public Mono<ServerResponse> updateStock(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var productId = Long.valueOf(request.pathVariable("productId"));

        return request.bodyToMono(ProductStockUpdateRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(mapper.toStockUpdate(productId, rq.data().stock()))
                        .build())
                .flatMap(updateProductStockUseCase::execute)
                .flatMap(tx -> ServerResponse.ok().bodyValue(mapper.toRs(tx)));
    }

    public Mono<ServerResponse> rename(ServerRequest request) {
        Map<String, String> headers = (Map<String, String>) request.attributes().get("validatedHeaders");
        var productId = Long.valueOf(request.pathVariable("productId"));

        return request.bodyToMono(ProductRenameRQ.class)
                .flatMap(dto -> requestUtil.checkBodyRequest(dto, request))
                .map(rq -> Transaction.builder()
                        .headers(headers)
                        .route(request.path())
                        .request(new ProductRename(productId, rq.data().name()))
                        .build())
                .flatMap(renameProductUseCase::execute)
                .flatMap(tx -> ServerResponse.ok().bodyValue(mapper.toRs(tx)));
    }
}